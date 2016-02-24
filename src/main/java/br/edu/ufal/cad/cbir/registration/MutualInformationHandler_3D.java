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

package br.edu.ufal.cad.cbir.registration;

import java.io.*;
import java.util.LinkedList;

public class MutualInformationHandler_3D {

	private String fixedImagePath = null;
	private String movingImagePath = null;
		
	public MutualInformationHandler_3D(String fixedImagePath, String movingImagePath)
	{
		this.fixedImagePath = fixedImagePath;
		this.movingImagePath = movingImagePath;
		this.register();
	}
	
	public String getFixedImagePath()
	{
		return this.fixedImagePath;
	}
	
	public String getMovingImagePath()
	{
		return this.movingImagePath;
	}
	
	private void register()
	{
		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("./3DImageRegistrationMI "+fixedImagePath+" "+movingImagePath+" jpg").getInputStream()));
			String aux = "";
			String resultado = "";
			
			aux = reader.readLine();
			
			while(aux != null)
			{
				resultado += aux + "\n";
				aux = reader.readLine();
			}
			
			reader.close();
			
			File exitName = new File(movingImagePath);
			BufferedWriter writer = new BufferedWriter(new FileWriter(exitName.getName()));
			writer.write(resultado);
			writer.close();
			
		} catch (IOException e) {
			System.err.println("Imagem não encontrada "+movingImagePath);
		}
	}
	
	
	
	public static String returnHandler(String result)
	{
		LinkedList<Float> list = new LinkedList<Float>();
		
		for (int i = 0; i < 200; i++) {
			
			int x, y;
			
			x = result.indexOf(i+"   ") + (i+"   ").length();
			y = result.indexOf("   [");
			
			String flo = result.substring(x, y);
			
			Float num = Float.parseFloat(flo);
			
			//System.out.println(i + " " + flo);
			
			result = result.replaceFirst("   ", "x");
			result = result.replaceFirst("   ", "x");
			
			list.add(num);
			
		}
		
		Float res = list.pop();
		
		while(!list.isEmpty())
		{
			Float a = list.pop();
			if(a < res)
				res = a;
		}
		
		return res.toString();
		
	}
	
}
