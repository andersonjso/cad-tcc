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

import java.awt.image.BufferedImage;

/**
 * 
 * @author Pedro Augusto Almeida Ayres
 * @email pedro.3a@gmail.com
 * 
 */
public class CMTextureAttributes extends CoocurrenceMatrix3D {

	//simple attributes
	private double energy = 0.00000;
	private double entropy = 0.00000;
	private double inertia = 0.00000;
	private double homogeneity = 0.00000;
	private double inverseDifferenceMoment = 0.00000;	

	private double rowSum[];
	private double columnSum[];
	private double mtxRownMean = 0.0;
	private double mtxColumnMean = 0.0;
	private double mtxRowSigmaSquare = 0, mtxColumnSigmaSquare = 0;

	//complex attributes
	private double correlation = 0.00000;
	private double shade = 0.00000;
	private double promenance = 0.00000;
	private double variance = 0.00000;

	public CMTextureAttributes(BufferedImage[] sourceImage) {

		super(sourceImage);

		this.rowSum = new double[super.width];
		this.columnSum = new double[super.height];
				
		setSimpleAttributes();
		usefulFeatures();
		complexAttributes();

	}




	private void setSimpleAttributes() {

		for (int i = 0; i < super.limit; i++) 
		{
			for (int j = 0; j < super.limit; j++)
			{

					this.energy += super.coocurrenceMatrix[i][j] * super.coocurrenceMatrix[i][j];

					// Entropy
					if (super.coocurrenceMatrix[i][j] > 0)
						this.entropy -= (double) super.coocurrenceMatrix[i][j]
						              * Math.log((double) super.coocurrenceMatrix[i][j]);

					this.inertia += (double) (i - j) * (i - j)
					* (double) super.coocurrenceMatrix[i][j];

					this.homogeneity += (double) super.coocurrenceMatrix[i][j]
					                                                        / (1.0 + module(i, j));

					if(i != j)
					this.inverseDifferenceMoment += (1.0 / (1.0 + elevated(module(i, j), this.stages)))
					* (double) super.coocurrenceMatrix[i][j];

					this.rowSum[i] += (double) super.coocurrenceMatrix[i][j];
					this.columnSum[j] += (double) super.coocurrenceMatrix[i][j];
					//this.zaxisSum[k] += (double) super.coocurrenceMatrix[i][j];

					this.mtxRownMean += (double) super.coocurrenceMatrix[i][j] * (double) i;
					this.mtxColumnMean += (double) super.coocurrenceMatrix[i][j] * (double) j;


				
			}
		}


	}


	private int module(int i, int j)
	{
		int result =  i - j;
		if(result >= 0)
			return result;
		else return -result;
	}
	
	private int elevated(int num, int pot)
	{
		int result = num;
		
		for(int i=0; i<(pot-1); i++)
		{
			result *= num;
		}
		
		return result;
	}
	
	/**
	 * @return Returns the energy.
	 */
	public double getEnergy() {
		return (double) this.energy;
	}

	/**
	 * @return Returns the correlation.
	 */
	public double getCorrelation() {

		return (double) correlation;
	}

	
	/**
	 * @return Returns the entropy.
	 */
	public double getEntropy() {
		return (double) this.entropy;
	}

	/**
	 * @return Returns the homogeneity.
	 */
	public double getHomogeneity() {
		return (double) this.homogeneity;
	}

	/**
	 * @return Returns the inertia.
	 */
	public double getInertia() {
		return (double) this.inertia;
	}

	/**
	 * @return Returns the inverseDifferenceMoment.
	 */
	public double getInverseDifferenceMoment() {
		return (double) this.inverseDifferenceMoment;
	}


