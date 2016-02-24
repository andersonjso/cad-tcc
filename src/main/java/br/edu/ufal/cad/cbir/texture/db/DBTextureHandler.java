/*
 * Copyright (c) 2002-2005 Universidade Federal Alagoas - Campus Arapiraca
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

package br.edu.ufal.cad.cbir.texture.db;

import br.edu.ufal.cad.cbir.texture.coocurrencematrix.TextureMetricEuclideanDistance;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class DBTextureHandler {

	private double euclideanDistance;
	private String path;
	private int id;
	private BufferedImage[] ref;
	private BufferedImage[] target;
	
	
	
	public DBTextureHandler(String path, BufferedImage[] ref) 
	{
		this.path = path;
		this.setRef(ref);
				
		buildImages();
		
		TextureMetricEuclideanDistance dist = new TextureMetricEuclideanDistance();
		
		dist.setReferenceImage(ref);
		dist.setTargetImage(target);
		
		this.euclideanDistance = dist.getMetric();
		
	}
	
	private void buildImages()
	{
		File dir = new File(path);
		
		if(dir.isDirectory())
		{
			String[] imgs = dir.list();
			
			target = new BufferedImage[imgs.length];
			
			for(int i=0; i<imgs.length; i++)
			{
				try {
					target[i] = ImageIO.read(new File(dir.getAbsolutePath()+"/"+imgs[i]));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}		
	}
	
	public void setEuclideanDistance(int ed)
	{
		this.euclideanDistance = ed;
	}
	
	public double getEuclideanDistance()
	{
		return this.euclideanDistance;
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
	public String getPath()
	{
		return this.path;
	}
		
	public void setID(int id)
	{
		this.id = id;
	}

	public int getID()
	{
		return this.id;
	}

	public void setRef(BufferedImage[] ref) {
		this.ref = ref;
	}

	public BufferedImage[] getRef() {
		return ref;
	}
	
	
}
