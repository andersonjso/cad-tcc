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


import br.edu.ufal.cad.images.general.InterfaceException;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * 
 * Modified by Pedro Augusto Almeida Ayres
 * 
 * @author Marcelo Costa Oliveira
 * @email oliveiramc@gmail.com
 * 
 */

public class TextureMetricEuclideanDistance implements TextureMetric {
	
	private ArrayList<double[]> referenceAttributesList = null;
	private ArrayList<double[]> targetAttributesList = null;
	
	public TextureMetricEuclideanDistance() {
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see src.texture.TextureMetric#setReferenceImage(src.image.ImageData)
	 */
	public void setReferenceImage(BufferedImage[] referenceImages) {
		
		this.referenceAttributesList = new ArrayList<double[]>();
		
		CMTextureAttributes ref = null;
		try {
			ref = new CMTextureAttributes(referenceImages);
		} catch (NullPointerException e) {
			System.out.println("Error to read Files on Path, Is the Path right ??");
		}
		
		this.referenceAttributesList.add(ref.get0to0DegreeFeaturesVector());
		this.referenceAttributesList.add(ref.get0to45DegreeFeaturesVector());
		this.referenceAttributesList.add(ref.get0to90DegreeFeaturesVector());
		this.referenceAttributesList.add(ref.get0to135DegreeFeaturesVector());
		this.referenceAttributesList.add(ref.get45to45DegreeFeaturesVector());
		this.referenceAttributesList.add(ref.get45to90DegreeFeaturesVector());
		this.referenceAttributesList.add(ref.get45to135DegreeFeaturesVector());
		this.referenceAttributesList.add(ref.get90to45DegreeFeaturesVector());
		this.referenceAttributesList.add(ref.get90to90DegreeFeaturesVector());
		this.referenceAttributesList.add(ref.get90to135DegreeFeaturesVector());
		this.referenceAttributesList.add(ref.get135to45DegreeFeaturesVector());
		this.referenceAttributesList.add(ref.get135to90DegreeFeaturesVector());
		this.referenceAttributesList.add(ref.get135to135DegreeFeaturesVector());
				
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see src.texture.TextureMetric#setTargetImage(src.image.ImageData)
	 */
	public void setTargetImage(BufferedImage[] targetImages) {
		
		this.targetAttributesList = new ArrayList<double[]>();
		
		CMTextureAttributes ref = null;
		try {
			ref = new CMTextureAttributes(targetImages);
		} catch (NullPointerException e) {
			System.out.println("Error reading Files on Path, Is the Path right ??");
		}
	
	
		this.targetAttributesList.add(ref.get0to0DegreeFeaturesVector());
		this.targetAttributesList.add(ref.get0to45DegreeFeaturesVector());
		this.targetAttributesList.add(ref.get0to90DegreeFeaturesVector());
		this.targetAttributesList.add(ref.get0to135DegreeFeaturesVector());
		this.targetAttributesList.add(ref.get45to45DegreeFeaturesVector());
		this.targetAttributesList.add(ref.get45to90DegreeFeaturesVector());
		this.targetAttributesList.add(ref.get45to135DegreeFeaturesVector());
		this.targetAttributesList.add(ref.get90to45DegreeFeaturesVector());
		this.targetAttributesList.add(ref.get90to90DegreeFeaturesVector());
		this.targetAttributesList.add(ref.get90to135DegreeFeaturesVector());
		this.targetAttributesList.add(ref.get135to45DegreeFeaturesVector());
		this.targetAttributesList.add(ref.get135to90DegreeFeaturesVector());
		this.targetAttributesList.add(ref.get135to135DegreeFeaturesVector());
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see src.texture.TextureMetric#getMetric()
	 */
	@SuppressWarnings("unchecked")
	public double getMetric() {
		
		double dist = 0;
		
		if( (this.referenceAttributesList == null) || (this.targetAttributesList == null) )
			throw new InterfaceException("Imagem de Refer�ncia ou Alvo n�o Fornecidas");
		
		Iterator itReference = this.referenceAttributesList.iterator();
		Iterator itTarget    = this.targetAttributesList.iterator();
		
		while(itReference.hasNext()) {
			
			double[] referenceValues = (double[]) itReference.next();
			double[] targetValues    = (double[]) itTarget.next();
			
			 
				for(int x = 0; x < referenceValues.length; x++) {
					
					dist += ( (double)referenceValues[x] - (double)targetValues[x] ) * 
									( (double)referenceValues[x] - (double)targetValues[x] );
					
					//Distancia Manhatan
					//dist +=  ( (double)referenceValues[x] - (double)targetValues[x] );
					
				}
			
		}
		
		return Math.sqrt( (double) dist) / (double) 100.00000;
		
		//Distancia Manhatan
		//return Math.abs( (double) dist );
		
		
	}

}