	private void usefulFeatures() {
		double rowMean = 0, columsMean = 0;
		double rowSigmaSquare = 0, columnSigmaSquare = 0;

		for (int i = 0; i < super.limit; i++) {

			rowMean += this.rowSum[i];
			columsMean += this.columnSum[i];

			this.mtxRowSigmaSquare += (double) (i - this.mtxRownMean)
			* (double) (i - this.mtxRownMean) * this.rowSum[i];
			
			this.mtxColumnSigmaSquare += (double) (i - this.mtxColumnMean)
			* (double) (i - this.mtxColumnMean) * this.columnSum[i];

		}

		rowMean /= super.limit;
		columsMean /= super.limit;

		for (int i = 0; i < super.limit; i++) {

			rowSigmaSquare += (rowSum[i] - rowMean) * (rowSum[i] - rowMean);
			columnSigmaSquare += (columnSum[i] - columsMean)
			* (columnSum[i] - columsMean);

		}

		rowSigmaSquare /= (super.limit - 1.);
		columnSigmaSquare /= (super.limit - 1.);

	}


	/**
	 * Features that use the usefulFeatures() Function
	 */
	private void complexAttributes() {


		for (int i = 0; i < super.limit; i++) {

			for (int j = 0; j < super.limit; j++) {

				this.correlation += ((double) i - this.mtxRownMean)	
				* ((double) j - this.mtxColumnMean) 
				* ((double) super.coocurrenceMatrix[i][j]);

				double deltaShade = (double) (i + j) - this.mtxRownMean
				- this.mtxColumnMean;
				
				this.shade += (double) (deltaShade * deltaShade * deltaShade)
				* (double) super.coocurrenceMatrix[i][j];
				
				this.promenance += (double) (deltaShade * deltaShade
						* deltaShade * deltaShade)
						* (double) super.coocurrenceMatrix[i][j];
				
				this.variance += (double) (i - this.mtxRownMean)
				* (double) (i - this.mtxRownMean)
				* (double) super.coocurrenceMatrix[i][j];

			}

		}

		this.correlation /= this.stages;
		this.correlation /= Math.sqrt(this.mtxRowSigmaSquare
				* this.mtxColumnSigmaSquare);
		
		if(Double.isNaN(this.correlation))
			this.correlation = (double) 0;
		
		this.shade /= this.stages;
		this.promenance /= this.stages;
		this.variance /= this.stages;
	}


	/**
	 * @return Returns the promenance.
	 */
	public double getPromenance() {
		return (double) promenance;
	}

	/**
	 * @return Returns the shade.
	 */
	public double getShade() {
		return (double) shade;
	}

	/**
	 * @return Returns the variance.
	 */
	public double getVariance() {
		return (double) variance;
	}


	private double[] getFeaturesVector() {
		double[] featuresVector = { this.getEnergy(), this.getEntropy(),
				this.getInertia(), this.getHomogeneity(),
				this.getCorrelation(), this.getShade(), this.getPromenance(),
				this.getVariance(), this.getInverseDifferenceMoment() };
		
		return featuresVector;
	}
	
	/**
	 * @return Get the Features Vector at 0 Degree. Note 0 is simetry to 180 
	 * @throws IllegalArgumentException
	 */
	public double[] get0to0DegreeFeaturesVector() throws IllegalArgumentException {
		
		super.setMatrix(0);
		
		this.setSimpleAttributes();
		this.usefulFeatures();
		this.complexAttributes();

		return this.getFeaturesVector();

	}
	
	
	/**
	 * @return Get the Features Vector at (0º, 45º) Degree. Note 45 is simetry to 225  
	 * @throws IllegalArgumentException
	 */
	public double[] get0to45DegreeFeaturesVector() throws IllegalArgumentException {
		
		super.setMatrix(1);
		
		this.setSimpleAttributes();
		this.usefulFeatures();
		this.complexAttributes();

		return this.getFeaturesVector();

	}
	
	
	/**
	 * @return Get the Features Vector  at (0º, 90º) Degree. Note 90 is simetry to 270  
	 * @throws IllegalArgumentException
	 */
	public double[] get0to90DegreeFeaturesVector() throws IllegalArgumentException {
		
		super.setMatrix(2);
		
		this.setSimpleAttributes();
		this.usefulFeatures();
		this.complexAttributes();

		return this.getFeaturesVector();

	}
	
	
	/**
	 * @return Get the Features Vector at (0º, 135º) Degree. Note 90 is simetry to 315
	 * @throws IllegalArgumentException
	 */
	public double[] get0to135DegreeFeaturesVector() throws IllegalArgumentException {
		
		super.setMatrix(3);
		
		this.setSimpleAttributes();
		this.usefulFeatures();
		this.complexAttributes();

		return this.getFeaturesVector();
		
	}
	
