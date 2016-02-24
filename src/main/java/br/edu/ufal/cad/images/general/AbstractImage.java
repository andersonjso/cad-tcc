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

package br.edu.ufal.cad.images.general;



import br.edu.ufal.cad.images.dicom.DicomParams;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;


/**
 * 
 * @author Marcelo Costa Oliveira 
 * @email oliveiramc@gmail.com
 * 
 */


public class AbstractImage {
	
	private int rows          = 0;
	private int columns       = 0;
	protected int bitsAllocated = 0;
	private short[] imageData = null;
	
	private BufferedImage image = null;
	
	/**
	 * 
	 */
	public AbstractImage( DicomParams dicomReader ){
			
			
		this.rows          = dicomReader.getRows();
		this.columns       = dicomReader.getColumns();
		this.bitsAllocated = dicomReader.getValueBitsAllocated();
		this.imageData     = dicomReader.getImageData();
		
		this.executeImageData();
		
				
	}
	
	private void executeImageData(){
		
		final int pixelStride = 1;
        final int scanlineStride = this.columns;
        final int[] bandOffsets = {0};
		
		
        DataBufferUShort short_db = new DataBufferUShort( (short[])imageData, (this.rows*this.columns) );
				
		WritableRaster wr = Raster.createInterleavedRaster(short_db,this.rows,
				this.columns, scanlineStride, pixelStride, bandOffsets, null);
		
		
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		int [] bits = {this.bitsAllocated};
		
			
		ComponentColorModel cm = new ComponentColorModel(cs, bits,
				false, false,
				Transparency.OPAQUE,
				DataBuffer.TYPE_USHORT);
		
		
	    this.image = new BufferedImage(cm, wr,
				cm.isAlphaPremultiplied(), null);
	    
	}
			
	public BufferedImage getBufferImage() {
		// TODO Auto-generated method stub
		return this.image;
	}
	
		

}
