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

package br.edu.ufal.cad.cbir.texture.coocurrencematrix;

import java.awt.image.BufferedImage;

/**
 * 
 * @author Marcelo Costa Oliveira 
 * @email oliveiramc@gmail.com
 * 
 */

public interface TextureMetric {
	
	/**
	 *
	 * @param ReferenceImage to get co-ocurrence matrix
	 */
	public void setReferenceImage(BufferedImage[] referenceImages) throws NullPointerException;
	/**
	 * @param targetImage
	 */
	public void setTargetImage(BufferedImage[] targetImages) throws NullPointerException;
	
	/**
	 * @return Measured Value between 2 Images.
	 */
	public double getMetric();
	

}