	/**
	 * @return Get the Features Vector at (45º, 45º) Degree. Note 45 is simetry to 225  
	 * @throws IllegalArgumentException
	 */
	public double[] get45to45DegreeFeaturesVector() throws IllegalArgumentException {
		
		super.setMatrix(4);
		
		this.setSimpleAttributes();
		this.usefulFeatures();
		this.complexAttributes();

		return this.getFeaturesVector();

	}
	
	
	/**
	 * @return Get the Features Vector  at (45º, 90º) Degree. Note 90 is simetry to 270  
	 * @throws IllegalArgumentException
	 */
	public double[] get45to90DegreeFeaturesVector() throws IllegalArgumentException {
		
		super.setMatrix(5);
		
		this.setSimpleAttributes();
		this.usefulFeatures();
		this.complexAttributes();

		return this.getFeaturesVector();

	}
	
	
	/**
	 * @return Get the Features Vector at (45º, 135º) Degree. Note 90 is simetry to 315
	 * @throws IllegalArgumentException
	 */
	public double[] get45to135DegreeFeaturesVector() throws IllegalArgumentException {
		
		super.setMatrix(6);
		
		this.setSimpleAttributes();
		this.usefulFeatures();
		this.complexAttributes();

		return this.getFeaturesVector();
		
	}
	
	/**
	 * @return Get the Features Vector at (90º, 45º) Degree. Note 45 is simetry to 225  
	 * @throws IllegalArgumentException
	 */
	public double[] get90to45DegreeFeaturesVector() throws IllegalArgumentException {
		
		super.setMatrix(7);
		
		this.setSimpleAttributes();
		this.usefulFeatures();
		this.complexAttributes();

		return this.getFeaturesVector();

	}
	
	
	/**
	 * @return Get the Features Vector  at (90º, 90º) Degree. Note 90 is simetry to 270  
	 * @throws IllegalArgumentException
	 */
	public double[] get90to90DegreeFeaturesVector() throws IllegalArgumentException {
		
		super.setMatrix(8);
		
		this.setSimpleAttributes();
		this.usefulFeatures();
		this.complexAttributes();

		return this.getFeaturesVector();

	}
	
	
	/**
	 * @return Get the Features Vector at (90º, 135º) Degree. Note 90 is simetry to 315
	 * @throws IllegalArgumentException
	 */
	public double[] get90to135DegreeFeaturesVector() throws IllegalArgumentException {
		
		super.setMatrix(9);
		
		this.setSimpleAttributes();
		this.usefulFeatures();
		this.complexAttributes();

		return this.getFeaturesVector();
		
	}
	
	
	/**
	 * @return Get the Features Vector at (135º, 45º) Degree. Note 45 is simetry to 225  
	 * @throws IllegalArgumentException
	 */
	public double[] get135to45DegreeFeaturesVector() throws IllegalArgumentException {
		
		super.setMatrix(10);
		
		this.setSimpleAttributes();
		this.usefulFeatures();
		this.complexAttributes();

		return this.getFeaturesVector();

	}
	
	
	/**
	 * @return Get the Features Vector  at (135º, 90º) Degree. Note 90 is simetry to 270  
	 * @throws IllegalArgumentException
	 */
	public double[] get135to90DegreeFeaturesVector() throws IllegalArgumentException {
		
		super.setMatrix(11);
		
		this.setSimpleAttributes();
		this.usefulFeatures();
		this.complexAttributes();

		return this.getFeaturesVector();

	}
	
	
	/**
	 * @return Get the Features Vector at (135º, 135º) Degree. Note 90 is simetry to 315
	 * @throws IllegalArgumentException
	 */
	public double[] get135to135DegreeFeaturesVector() throws IllegalArgumentException {
		
		super.setMatrix(12);
		
		this.setSimpleAttributes();
		this.usefulFeatures();
		this.complexAttributes();

		return this.getFeaturesVector();
		
	}
	
	
}
