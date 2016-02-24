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

import java.util.HashMap;
import java.util.Map;

/**
 * @author oliveiramc
 * 
 */
public class DicomPatientInfo implements DicomInfoTags {

	public static final int PATIENT_NAME = 0x00100010;

	public static final int PATIENT_SEX = 0x00100040;

	public static final int PATIENT_BIRTH_DATE = 0x00100030;

	public static final String ATT_SEX = "sex";

	public static final String ATT_NAME = "name";

	public static final String ATT_BIRTH_DATE = "birthDate";

	@SuppressWarnings("unchecked")
	private static Map patientMap;
	
	@SuppressWarnings("unchecked")
	protected static synchronized void patientInfo(String attributes, String values) {

		if (patientMap == null) {
			patientMap = new HashMap();
		}

		patientMap.put(attributes, values);

	}

	protected static Object getPatientInfo(String name) {
		return patientMap.get(name);
	}

	@SuppressWarnings("unchecked")
	public static Map getPatientInfoMap() {
		return patientMap;
	}

}
