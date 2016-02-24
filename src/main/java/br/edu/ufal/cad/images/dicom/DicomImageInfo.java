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

import java.io.InputStream;

/**
 * 
 * @author Marcelo Costa Oliveira 
 * @email oliveiramc@gmail.com
 * 
 */

/** This class consists of public fields that describe an image file. */
public class DicomImageInfo implements DicomInfoTags{
	
	public static final int WINDOW_WIDTH = 0x00281051;

	public static final int REESCALE_INTERCEPT = 0x00281052;

	public static final int RED_PALETTE = 0x00281201;

	public static final int GREEN_PALETTE = 0x00281202;

	public static final int BLUE_PALETTE = 0x00281203;

	public static final int PIXEL_DATA = 0x7FE00010;

	public static final int PIXEL_REPRESENTATION = 0x00280103;

	public static final int TRANSFER_SYNTAX_UID = 0x00020010;

	public static final int SLICE_SPACING = 0x00180088;

	public static final int SAMPLES_PER_PIXEL = 0x00280002;

	public static final int PHOTOMETRIC_INTERPRETATION = 0x00280004;

	public static final int PLANAR_CONFIGURATION = 0x00280006;

	public static final int NUMBER_OF_FRAMES = 0x00280008;

	public static final int ROWS = 0x00280010;

	public static final int COLUMNS = 0x00280011;

	public static final int PIXEL_SPACING = 0x00280030;

	public static final int BITS_ALLOCATED = 0x00280100;

	public static final int WINDOW_CENTER = 0x00281050;
	
	public static final int SOP_UID = 0x00080018;
	
    /** 8-bit unsigned integer (0-255). */
    public static final int GRAY8 = 0;

    /** 16-bit signed integer (-32768-32767). Imported signed images
        are converted to unsigned by adding 32768. */
    public static final int GRAY16_SIGNED = 1;

    /** 16-bit unsigned integer (0-65535). */
    public static final int GRAY16_UNSIGNED = 2;

    /** 32-bit signed integer. Imported 32-bit integer images are
        converted to floating-point. */
    public static final int GRAY32_INT = 3;

    /** 32-bit floating-point. */
    public static final int GRAY32_FLOAT = 4;

    /** 8-bit unsigned integer with color lookup table. */
    public static final int COLOR8 = 5;

    /** 24-bit interleaved RGB. Import/export only. */
    public static final int RGB = 6;

    /** 24-bit planer RGB. Import only. */
    public static final int RGB_PLANAR = 7;

    /** 1-bit black and white. Import only. */
    public static final int BITMAP = 8;

    /** 32-bit interleaved ARGB. Import only. */
    public static final int ARGB = 9;

    /** 24-bit interleaved BGR. Import only. */
    public static final int BGR = 10;

    /** 32-bit unsigned integer. Imported 32-bit integer images are
        converted to floating-point. */
    public static final int GRAY32_UNSIGNED = 11;

    // File formats
    public static final int UNKNOWN = 0;
    public static final int RAW = 1;
    public static final int TIFF = 2;
    public static final int GIF_OR_JPG = 3;
    public static final int FITS = 4;
    public static final int BMP = 5;
    public static final int DICOM = 6;

    /* File format (TIFF, GIF_OR_JPG, BMP, etc.). Used by the File/Revert command */
    public static int fileFormat;

    /* File type (GRAY8, GRAY_16_UNSIGNED, RGB, etc.) */
    public static int fileType;

    public static String fileName;
    public static String directory;
    public static String url;
    public static int width;
    public static int height;
    public static int offset=0;
    public static int nImages;
    public static int gapBetweenImages;
    public static boolean whiteIsZero;
    public static boolean intelByteOrder;
    public static int lutSize;
    public static byte[] reds;
    public static byte[] greens;
    public static byte[] blues;
    public static Object pixels;
    public static String info;
    public static InputStream inputStream;

    public static double pixelWidth=1.0;
    public static double pixelHeight=1.0;
    public static double pixelDepth=1.0;
    public static String unit;
    public static int calibrationFunction;
    public static double[] coefficients;
    public static String valueUnit;
    public static double frameInterval;
    public static String description;
    
    
    /** Returns the number of bytes used per pixel. */
    public static int getBytesPerPixel() {
        switch (fileType) {
            case GRAY8: case COLOR8: case BITMAP: return 1;
            case GRAY16_SIGNED: case GRAY16_UNSIGNED: return 2;
            case GRAY32_INT: case GRAY32_UNSIGNED: case GRAY32_FLOAT: case ARGB: return 4;
            case RGB: case RGB_PLANAR: case BGR: return 3;
            default: return 0;
        }
    }
 
    @SuppressWarnings("unused")
	private static String getType() {
        switch (fileType) {
            case GRAY8: return "byte";
            case GRAY16_SIGNED: return "short";
            case GRAY16_UNSIGNED: return "ushort";
            case GRAY32_INT: return "int";
            case GRAY32_UNSIGNED: return "uint";
            case GRAY32_FLOAT: return "float";
            case COLOR8: return "byte+lut";
            case RGB: return "RGB";
            case RGB_PLANAR: return "RGB(p)";
            case BITMAP: return "bitmap";
            case ARGB: return "ARGB";
            case BGR: return "BGR";
            default: return "";
        }
    }

}