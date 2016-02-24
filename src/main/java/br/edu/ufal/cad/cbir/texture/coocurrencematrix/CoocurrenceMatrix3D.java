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

package br.edu.ufal.cad.cbir.texture.coocurrencematrix;




import br.edu.ufal.cad.images.general.Raster2;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;


public abstract class CoocurrenceMatrix3D {

	private Raster2 sourceImages;
	protected int[][] coocurrenceMatrix;
	private int[][][] grayMatrix;//three dimensional gray-scale matrix
	protected int width, height;//dimensions of the x and y axis of the medical image
	protected int stages;//z axis
	protected int limit = 256;//coocurrence matrix bounds limit
	protected int distance = 1;


	/**
	 * 
	 * @param sourceImage
	 * 
	 */
	public CoocurrenceMatrix3D(BufferedImage[] sourceImage){

		this.width = sourceImage[0].getWidth();
		this.height = sourceImage[0].getHeight();
		this.stages = sourceImage.length;

		this.coocurrenceMatrix = new int[this.limit][this.limit];
		this.grayMatrix = new int[this.width][this.height][this.stages];

		this.clearMatrix();
		
		//creates a 3D matrix who represents the 3D gray-scale medical image
		for(int s=0; s<this.stages; s++)//z-axis
		{
			Raster auxRaster = sourceImage[s].getRaster();
			this.sourceImages = new Raster2(auxRaster);

			for(int x=0; x<this.width; x++)
				for(int y=0; y<this.height; y++)
				{
					this.grayMatrix[x][y][s] = this.sourceImages.grey(x, y);
				}
		}

	}
	
	
	public void setDistance(int distance)
	{
		this.distance = distance;
	}
	
	/**
	 * Constrói a matriz de coocorrencia 
	 * a partir de um angulo pré-determinado
	 *
	 * \n number / angles
	 * \n 0 = (-, 0º)
	 * \n 1 = (0º, 45º)
	 * \n 2 = (0º, 90º)
	 * \n 3 = (0º, 135º)
	 * \n 4 = (45º, 45º)
	 * \n 5 = (45º, 90º)
	 * \n 6 = (45º, 135º)
	 * \n 7 = (90º, 45º)
	 * \n 8 = (90º, 90º)
	 * \n 9 = (90º, 135º)
	 * \n 10 = (135º, 45º)
	 * \n 11 = (135º, 90º)
	 * \n 12 = (135º, 135º)
	 
	 * 
	 * 
	 * @param angle
	 */
	public void setMatrix(int angle)
	{
		switch(angle)
		{
		case 0:
			buildMatrix(0, 0, this.distance);
			break;
		case 1:
			buildMatrix(this.distance, 0, this.distance);
			break;
		case 2:
			buildMatrix(this.distance, 0, 0);
			break;
		case 3:
			buildMatrix(this.distance, 0, -this.distance);
			break;
		case 4:
			buildMatrix(this.distance, this.distance, this.distance);
			break;
		case 5:
			buildMatrix(this.distance, this.distance, 0);
			break;
		case 6:
			buildMatrix(this.distance, this.distance, -this.distance);
			break;
		case 7:
			buildMatrix(0, this.distance, this.distance);
			break;
		case 8:
			buildMatrix(0, this.distance, 0);
			break;
		case 9:
			buildMatrix(0, this.distance, -this.distance);
			break;
		case 10:
			buildMatrix(-this.distance, this.distance, this.distance);
			break;
		case 11:
			buildMatrix(-this.distance, this.distance, 0);
			break;
		case 12:
			buildMatrix(-this.distance, this.distance, -this.distance);
			break;
			
			
			
		}
		
	}

	/**
	 * Este método constrói a matriz de coocorrencia 3D
	 * 
	 * @author Pedro
	 */
	private void buildMatrix(int i, int j, int k)
	{
		for(int x=0; x<this.width; x++)
			for(int y=0; y<this.height; y++)
				for(int z=0; z<this.stages; z++)
				{
					watchNeighborhood(x, y, z, i, j, k);
				}
	}

	/**
	 * Este método é responsável por preencher a matriz
	 * de coocorrencia com dados de um visinhança 
	 * pré-determinada.
	 * 
	 * @author Pedro
	 * @param x
	 * @param y
	 * @param z
	 * @param angleX
	 * @param angleY
	 * @param angleZ
	 */
	private void watchNeighborhood(int x, int y, int z, int angleX, int angleY, int angleZ)
	{


		if(x >= 0 && x<limit && y >= 0 && y<limit && z >= 0 && z<this.stages && x+angleX >= 0 && x+angleX<limit && y+angleY >= 0 && y+angleY<limit && z+angleZ >= 0 && z+angleZ<this.stages)
		{
			int i = (int) this.grayMatrix[x][y][z] / 16;

			int j = (int) this.grayMatrix[x+angleX][y+angleY][z+angleZ] / 16;
			
			if(i > 255)
				i /= 16;
		
			if(j > 255)
				j /= 16;
			
			this.coocurrenceMatrix[i][j]++;
			this.coocurrenceMatrix[j][i]++;
		}
	}

	/**
	 * Retorna a matriz de coocorrência
	 * 
	 * @return int[][][]
	 */
	public int[][] getCoocurrenceMatrix()
	{
		return coocurrenceMatrix;
	}


	private void clearMatrix()
	{
		for(int i=0; i<this.limit; i++)
			for(int j=0; j<this.limit; j++)
				this.coocurrenceMatrix[i][j] = 0;
	}






}
