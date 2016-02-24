/**
 * 
 */
package br.edu.ufal.cad.images.general;

import javax.swing.*;

/**
 * @author oliveiramc
 *
 */
@SuppressWarnings("serial")
public class InterfaceException extends RuntimeException{
	
	public InterfaceException() {
		super();
	}
	
	public InterfaceException(String msg) {
		super(msg);
		JOptionPane.showMessageDialog(null, msg);
		
	}

}
