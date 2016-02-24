/*
 * Copyright (c) 2015 Universidade Federal de Alagoas
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package br.edu.ufal.cad.cbir.isa;

import com.mongodb.*;
import org.jocl.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.jocl.CL.*;

public class NoduleRetrievalPerformanceEvaluation {
	
	private static String kernelEuclidean = "#pragma OPENCL EXTENSION cl_khr_fp64: enable \n"
			+" __kernel void euclideanDistance("
			+ "__global const double *refArray, "
			+ "__global const double *dbArray, "
			+ "int sizeArray, int numberArrays, " 
			+ "__global double *output){"
			+ "  int id = get_global_id(0);" 
			+ "	 if(id < numberArrays){"
//			+ "	 	int cycles = numberArrays / get_global_size(0);"
//			+ "	 	for(int j = 0; j < cycles; j = j + get_global_size(0)){"
			+ "			double sum = 0;" 
			+ "			for(int i = 0; i < sizeArray; i++)"
			+ " 			sum += pow((dbArray[((id)*sizeArray) + i]-refArray[i]),2);"//id+j
			+ "	    	output[id] = sqrt(sum);"//id+j
//			+ "	 	}"
			+ "	 }" 
			+ "}";
	
	private static String kernelManhattan = "#pragma OPENCL EXTENSION cl_khr_fp64: enable \n"
			+" __kernel void manhattanDistance("
			+ "__global const double *refArray, "
			+ "__global const double *dbArray, "
			+ "int sizeArray, int numberArrays, " 
			+ "__global double *output){"
			+ "  int id = get_global_id(0);" 
			+ "	 if(id < numberArrays){"
//			+ "	 	int cycles = numberArrays / get_global_size(0);"
//			+ "	 	for(int j = 0; j < cycles; j = j + get_global_size(0)){"
			+ "			double sum = 0;" 
			+ "			double temp;"
			+ "			for(int i = 0; i < sizeArray; i++){"
			+ "				temp = dbArray[((id)*sizeArray) + i]-refArray[i];"//id+j
			+ "				if(temp < 0)"
			+ "					temp = -temp;"	
			+ " 			sum += temp;"
			+ "			}"
			+ "	    	output[id] = sum;"//id+j 
//			+ "	 	}"
			+ "	 }" 
			+ "}";
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		String path = "/LIDC-IDRI/LIDC-IDRI-0001";//exame referencia
//		System.out.println(path);
		
		MongoClient mongoClient = new MongoClient();
		DB db = mongoClient.getDB("public");
		DBCollection col = db.getCollection("exams");		
		BasicDBObject exam = (BasicDBObject) col.findOne(new BasicDBObject("path", java.util.regex.Pattern.compile(path)));
		BasicDBObject reading = (BasicDBObject) exam.get("readingSession");
		BasicDBList big = (BasicDBList) reading.get("bignodule");
		BasicDBObject nodule = (BasicDBObject) big.get(0);
		String malRef = nodule.getString("malignancy");		
		BasicDBList att = (BasicDBList) nodule.get("marginAttributes3D");
		BasicDBList att2 = (BasicDBList) nodule.get("textureAttributes");
		
		//VETOR DE REFERENCIA
		double[] arrayReference = new double[att.size()+att2.size()];//+att2.size()
		for (int i = 0; i < att.size(); i++)
			arrayReference[i] = Double.parseDouble(att.get(i).toString());
		for (int i = 0; i < att2.size(); i++)
			arrayReference[i+att.size()] = Double.parseDouble(att2.get(i).toString());
		
		int sizeArray = arrayReference.length;
//		System.out.println("malRef "+malRef);
		
		//VETORES DO BANCO
		double[] array;
		ArrayList<SimilarNodule> nodules = new ArrayList<SimilarNodule>();
		DBCursor cursor = col.find();
		
		while(cursor.hasNext()){
			exam = (BasicDBObject) cursor.next();
			reading = (BasicDBObject) exam.get("readingSession");
			big = (BasicDBList) reading.get("bignodule");
			
			for (int i = 0; i < big.size(); i++) {
				nodule = (BasicDBObject) big.get(i);
				if(nodule.containsField("marginAttributes3D") && nodule.containsField("textureAttributes") && !nodule.get("malignancy").equals("3")){
//						&& !nodule.get("malignancy").equals("2") && !nodule.get("malignancy").equals("4")
					att = (BasicDBList) nodule.get("marginAttributes3D");
					att2 = (BasicDBList) nodule.get("textureAttributes");
					array = new double[att.size()+att2.size()];//+att2.size()			
					for (int t = 0; t < att.size(); t++)
						array[t] = Double.parseDouble(att.get(t).toString());
					for (int t = 0; t < att2.size(); t++)
						array[t+att.size()] = Double.parseDouble(att2.get(t).toString());
					
					nodules.add(new SimilarNodule(Integer.parseInt(nodule.getString("malignancy")), array));
				}				
			}
		}
		
		int numberNodules = 1171;		
		double[] arrayDB = new double[numberNodules*sizeArray];
		for (int i = 0; i < numberNodules; i++) {
			array = nodules.get(i).getAttributes();
			for (int j = 0; j < sizeArray; j++) {
				arrayDB[i*sizeArray+j] = array[j];
			}				
		}	
		
		//ANÁLISE DE SIMILARIDADE DOS NÓDULOS
		final int platformIndex = 0;
		final long deviceType = CL_DEVICE_TYPE_ALL;
		final int deviceIndex = 0;

		// Enable exceptions and subsequently omit error checks in this sample
		CL.setExceptionsEnabled(true);

		// Obtain the number of platforms
		int numPlatformsArray[] = new int[1];
		clGetPlatformIDs(0, null, numPlatformsArray);
		int numPlatforms = numPlatformsArray[0];

		// Obtain a platform ID
		cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
		clGetPlatformIDs(platforms.length, platforms, null);
		cl_platform_id platform = platforms[platformIndex];

		// Initialize the context properties
		cl_context_properties contextProperties = new cl_context_properties();
		contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

		// Obtain the number of devices for the platform
		int numDevicesArray[] = new int[1];
		clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
		int numDevices = numDevicesArray[0];

		// Obtain a device ID
		cl_device_id devices[] = new cl_device_id[numDevices];
		clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
		cl_device_id device = devices[deviceIndex];

		// Create a context for the selected device
		cl_context context = clCreateContext(contextProperties, 1,
				new cl_device_id[] { device }, null, null, null);

		// Create a command-queue for the selected device
		cl_command_queue commandQueue = clCreateCommandQueue(context, device,
				CL.CL_QUEUE_PROFILING_ENABLE, null);

		// Allocate the memory objects for the input- and output data
		cl_mem memObjects[] = new cl_mem[3];

		cl_event memEvent0 = new cl_event();
		memObjects[0] = clCreateBuffer(context, CL_MEM_READ_ONLY,
				Sizeof.cl_double * sizeArray, null, null);
		clEnqueueWriteBuffer(commandQueue, memObjects[0], CL_TRUE, 0,
				Sizeof.cl_double * sizeArray, 
				Pointer.to(arrayReference), 0, null, memEvent0);
		clWaitForEvents(1, new cl_event[] { memEvent0 });

		cl_event memEvent1 = new cl_event();
		memObjects[1] = clCreateBuffer(context, CL_MEM_READ_ONLY,
				Sizeof.cl_double * (sizeArray * numberNodules), null, null);
		clEnqueueWriteBuffer(commandQueue, memObjects[1], CL_TRUE, 0,
				Sizeof.cl_double * (sizeArray * numberNodules),
				Pointer.to(arrayDB), 0, null, memEvent1);
		clWaitForEvents(1, new cl_event[] { memEvent1 });

		memObjects[2] = clCreateBuffer(context, CL_MEM_READ_WRITE,
				Sizeof.cl_double * numberNodules, null, null);

		// Create the program from the source code
		cl_program program = clCreateProgramWithSource(context, 1,
				new String[] {kernelEuclidean}, null, null);//kernelManhattan

		// Build the program
		clBuildProgram(program, 0, null, null, null, null);

		// Create the kernel
		cl_kernel kernel = clCreateKernel(program, "euclideanDistance", null);//manhattanDistance

		// Set the arguments for the kernel
		clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memObjects[0]));
		clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memObjects[1]));
		clSetKernelArg(kernel, 2, Sizeof.cl_int,
				Pointer.to(new int[] { sizeArray }));
		clSetKernelArg(kernel, 3, Sizeof.cl_int,
				Pointer.to(new int[] { numberNodules }));
		clSetKernelArg(kernel, 4, Sizeof.cl_mem, Pointer.to(memObjects[2]));
		
		int wiwg = 32;
		int nwg = (numberNodules + wiwg - 1) / wiwg;

		// Set the work-item dimensions
		long global_work_size[] = new long[] { wiwg*nwg };//wiwg*nwg
		long local_work_size[] = new long[] { wiwg };//wiwg

		cl_event kernelEvent = new cl_event();
		// Execute the kernel
		clEnqueueNDRangeKernel(commandQueue, kernel, 1, null, global_work_size,
				local_work_size, 0, null, kernelEvent);
		clWaitForEvents(1, new cl_event[] { kernelEvent });

		double[] distances = new double[numberNodules];
		
		cl_event readEvent = new cl_event();
		// Read the output data
		clEnqueueReadBuffer(commandQueue, memObjects[2], CL_TRUE, 0,
				Sizeof.cl_double * numberNodules, Pointer.to(distances), 0,
				null, readEvent);

		clWaitForEvents(1, new cl_event[] { readEvent });

		// Release kernel, program, and memory objects
		clReleaseMemObject(memObjects[0]);
		clReleaseMemObject(memObjects[1]);
		clReleaseMemObject(memObjects[2]);
		clReleaseKernel(kernel);
		clReleaseProgram(program);
		clReleaseCommandQueue(commandQueue);
		clReleaseContext(context);
		
//		BufferedReader br = new BufferedReader(new FileReader("resultsCPUATDE"));
//		
//		for (int i = 0; i < numberNodules; i++){ 
////			nodules.get(i).setDistance(distances[i]);
////			System.out.println(distances[i]);
//			if(distances[i] != Double.parseDouble(br.readLine()))
//				System.out.println("DEU MERDA");
//		}
//		
//		System.out.println("PASSOU");
		
		// Print the timing information for the commands
		ExecutionStatistics executionStatistics = new ExecutionStatistics();
//		executionStatistics.addEntry("memory0", memEvent0);
//		executionStatistics.addEntry("memory1", memEvent1);
		executionStatistics.addEntry("kernel", kernelEvent);
//		executionStatistics.addEntry("read", readEvent);
		executionStatistics.print();
//		System.out.println(executionStatistics.getTotalTime().replaceAll(" ", ""));		
		
	}
	
	static class ExecutionStatistics {
		/**
		 * A single entry of the ExecutionStatistics
		 */
		private static class Entry {
			private String name;
			private long submitTime[] = new long[1];
			private long queuedTime[] = new long[1];
			private long startTime[] = new long[1];
			private long endTime[] = new long[1];

			Entry(String name, cl_event event) {
				this.name = name;
				CL.clGetEventProfilingInfo(event,
						CL.CL_PROFILING_COMMAND_QUEUED, Sizeof.cl_ulong,
						Pointer.to(queuedTime), null);
				CL.clGetEventProfilingInfo(event,
						CL.CL_PROFILING_COMMAND_SUBMIT, Sizeof.cl_ulong,
						Pointer.to(submitTime), null);
				CL.clGetEventProfilingInfo(event,
						CL.CL_PROFILING_COMMAND_START, Sizeof.cl_ulong,
						Pointer.to(startTime), null);
				CL.clGetEventProfilingInfo(event, CL.CL_PROFILING_COMMAND_END,
						Sizeof.cl_ulong, Pointer.to(endTime), null);
			}

			void normalize(long baseTime) {
				submitTime[0] -= baseTime;
				queuedTime[0] -= baseTime;
				startTime[0] -= baseTime;
				endTime[0] -= baseTime;
			}

			long getQueuedTime() {
				return queuedTime[0];
			}

			void print() {
				System.out.println("Event " + name + ": ");
				System.out.println("Queued : "
						+ String.format("%8.3f", queuedTime[0] / 1e6) + " ms");
				System.out.println("Submit : "
						+ String.format("%8.3f", submitTime[0] / 1e6) + " ms");
				System.out.println("Start  : "
						+ String.format("%8.3f", startTime[0] / 1e6) + " ms");
				System.out.println("End    : "
						+ String.format("%8.3f", endTime[0] / 1e6) + " ms");

				long duration = endTime[0] - startTime[0];
				System.out.println(String.format("%8.3f", duration / 1e6));
			}
		}

		public String getTotalTime() {
			return String.format("%8.3f", entries.get(entries.size()-1).endTime[0] / 1e6);
		}

		/**
		 * The list of entries in this instance
		 */
		private List<Entry> entries = new ArrayList<Entry>();

		/**
		 * Adds the specified entry to this instance
		 * 
		 * @param name
		 *            A name for the event
		 * @param event
		 *            The event
		 */
		public void addEntry(String name, cl_event event) {
			entries.add(new Entry(name, event));
		}

		/**
		 * Removes all entries
		 */
		public void clear() {
			entries.clear();
		}

		/**
		 * Normalize the entries, so that the times are relative to the time
		 * when the first event was queued
		 */
		private void normalize() {
			long minQueuedTime = Long.MAX_VALUE;
			for (Entry entry : entries) {
				minQueuedTime = Math.min(minQueuedTime, entry.getQueuedTime());
			}
			for (Entry entry : entries) {
				entry.normalize(minQueuedTime);
			}
		}

		/**
		 * Print the statistics
		 */
		public void print() {
			normalize();
			for (Entry entry : entries) {
				entry.print();
			}
		}
	}

}
