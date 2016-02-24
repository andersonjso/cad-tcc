/*
 * Copyright (c) 2002-2005 Universidade Federal de Campina Grande
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

package br.edu.ufal.cad.images.dicom;

import java.io.IOException;


/**
 * 
 * @author Marcelo Costa Oliveira
 * @email oliveiramc@gmail.com
 * 
 */


public class DicomImageFile extends AbstractDicomFile implements DicomParams {

	private final String imagePath;
	
	public static String ATT_PATIENT_NAME = "patientName";
	

	/**
	 * @param path
	 * @throws DicomReadeIOException 
	 * @throws DicomReadeIOException 
	 * @throws IOException 
	 * @throws NullPointerException 
	 * @throws IndexOutOfBoundsException 
	 * @throws IOException 
	 * @throws NullPointerException 
	 * @throws IndexOutOfBoundsException 
	 * @throws IOException 
	 * @throws IndexOutOfBoundsException 
	 */
	public DicomImageFile(String path) throws DicomReadeIOException{
		super(path);
		this.imagePath = path;
	}

	public String getImagePath() {
		return this.imagePath;
	}

	@SuppressWarnings("static-access")
	public int getRows() {
		return super.localFileInfo.height;
	}

	@SuppressWarnings("static-access")
	public int getColumns() {
		
		return super.localFileInfo.width;
	}
	
	/*private void PatientData() {
		
		Hashtable hPatientData = new Hashtable();
		hPatientData.put(ATT_PATIENT_NAME, super.localFileInfo.patientName);
		
	}*/
	
	
	
	@SuppressWarnings("static-access")
	public int getValueBitsAllocated() {
		@SuppressWarnings("unused")
		int a = super.localFileInfo.fileType;

		switch (super.localFileInfo.getBytesPerPixel()) {

		case 1:
			return 8;
		case 2:
			return 12;
		case 4:
			return 32;
		case 3:
			return 24;
		default:
			return 0;
		}

	}

	public short[] getImageData() {
		try {
			return super.getImageShort();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

}
