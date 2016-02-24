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

import br.edu.ufal.cad.cbir.texture.coocurrencematrix.CMTextureAttributes;
import br.edu.ufal.cad.images.dicom.DicomImageFile;
import br.edu.ufal.cad.images.dicom.DicomReadeIOException;
import br.edu.ufal.cad.images.general.AbstractImage;
import br.edu.ufal.cad.mongodb.tags.Points;
import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import org.bson.types.ObjectId;
import org.jocl.*;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static org.jocl.CL.*;

public class ISATexture {

	private BufferedImage[] imagesReference;
	private double[] arrayReference;
	private double[] arrayDB;
	private ArrayList<SimilarNodule> vectorDB; //TODO vetor de nodulos
	private int sizeRetrieval = 1485; //TODO numero de nodulos recuperados
	
	int sizeArray = 36; //TODO tamanho do vetor de atributos
	int numberNodules;
	double[] distances;
	MongoClient mongoClient;
	DB db;
	DBCollection col;
	GridFS grid;
	GridFSDBFile file;
	
//	public ISATexture(BufferedImage[] img) {
//		mongoClient = new MongoClient();
//		db = mongoClient.getDB("test");
//        col = db.getCollection("exams");
//        grid = new GridFS(db,"images");
//		this.imagesReference = img;
//
//		getReferenceTextureAttributes();
//		getDBTextureAttributes();
////		runISA();
////		getNodulesImages();
//	}

    public ISATexture(BufferedImage[] img){
        this.imagesReference = img;
        getReferenceTextureAttributes();
    }

    public double[] getArrayReference(){
        return arrayReference;
    }
	
