/*
 * Copyright (c) 2015 Universidade Federal de Alagoas
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

package br.edu.ufal.cad.cbir.isa;



import br.edu.ufal.cad.mongodb.tags.Points;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SimilarNodule {
	
	private String idExam;
	private String path;	
	private String idNodule;
	private double[] attributes;
	private double distance;
	private int texture;
	private int malignancy;
	private int subtlety;
	private int calcification;
	private int internalStructure;
	private int lobulation;
	private int margin;
	private int sphericity;
	private int spiculation;
	private String[] originalId;
	private String[] segmentedId;	
	private BufferedImage[] originalImage;
	private BufferedImage[] noduleImage;
	private String[] filename;
	private ArrayList<Points[]> points;

	public SimilarNodule() {}

	@JsonCreator
	public SimilarNodule(String idExam,
						 String path,
						 String idNodule,
						 double[] attributes,
						 double distance,
						 int texture,
						 int malignancy,
						 int subtlety,
						 int calcification,
						 int internalStructure,
						 int lobulation,
						 int margin,
						 int sphericity,
						 int spiculation,
						 String[] originalId,
						 String[] segmentedId,
						 BufferedImage[] originalImage,
						 BufferedImage[] noduleImage,
						 String[] filename,
						 ArrayList<Points[]> points) {
		this.idExam = idExam;
		this.path = path;
		this.idNodule = idNodule;
		this.attributes = attributes;
		this.distance = distance;
		this.texture = texture;
		this.malignancy = malignancy;
		this.subtlety = subtlety;
		this.calcification = calcification;
		this.internalStructure = internalStructure;
		this.lobulation = lobulation;
		this.margin = margin;
		this.sphericity = sphericity;
		this.spiculation = spiculation;
		this.originalId = originalId;
		this.segmentedId = segmentedId;
		this.originalImage = originalImage;
		this.noduleImage = noduleImage;
		this.filename = filename;
		this.points = points;
	}

	public SimilarNodule(String idExam, int mal, double[] att, String path, String noduleId){
		this.idExam = idExam;
		malignancy = mal;
		attributes = att;
		this.path = path;
		this.idNodule = noduleId;
	}

	public SimilarNodule(int mal, double[] att){
		malignancy = mal;
		attributes = att;
	}

	public ArrayList<Points[]> getPoints() {
		return points;
	}
	public void setPoints(ArrayList<Points[]> points) {
		this.points = points;
	}
	public String[] getFilename() {
		return filename;
	}
	public void setFilename(String[] filename) {
		this.filename = filename;
	}
	public String[] getOriginalId() {
		return originalId;
	}
	public void setOriginalId(String[] originalId) {
		this.originalId = originalId;
	}
	public String[] getSegmentedId() {
		return segmentedId;
	}
	public void setSegmentedId(String[] segmentedId) {
		this.segmentedId = segmentedId;
	}
	public BufferedImage[] getOriginalImage() {
		return originalImage;
	}
	public void setOriginalImage(BufferedImage[] originalImage) {
		this.originalImage = originalImage;
	}
	public BufferedImage[] getNoduleImage() {
		return noduleImage;
	}
	public void setNoduleImage(BufferedImage[] noduleImage) {
		this.noduleImage = noduleImage;
	}
	public int getTexture() {
		return texture;
	}
	public void setTexture(int texture) {
		this.texture = texture;
	}
	public int getMalignancy() {
		return malignancy;
	}
	public void setMalignancy(int malignancy) {
		this.malignancy = malignancy;
	}
	public int getSubtlety() {
		return subtlety;
	}
	public void setSubtlety(int subtlety) {
		this.subtlety = subtlety;
	}
	public int getCalcification() {
		return calcification;
	}
	public void setCalcification(int calcification) {
		this.calcification = calcification;
	}
	public int getInternalStructure() {
		return internalStructure;
	}
	public void setInternalStructure(int internalStructure) {
		this.internalStructure = internalStructure;
	}
	public int getLobulation() {
		return lobulation;
	}
	public void setLobulation(int lobulation) {
		this.lobulation = lobulation;
	}
	public int getMargin() {
		return margin;
	}
	public void setMargin(int margin) {
		this.margin = margin;
	}
	public int getSphericity() {
		return sphericity;
	}
	public void setSphericity(int sphericity) {
		this.sphericity = sphericity;
	}
	public int getSpiculation() {
		return spiculation;
	}
	public void setSpiculation(int spiculation) {
		this.spiculation = spiculation;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getIdExam() {
		return idExam;
	}
	public void setIdExam(String idExam) {
		this.idExam = idExam;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getIdNodule() {
		return idNodule;
	}
	public void setIdNodule(String idNodule) {
		this.idNodule = idNodule;
	}
	public double[] getAttributes() {
		return attributes;
	}
	public void setAttributes(double[] attributes) {
		this.attributes = attributes;
	}
}
