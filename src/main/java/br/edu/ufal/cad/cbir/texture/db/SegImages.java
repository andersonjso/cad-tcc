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

public class SegImages {

	private int id;
	private String path;
	private double dist;
	private String[] caracteristicas;
	
	public SegImages(int id, String path) {
		
		this.id = id;
		this.path = path;
		
	}
	
	public void setCaracteristicas(String[] caracteristicas)
	{
		this.caracteristicas = caracteristicas;		
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setDist(double dist) {
		this.dist = dist;
	}

	public double getDist() {
		return dist;
	}
	
	public SegImages clone()
	{
		SegImages ret = new SegImages(0, new String(this.path));
		ret.setDist(this.dist);
		ret.setDist(this.dist);
		
		if(this.caracteristicas != null)
			ret.setCaracteristicas(this.caracteristicas.clone());
			
		return ret;
	}

	public String[] getCaracteristicas() {
		// TODO Auto-generated method stub
		return this.caracteristicas;
	}
	
	
}
