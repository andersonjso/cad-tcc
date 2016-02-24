package br.edu.ufal.cad.mongodb.tags;

public class Points {
	
	private int xCoord;
	private int yCoord;

	public Points(int xCoord, int yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}
	
	public Points(){
		
	}

	public int getXCoord() {
		return xCoord;
	}

	public int getYCoord() {
		return yCoord;
	}

	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

}
