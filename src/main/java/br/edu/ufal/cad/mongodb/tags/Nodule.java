package br.edu.ufal.cad.mongodb.tags;

import java.util.List;


public interface Nodule {
	
	String getNoduleID();	
	List<Roi> getRois();

}
