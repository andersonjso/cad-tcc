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
public class DicomExamInfo {
	
	public static final int EXAM_ID     = 0x00200011;
	public static final int EXAM_DATE   = 0x00080021;
	public static final int EXAM_MODALITY = 0x00080060;
	
	public static final String ATT_EXAM_ID   = "examID";
	public static final String ATT_EXAM_DATE = "examDate";
	public static final String ATT_MODALITY  = "examModality";
	
	@SuppressWarnings("unchecked")
	private static Map examtMap;

	@SuppressWarnings("unchecked")
	public static synchronized void examInfo(String attributes, String values) {

		if (examtMap == null) {
			examtMap = new HashMap();
		}

		examtMap.put(attributes, values);

	}

	public static Object getExamInfo(String name) {
		return examtMap.get(name);
	}

	@SuppressWarnings("unchecked")
	public static Map getExamInfoMap() {
		return examtMap;
	}

}
