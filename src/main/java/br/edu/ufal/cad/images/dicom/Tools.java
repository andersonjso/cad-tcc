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

package br.edu.ufal.cad.images.dicom;

import java.awt.*;

/**
 * 
 * @author Marcelo Costa Oliveira 
 * @email oliveiramc@gmail.com
 * 
 */

/** This class contains static utility methods. */
 public class Tools {
    /** This array contains the 16 hex digits '0'-'F'. */
    public static final char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    
    /** Converts a Color to an 7 byte hex string starting with '#'. */
    public static String c2hex(Color c) {
        int i = c.getRGB();
        char[] buf7 = new char[7];
        buf7[0] = '#';
        for (int pos=6; pos>=1; pos--) {
            buf7[pos] = hexDigits[i&0xf];
            i >>>= 4;
        }
        return new String(buf7);
    }
        
    /** Converts a float to an 9 byte hex string starting with '#'. */
    public static String f2hex(float f) {
        int i = Float.floatToIntBits(f);
        char[] buf9 = new char[9];
        buf9[0] = '#';
        for (int pos=8; pos>=1; pos--) {
            buf9[pos] = hexDigits[i&0xf];
            i >>>= 4;
        }
        return new String(buf9);
    }
        
    public static double[] getMinMax(double[] a) {
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        double value;
        for (int i=0; i<a.length; i++) {
            value = a[i];
            if (value<min)
                min = value;
            if (value>max)
                max = value;
        }
        double[] minAndMax = new double[2];
        minAndMax[0] = min;
        minAndMax[1] = max;
        return minAndMax;
    }

    public static double[] getMinMax(float[] a) {
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        double value;
        for (int i=0; i<a.length; i++) {
            value = a[i];
            if (value<min)
                min = value;
            if (value>max)
                max = value;
        }
        double[] minAndMax = new double[2];
        minAndMax[0] = min;
        minAndMax[1] = max;
        return minAndMax;
    }
    
    /** Converts the float array 'a' to a double array. */
    public static double[] toDouble(float[] a) {
        int len = a.length;
        double[] d = new double[len];
        for (int i=0; i<len; i++)
            d[i] = a[i];
        return d;
    }
    
    /** Converts the double array 'a' to a float array. */
    public static float[] toFloat(double[] a) {
        int len = a.length;
        float[] f = new float[len];
        for (int i=0; i<len; i++)
            f[i] = (float)a[i];
        return f;
    }
    
    /** Converts carriage returns to line feeds. */
    public static String fixNewLines(String s) {
        char[] chars = s.toCharArray();
        for (int i=0; i<chars.length; i++)
            {if (chars[i]=='\r') chars[i] = '\n';}
        return new String(chars);
    }

    /**
    * Returns a new double initialized to the value represented by the 
    * specified <code>String</code>.
    *
    * @param      s   the string to be parsed.
    * @return     the double value represented by the string argument.
    * @exception  NumberFormatException  if the string does not contain a
    *             parsable double.
    */
    public static double parseDouble(String s) throws NumberFormatException {
        Double d = new Double(s);
        return(d.doubleValue());
    }

}