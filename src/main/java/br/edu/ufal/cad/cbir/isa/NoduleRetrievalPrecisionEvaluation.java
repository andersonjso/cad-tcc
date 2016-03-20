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

import br.edu.ufal.AttributesNodule;
import com.mongodb.*;
import org.jocl.*;

import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.jocl.CL.*;

public class NoduleRetrievalPrecisionEvaluation {

	private static String kernelEuclidean = "#pragma OPENCL EXTENSION cl_khr_fp64: enable \n"
			+" __kernel void euclideanDistance("
			+ "__global const double *refArray, "
			+ "__global const double *dbArray, "
			+ "int sizeArray, int numberArrays, "
			+ "__global double *output){"
			+ "  int id = get_global_id(0);"
			+ "	 if(id < numberArrays){"
			+ "		double sum = 0;"
			+ "		for(int i = 0; i < sizeArray; i++)"
			+ " 		sum += pow((dbArray[(id*sizeArray) + i]-refArray[i]),2);"
			+ "	    output[id] = sqrt(sum);"
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
			+ "		double sum = 0;"
			+ "		double temp;"
			+ "		for(int i = 0; i < sizeArray; i++){"
			+ "			temp = dbArray[(id*sizeArray) + i]-refArray[i];"
			+ "			if(temp < 0)"
			+ "				temp = -temp;"
			+ " 		sum += temp;"
			+ "		}"
			+ "	    output[id] = sum;"
			+ "	 }"
			+ "}";

	private MongoClient mongoClient = new MongoClient();
	private DB db = mongoClient.getDB("test");
	private DBCollection col = db.getCollection("exams");
	private BasicDBObject exam;
	private BasicDBObject reading;
	private BasicDBList big;
	private BasicDBObject nodule;
	private String malRef;
	//		BasicDBList att = (BasicDBList) nodule.get("marginAttributes3D");
	BasicDBList att;

	public List<SimilarNodule> retrieveSimilarNodules(double[] arrayReference) throws UnknownHostException {
		//VETOR DE REFERENCIA
//		AttributesNodule attributesNodule = new AttributesNodule();
//		double[] arrayReference = attributesNodule.getImageTextureAttributes(image);

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
				if(nodule.containsField("textureAttributes") && !nodule.get("malignancy").equals("3")
						&& !nodule.get("malignancy").equals("2") && !nodule.get("malignancy").equals("4")){
//					att = (BasicDBList) nodule.get("marginAttributes3D");
					att = (BasicDBList) nodule.get("textureAttributes");
					array = new double[att.size()];
					for (int t = 0; t < att.size(); t++) {
						array[t] = Double.parseDouble(att.get(t).toString());
					}
//					for (int t = 0; t < att2.size(); t++)
//						array[t+att.size()] = Double.parseDouble(att2.get(t).toString());
					nodules.add(new SimilarNodule(
							exam.getString("_id"),
							Integer.parseInt(nodule.getString("malignancy")),
							array,
							exam.getString("path"),
							nodule.getString("noduleID"),
							Integer.parseInt(nodule.getString("texture")),
							Integer.parseInt(nodule.getString("subtlety")),
							Integer.parseInt(nodule.getString("calcification")),
							Integer.parseInt(nodule.getString("internalStructure")),
							Integer.parseInt(nodule.getString("lobulation")),
							Integer.parseInt(nodule.getString("margin")),
							Integer.parseInt(nodule.getString("sphericity")),
						    Integer.parseInt(nodule.getString("spiculation"))));
				}
			}
		}

		int numberNodules = nodules.size();
		double[] arrayDB = new double[nodules.size()*sizeArray];
		for (int i = 0; i < nodules.size(); i++) {
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
		long global_work_size[] = new long[] { wiwg*nwg };
		long local_work_size[] = new long[] { wiwg };

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

		for (int i = 0; i < numberNodules; i++)
			nodules.get(i).setDistance(distances[i]);

		Collections.sort(nodules, new Comparator<SimilarNodule>() {
			@Override
			public int compare(SimilarNodule o1, SimilarNodule o2) {
				// TODO Auto-generated method stub
				return new Double(o1.getDistance()).compareTo(o2.getDistance());
			}
		});

		int maligno = 0;
		int benigno = 0;
		int size = 0;

//		PRECISION X RECALL
//		for (int i = 0; i < nodules.size(); i++) {
//			size++;
//			if(nodules.get(i).getMalignancy() == 5){
//				maligno++;
////				if(maligno == 16)
////					System.out.println(size);
////				else if(maligno == 32)
////					System.out.println(size);
////				else if(maligno == 48)
////					System.out.println(size);
////				else if(maligno == 64)
////					System.out.println(size);
////				else if(maligno == 80)
////					System.out.println(size);
////				else if(maligno == 96)
////					System.out.println(size);
////				else if(maligno == 112)
////					System.out.println(size);
////				else if(maligno == 128)
////					System.out.println(size);
////				else if(maligno == 144)
////					System.out.println(size);
////				else if(maligno == 160)
////					System.out.println(size);
//			}
//			else if(nodules.get(i).getMalignancy() == 1){
//				benigno++;
//				if(benigno == 27)
//					System.out.println(size);
//				else if(benigno == 55)
//					System.out.println(size);
//				else if(benigno == 82)
//					System.out.println(size);
//				else if(benigno == 109)
//					System.out.println(size);
//				else if(benigno == 137)
//					System.out.println(size);
//				else if(benigno == 164)
//					System.out.println(size);
//				else if(benigno == 191)
//					System.out.println(size);
//				else if(benigno == 218)
//					System.out.println(size);
//				else if(benigno == 246)
//					System.out.println(size);
//				else if(benigno == 273)
//					System.out.println(size);
//			}
//		}
//
//		System.out.println("size "+size);
//		System.out.println("maligno "+maligno);
//		System.out.println("benigno "+benigno);
//
//		PRECISION(10)

		ArrayList<SimilarNodule> listSimilarNodules = new ArrayList<>();
		for (int i = 1; i < 11; i++) {
			listSimilarNodules.add(nodules.get(i));
//
//			if(nodules.get(i).getMalignancy() == 5){
//				System.out.println(nodules.get(i).getPath()
//						+ " and " +
//						nodules.get(i).getIdNodule());
//				System.out.println("M");
//			}
//			else
//				System.out.println("B");
		}

		return listSimilarNodules;
	}
	public static void main(String[] args) throws UnknownHostException {

		String path = "/LIDC-IDRI/LIDC-IDRI-0001";//exame referencia
//		System.out.println(path);

		NoduleRetrievalPrecisionEvaluation noduleRetrievalPrecisionEvaluation = new NoduleRetrievalPrecisionEvaluation();

//		List<SimilarNodule> similarNodules = noduleRetrievalPrecisionEvaluation.retrieveSimilarNodules(path);

//		for(SimilarNodule similarNodule : similarNodules){
//			System.out.println(similarNodule.getPath());
//		}


	}

}