	public ISATexture() throws IOException {
		try {
			mongoClient = new MongoClient();
			db = mongoClient.getDB("test");
			col = db.getCollection("exams");
			grid = new GridFS(db,"images");
//			this.imagesReference = img;
			String path = "/LIDC-IDRI/LIDC-IDRI-0997"; //TODO inicio do id do path do exame
			System.out.println(path);
			BasicDBObject exam = (BasicDBObject) col.findOne(new BasicDBObject("path", java.util.regex.Pattern.compile(path)));
			BasicDBObject reading = (BasicDBObject) exam.get("readingSession");
			BasicDBList nodules = (BasicDBList) reading.get("bignodule");
			BasicDBObject nodule = (BasicDBObject) nodules.get(0);
			BasicDBList rois = (BasicDBList) nodule.get("roi");
			BasicDBObject roi;
			BufferedImage[] imgs = new BufferedImage[rois.size()];
			for (int i = 0; i < rois.size(); i++) {
				roi = (BasicDBObject) rois.get(i);
				file = grid.find(roi.getObjectId("noduleImage"));				
				imgs[i] = ImageIO.read(file.getInputStream());				 
			}
			imagesReference = imgs;
			
			BasicDBList att = (BasicDBList) nodule.get("normalizedTextureAttributesSem3");
			arrayReference = new double[sizeArray];
			for (int i = 0; i < arrayReference.length; i++) 
				arrayReference[i] = Double.parseDouble(att.get(i)+"");
			
			getDBTextureAttributes();
			runISA();
//			getNodulesImages();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}		
	}
	
	public BufferedImage[] getReferenceImages(){
		return this.imagesReference;
	}
	
	public int getSizeRetrieval() {
		return sizeRetrieval;
	}

	public void setSizeRetrieval(int sizeRetrieval) {
		this.sizeRetrieval = sizeRetrieval;
	}

	public ArrayList<SimilarNodule> getVectorDB() {
		return vectorDB;
	}

	public void setVectorDB(ArrayList<SimilarNodule> vectorDB) {
		this.vectorDB = vectorDB;
	}
	
	private void getNodulesImages() {		
		BufferedImage[] imgOriginal;
		BufferedImage[] nodOriginal;		
		String[] a;
		String[] b;
		String[] filename;
		BufferedImage buffer;
		BufferedImage img;
		Points[] p;
		
		for (int i = 0; i < sizeRetrieval; i++) {
			imgOriginal = new BufferedImage[vectorDB.get(i).getOriginalId().length];
			nodOriginal = new BufferedImage[vectorDB.get(i).getSegmentedId().length];
			filename = new String[vectorDB.get(i).getSegmentedId().length];
			
			for (int j = 0; j < imgOriginal.length; j++) {												
				try {
					a = vectorDB.get(i).getOriginalId();
					file = grid.find(new ObjectId(a[j]));
					filename[j] = file.getFilename();
					file.writeTo("./temp.dcm");
					buffer = this.readDicom("./temp.dcm");
					img = new BufferedImage(buffer.getWidth(), buffer.getHeight(), BufferedImage.TYPE_INT_RGB);
					
					for (int x = 0; x < img.getWidth(); x++) {
						for (int y = 0; y < img.getHeight(); y++) {							
								img.setRGB(x, y, buffer.getRGB(x, y));
						}
					}
					
					p = vectorDB.get(i).getPoints().get(j);					
					for (int pos = 0; pos < p.length; pos++)			
						img.setRGB(p[pos].getXCoord(), p[pos].getYCoord(), new Color(255, 0, 0).getRGB());
					
					imgOriginal[j] = img;
					
					b = vectorDB.get(i).getSegmentedId();
					file = grid.find(new ObjectId(b[j]));				
					nodOriginal[j] = ImageIO.read(file.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			vectorDB.get(i).setOriginalImage(imgOriginal);
			vectorDB.get(i).setNoduleImage(nodOriginal);
			vectorDB.get(i).setFilename(filename);
		}		
	}
	
	static BufferedImage readDicom(String path) {
		DicomImageFile dicom = null;
		try {
			dicom = new DicomImageFile(path);
		} catch (DicomReadeIOException e1) {
			e1.printStackTrace();
		}
		AbstractImage im = new AbstractImage(dicom);

		return im.getBufferImage();
	}

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

	private void runISA() {
//		for (int i = 0; i < arrayReference.length; i++) {
//			arrayReference[i] = arrayDB[i];
//		}
//		for (int i = 0; i < 360; i++) {
//			System.out.println(((int)i/36)+" "+arrayDB[i]);
//		}
		// The platform, device type and device number
		// that will be used
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
				new String[] { kernelEuclidean }, null, null);

		// Build the program
		clBuildProgram(program, 0, null, null, null, null);

		// Create the kernel
		cl_kernel kernel = clCreateKernel(program, "euclideanDistance", null);

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

		distances = new double[numberNodules];
		
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
		
		for (int i = 0; i < vectorDB.size(); i++) {
			vectorDB.get(i).setDistance(distances[i]);
		}
		
		Collections.sort(vectorDB, new Comparator<SimilarNodule>() {
			@Override
			public int compare(SimilarNodule o1, SimilarNodule o2) {
				// TODO Auto-generated method stub
				return new Double(o1.getDistance()).compareTo(o2.getDistance());
			}
		});
		
		int mal5 = 0;
		int mal4 = 0;
		int mal3 = 0;
		int mal2 = 0;
		int mal1 = 0;
		
		int size = 0;
		
		for (int i = 0; i < sizeRetrieval; i++) {
			size++;
			//TODO System.out.println(vectorDB.get(i).getMalignancy()); para retornar os 10 primeiros
			if(vectorDB.get(i).getMalignancy() == 1){
				mal1++;
				if(mal1 == 367) //TODO numero de nodulos relevantes recuperados
					System.out.println(size);
			}
			else if(vectorDB.get(i).getMalignancy() == 2)
				mal2++;
			else if(vectorDB.get(i).getMalignancy() == 3)
				mal3++;
			else if(vectorDB.get(i).getMalignancy() == 4)
				mal4++;
			else if(vectorDB.get(i).getMalignancy() == 5)
				mal5++;
				
		}
		
		System.out.println("Malignidade de Referência = "+vectorDB.get(0).getMalignancy());
		System.out.println("Mal1 "+mal1);
		System.out.println("Mal2 "+mal2);		
//		System.out.println("Malignidade 3 = "+mal3);
		System.out.println("Mal4 "+mal4);
		System.out.println("Mal5 "+mal5);
		System.out.println((mal1+mal2+mal4+mal5));
		
	}

//	private double[] getArrayReference()

	private void getDBTextureAttributes() {			
			DBCursor cursor = col.find();
			vectorDB = new ArrayList<SimilarNodule>();
			BasicDBList att;
			BasicDBObject rs;
			BasicDBObject exam;
			BasicDBList nod;
			SimilarNodule nodule;
			double[] temp;
			BasicDBList rois;
			BasicDBObject roi;
			String[] originals;
			String[] nodules;
			BasicDBList points;			
			BasicDBObject point;
			Points[] p;
			ArrayList<Points[]> listPoints;
			
			while (cursor.hasNext()) {				
				exam = (BasicDBObject) cursor.next();				
				rs = (BasicDBObject) exam.get("readingSession");
				nod = (BasicDBList) rs.get("bignodule");
				for (Object n : nod) {
					if(Integer.parseInt(((BasicDBObject) n).getString("malignancy")) != 3){
						nodule = new SimilarNodule();
						nodule.setIdExam(((BasicDBObject) exam).get("_id")+"");
						nodule.setPath(((BasicDBObject) exam).get("path")+"");
						nodule.setIdNodule(((BasicDBObject) n).get("noduleID")+"");
						nodule.setCalcification(Integer.parseInt(((BasicDBObject) n).get("calcification")+""));
						nodule.setInternalStructure(Integer.parseInt(((BasicDBObject) n).get("internalStructure")+""));
						nodule.setLobulation(Integer.parseInt(((BasicDBObject) n).get("lobulation")+""));
						nodule.setMalignancy(Integer.parseInt(((BasicDBObject) n).get("malignancy")+""));					
						nodule.setMargin(Integer.parseInt(((BasicDBObject) n).get("margin")+""));
						nodule.setSphericity(Integer.parseInt(((BasicDBObject) n).get("sphericity")+""));
						nodule.setSpiculation(Integer.parseInt(((BasicDBObject) n).get("spiculation")+""));
						nodule.setSubtlety(Integer.parseInt(((BasicDBObject) n).get("subtlety")+""));
						nodule.setTexture(Integer.parseInt(((BasicDBObject) n).get("texture")+""));				
					
						att = (BasicDBList) ((BasicDBObject) n).get("textureAttributes"); //TODO vetor do banco
						temp = new double[sizeArray];
						for (int i = 0; i < temp.length; i++) 
							temp[i] = Double.parseDouble(att.get(i)+"");
						//TODO temp2[0] = temp[0];
						//TODO temp2[1] = temp[2];
						//TODO nodule.setTextureAttributes(temp2);
						nodule.setAttributes(temp);
						
						rois = (BasicDBList) ((BasicDBObject) n).get("roi");
						originals = new String[rois.size()];
						nodules = new String[rois.size()];
						listPoints = new ArrayList<Points[]>();
						for (int i = 0; i < rois.size(); i++) {
							roi = (BasicDBObject) rois.get(i);						
							originals[i] = roi.getString("originalImage");
							nodules[i] = roi.getString("noduleImage");
							points = (BasicDBList) roi.get("edgeMap");
							p = new Points[points.size()];
							for (int j = 0; j < points.size(); j++) {
								point = (BasicDBObject) points.get(j);
								p[j] = new Points(Integer.parseInt(point.getString("xCoord")), Integer.parseInt(point.getString("yCoord")));
							}						
							listPoints.add(p);
						}
						nodule.setPoints(listPoints);
						nodule.setOriginalId(originals);
						nodule.setSegmentedId(nodules);
						
						vectorDB.add(nodule);
					}
				}
			}

			arrayDB = new double[vectorDB.size()*sizeArray];
			for (int i = 0; i < vectorDB.size(); i++) {
				temp = vectorDB.get(i).getAttributes();
				for (int j = 0; j < sizeArray; j++) {
					arrayDB[i*sizeArray+j] = temp[j];
				}				
			}
			numberNodules = vectorDB.size();		
	}

	private void getReferenceTextureAttributes() {
		CMTextureAttributes att = new CMTextureAttributes(imagesReference);
		double[] atts;
		arrayReference = new double[sizeArray];
		int p = 0;

		atts = att.get0to0DegreeFeaturesVector();
		for (double d : atts) {
			arrayReference[p] = d;
			p++;
		}

		atts = att.get0to135DegreeFeaturesVector();
		for (double d : atts) {
			arrayReference[p] = d;
			p++;
		}

		atts = att.get0to45DegreeFeaturesVector();
		for (double d : atts) {
			arrayReference[p] = d;
			p++;
		}

		atts = att.get0to90DegreeFeaturesVector();
		for (double d : atts) {
			arrayReference[p] = d;
			p++;
		}
	}
	
	 public static void main(String[] args) throws IOException {
			JFrame frame = new JFrame();
			frame.setSize(1920, 750);
			frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);		
	//		frame.add(new RetrievalPanel());
//			frame.setVisible(true);
		}	 
}
