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


/**
 * @author oliveiramc
 * 
 */
@SuppressWarnings("serial")
public class DicomReadeIOException extends Exception {

	private String message;

	@SuppressWarnings("unused")
	private Throwable cause;

	/**
	 * @param message  An explicative message.
	 * @param cause    The exception cause
	 */
	public DicomReadeIOException(String message, Throwable cause) {

		super(message, cause);
		this.message = message;
		this.cause = cause;

	}
	
	public DicomReadeIOException(String message) {

		super(message);
		this.message = message;
	
	}

	public String getMessage() {
		return message + " " + getCause().getMessage();
	}

}