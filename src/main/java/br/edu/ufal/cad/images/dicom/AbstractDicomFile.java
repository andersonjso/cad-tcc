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

import javax.swing.*;
import java.io.*;
import java.util.Properties;


/**
 * 
 * @author Marcelo Costa Oliveira
 * @email oliveiramc@gmail.com
 * 
 */

public class AbstractDicomFile {

	private static final int AE = 0x4145, AS = 0x4153, AT = 0x4154,
			CS = 0x4353, DA = 0x4441, DS = 0x4453, DT = 0x4454, FD = 0x4644,
			FL = 0x464C, IS = 0x4953, LO = 0x4C4F, LT = 0x4C54, PN = 0x504E,
			SH = 0x5348, SL = 0x534C, SS = 0x5353, ST = 0x5354, TM = 0x544D,
			UI = 0x5549, UL = 0x554C, US = 0x5553, UT = 0x5554, OB = 0x4F42,
			OW = 0x4F57, SQ = 0x5351, UN = 0x554E, QQ = 0x3F3F;

	private static Properties dictionary;

	private String directory, fileName;

	private static final int ID_OFFSET = 128; // location of "DICM"

	private static final String DICM = "DICM";

	private BufferedInputStream f;

	private int location = 0;

	private boolean littleEndian = true;

	private int elementLength;

	private int vr; // Value Representation

	private static final int IMPLICIT_VR = 0x2D2D; // '--'

	@SuppressWarnings("unused")
	private byte[] vrLetters = new byte[2];

	private int previousGroup;

	private StringBuffer dicomInfo = new StringBuffer(1000);

	private boolean dicmFound; // "DICM" found at offset 128

	private boolean oddLocations; // one or more tags at odd locations

	private boolean bigEndianTransferSyntax = false;

	public double windowCenter = -1, windowWidth = -1;

	public double ReescaleIntercept = 0;

	public int TargetWidth, TargetHeight;

	public int CenterLeft, CenterTop;

	public String photoInterpretation = "";

	public DicomImageInfo localFileInfo;

	public int[] rawdata;

	public short[] rawdataShort;

	public int[] viewportdata;

	// public AbstractDicomFile(String directory, String fileName) throws
	// IOException {

	@SuppressWarnings("static-access")
	public AbstractDicomFile(String path) throws DicomReadeIOException {

		int width = 0;
		int height = 0;

		if (dictionary == null) {
			DicomDictionary d = new DicomDictionary();
			dictionary = d.getDictionary();
		}

		try {
			try {

				this.getFileInfo(path);
				width = DicomImageInfo.width;
				height = DicomImageInfo.height;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new IOException();
			}
		} catch (IOException e1) {
			throw new DicomReadeIOException("Erro ao Ler Arquivo Dicom !", e1);
		}

		int length = width * height;
		rawdata = new int[length];

		CenterLeft = width / 2;
		CenterTop = height / 2;

		BufferedInputStream f1;
		try {
			f1 = new BufferedInputStream(new FileInputStream(path));
		} catch (FileNotFoundException e1) {
			throw new DicomReadeIOException("Arquivo no Encontrado !", e1);
		}

		DataInputStream f = new DataInputStream(f1);

		int bInvert = 0;
		if (photoInterpretation.trim().compareToIgnoreCase("MONOCHROME1") == 0) {
			bInvert = 1;
			windowCenter = -2048;
			windowWidth = 4096;
		}

		try {
			f.skip(localFileInfo.offset);
		} catch (IOException e1) {
			throw new DicomReadeIOException("Erro ao Ler Arquivo Dicom !", e1);
		}

		int pos = 0;
		rawdataShort = new short[length];
		while (pos < length) {

			try {
				rawdataShort[pos] = (short) ((f.readUnsignedByte()) + ((f
						.readUnsignedByte()) << 8));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"Arquivo nao pode ser Lido !!!");
			}

			if (bInvert == 1) {
				// rawdata[pos] = ~rawdata[pos];
				rawdataShort[pos] = (short) ~rawdataShort[pos];

			}

			pos++;

		}

	}

	String getString(int length) throws IOException {
		byte[] buf = new byte[length];
		int pos = 0;
		while (pos < length) {
			int count = f.read(buf, pos, length - pos);
			pos += count;
		}
		location += length;
		return new String(buf);
	}

	public short[] getImageShort() throws IOException {

		return rawdataShort;

	}

	public int getByte() throws IOException {
		int b = f.read();
		if (b == -1)
			throw new IOException("unexpected EOF");
		++location;
		return b;
	}

	public int getShort() throws IOException {
		int b0 = getByte();
		int b1 = getByte();
		if (littleEndian)
			return ((b1 << 8) + b0);
		else
			return ((b0 << 8) + b1);
	}

	final int getInt() throws IOException {
		int b0 = getByte();
		int b1 = getByte();
		int b2 = getByte();
		int b3 = getByte();
		if (littleEndian)
			return ((b3 << 24) + (b2 << 16) + (b1 << 8) + b0);
		else
			return ((b0 << 24) + (b1 << 16) + (b2 << 8) + b3);
	}

	byte[] getLut(int length) throws IOException {
		if ((length & 1) != 0) { // odd
			@SuppressWarnings("unused")
			String dummy = getString(length);
			return null;
		}
		length /= 2;
		byte[] lut = new byte[length];
		for (int i = 0; i < length; i++)
			lut[i] = (byte) (getShort() >>> 8);
		return lut;
	}

	int getLength() throws IOException {
		int b0 = getByte();
		int b1 = getByte();
		int b2 = getByte();
		int b3 = getByte();

		vr = (b0 << 8) + b1;

		switch (vr) {
		case OB:
		case OW:
		case SQ:
		case UN:
			// Explicit VR with 32-bit length if other two bytes are zero
			if ((b2 == 0) || (b3 == 0))
				return getInt();
			// Implicit VR with 32-bit length
			vr = IMPLICIT_VR;
			if (littleEndian)
				return ((b3 << 24) + (b2 << 16) + (b1 << 8) + b0);
			else
				return ((b0 << 24) + (b1 << 16) + (b2 << 8) + b3);
		case AE:
		case AS:
		case AT:
		case CS:
		case DA:
		case DS:
		case DT:
		case FD:
		case FL:
		case IS:
		case LO:
		case LT:
		case PN:
		case SH:
		case SL:
		case SS:
		case ST:
		case TM:
		case UI:
		case UL:
		case US:
		case UT:
		case QQ:
			// Explicit vr with 16-bit length
			if (littleEndian)
				return ((b3 << 8) + b2);
			else
				return ((b2 << 8) + b3);
		default:
			// Implicit VR with 32-bit length...
			vr = IMPLICIT_VR;
			if (littleEndian)
				return ((b3 << 24) + (b2 << 16) + (b1 << 8) + b0);
			else
				return ((b0 << 24) + (b1 << 16) + (b2 << 8) + b3);
		}
	}

	int getNextTag() throws IOException {
		int groupWord = getShort();
		if (groupWord == 0x0800 && bigEndianTransferSyntax) {
			littleEndian = false;
			groupWord = 0x0008;
		}
		int elementWord = getShort();
		int tag = groupWord << 16 | elementWord;
		elementLength = getLength();

		// hack needed to read some GE files
		// The element length must be even!
		if (elementLength == 13 && !oddLocations)
			elementLength = 10;

		// "Undefined" element length.
		// This is a sort of bracket that encloses a sequence of elements.
		if (elementLength == -1)
			elementLength = 0;
		return tag;
	}

	private void readDicom(String buffer) throws Exception {
		long skipCount;

		try {
			f = new BufferedInputStream(new FileInputStream(buffer));
		} catch (Exception e) {
			System.err.println("File not Found or can't be read");
		}

		skipCount = (long) ID_OFFSET;

		while (skipCount > 0)
			skipCount -= f.skip(skipCount);
		location += ID_OFFSET;

		if (!getString(4).equals(DICM)) {
			f.close();

			try {

				f = new BufferedInputStream(new FileInputStream(buffer));

			} catch (Exception e) {

				System.err.println("ERRO no DICM.....");
			}

			location = 0;

		} else {
			dicmFound = true;

		}

	}

	private void createImageInfo() {

		DicomImageInfo.fileFormat = DicomImageInfo.RAW;
		DicomImageInfo.fileName = fileName;
		DicomImageInfo.directory = directory;
		DicomImageInfo.width = 0;
		DicomImageInfo.height = 0;
		DicomImageInfo.offset = 0;
		DicomImageInfo.intelByteOrder = true;
		DicomImageInfo.fileType = DicomImageInfo.GRAY16_UNSIGNED;
		DicomImageInfo.fileFormat = DicomImageInfo.DICOM;

	}

	public void getFileInfo(String buffer) throws Exception {

		this.readDicom(buffer);
		this.createImageInfo();

		boolean decodingTags = true;

		while (decodingTags) {
			int tag = getNextTag();
			if ((location & 1) != 0) // DICOM tags must be at even locations
				oddLocations = true;

			decodingTags = readImageTags(tag);

		}

		f.close();

	}

	private boolean readImageTags(int tag) throws IOException {

		int bitsAllocated = 16;
		boolean decodingTags = true;
		int samplesPerPixel = 1;
		int planarConfiguration = 0;

		@SuppressWarnings("unused")
		boolean inSequence = true;
		String s;

		switch (tag) {
		case DicomImageInfo.TRANSFER_SYNTAX_UID:
			s = getString(elementLength);
			addInfo(tag, s);
			if (s.indexOf("1.2.4") > -1 || s.indexOf("1.2.5") > -1) {
				f.close();
				String msg = "Cannot open compressed DICOM images.\n \n";
				msg += "Transfer Syntax UID = " + s;
				throw new IOException(msg);
			}
			if (s.indexOf("1.2.840.10008.1.2.2") >= 0)
				bigEndianTransferSyntax = true;
			break;
		case DicomImageInfo.NUMBER_OF_FRAMES:
			s = getString(elementLength);
			addInfo(tag, s);
			double frames = s2d(s);
			if (frames > 1.0)
				DicomImageInfo.nImages = (int) frames;
			break;
		case DicomImageInfo.SAMPLES_PER_PIXEL:
			samplesPerPixel = getShort();
			addInfo(tag, samplesPerPixel);
			break;
		case DicomImageInfo.PHOTOMETRIC_INTERPRETATION:
			photoInterpretation = getString(elementLength);
			addInfo(tag, photoInterpretation);
			break;
		case DicomImageInfo.PLANAR_CONFIGURATION:
			planarConfiguration = getShort();
			addInfo(tag, planarConfiguration);
			break;
		case DicomImageInfo.ROWS:
			DicomImageInfo.height = getShort();
			addInfo(tag, DicomImageInfo.height);
			break;
		case DicomImageInfo.COLUMNS:
			DicomImageInfo.width = getShort();
			addInfo(tag, DicomImageInfo.width);
			break;
		case DicomImageInfo.PIXEL_SPACING:
			String scale = getString(elementLength);
			getSpatialScale(scale);
			addInfo(tag, scale);
			break;
		case DicomImageInfo.SLICE_SPACING:
			String spacing = getString(elementLength);
			DicomImageInfo.pixelDepth = s2d(spacing);
			addInfo(tag, spacing);
			break;
		case DicomImageInfo.BITS_ALLOCATED:
			bitsAllocated = getShort();
			if (bitsAllocated == 8)
				DicomImageInfo.fileType = DicomImageInfo.GRAY8;
			addInfo(tag, bitsAllocated);
			break;
		case DicomImageInfo.PIXEL_REPRESENTATION:
			int pixelRepresentation = getShort();
			if (pixelRepresentation == 1)
				DicomImageInfo.fileType = DicomImageInfo.GRAY16_SIGNED;
			addInfo(tag, pixelRepresentation);
			break;
		case DicomImageInfo.WINDOW_CENTER:
			String center = getString(elementLength);
			if (windowCenter == -1) {
				windowCenter = s2d(center);
			}
			addInfo(tag, center);
			break;
		case DicomImageInfo.REESCALE_INTERCEPT:
			String reescale = getString(elementLength);
			ReescaleIntercept = s2d(reescale);
			addInfo(tag, reescale);
			break;
		case DicomImageInfo.WINDOW_WIDTH:
			String width = getString(elementLength);
			if (windowWidth == -1) {
				windowWidth = s2d(width);
			}
			addInfo(tag, width);
			break;

		case DicomImageInfo.RED_PALETTE:
			DicomImageInfo.reds = getLut(elementLength);
			addInfo(tag, elementLength / 2);
			break;
		case DicomImageInfo.GREEN_PALETTE:
			DicomImageInfo.greens = getLut(elementLength);
			addInfo(tag, elementLength / 2);
			break;
		case DicomImageInfo.BLUE_PALETTE:
			DicomImageInfo.blues = getLut(elementLength);
			addInfo(tag, elementLength / 2);
			break;
		case DicomImageInfo.PIXEL_DATA:
			// Start of image data...
			if (elementLength != 0) {
				DicomImageInfo.offset = location;
				addInfo(tag, location);
				decodingTags = false;
			} else
				addInfo(tag, null);
			break;
		/**
		 * Patient Info Tags
		 */
		case DicomPatientInfo.PATIENT_NAME:

			String patietName = getString(elementLength);
			DicomPatientInfo.patientInfo(DicomPatientInfo.ATT_NAME, patietName);
			break;

		case DicomPatientInfo.PATIENT_SEX:

			String patietSex = getString(elementLength);
			DicomPatientInfo.patientInfo(DicomPatientInfo.ATT_SEX, patietSex);
			break;

		case DicomPatientInfo.PATIENT_BIRTH_DATE:

			String patietBirth = getString(elementLength);
			DicomPatientInfo.patientInfo(DicomPatientInfo.ATT_BIRTH_DATE,
					patietBirth);
			break;

		/**
		 * Exam Info Tags
		 */
		case DicomExamInfo.EXAM_ID:

			String examID = getString(elementLength);
			DicomExamInfo.examInfo(DicomExamInfo.ATT_EXAM_ID, examID);
			break;

		case DicomExamInfo.EXAM_DATE:

			String examDate = getString(elementLength);
			DicomExamInfo.examInfo(DicomExamInfo.ATT_EXAM_DATE, examDate);
			break;

		case DicomExamInfo.EXAM_MODALITY:

			String examModality = getString(elementLength);
			DicomExamInfo.examInfo(DicomExamInfo.ATT_MODALITY,
					examModality);
			break;

		case 0x7F880010:
			// What is this? - RAK
			if (elementLength != 0) {
				DicomImageInfo.offset = location + 4;
				decodingTags = false;
			}
			break;
		default:
			// Not used, skip over it...
			addInfo(tag, null);
		} // while(decodingTags)

		if (DicomImageInfo.fileType == DicomImageInfo.GRAY8) {
			if (DicomImageInfo.reds != null
					&& DicomImageInfo.greens != null
					&& DicomImageInfo.blues != null
					&& DicomImageInfo.reds.length == DicomImageInfo.greens.length
					&& DicomImageInfo.reds.length == DicomImageInfo.blues.length) {
				DicomImageInfo.fileType = DicomImageInfo.COLOR8;
				DicomImageInfo.lutSize = DicomImageInfo.reds.length;

			}
		}

		if (samplesPerPixel == 3 && photoInterpretation.startsWith("RGB")) {
			if (planarConfiguration == 0)
				DicomImageInfo.fileType = DicomImageInfo.RGB;
			else if (planarConfiguration == 1)
				DicomImageInfo.fileType = DicomImageInfo.RGB_PLANAR;
		} else if (photoInterpretation.endsWith("1 "))
			DicomImageInfo.whiteIsZero = true;

		if (!littleEndian)
			DicomImageInfo.intelByteOrder = false;

		if (!littleEndian)
			DicomImageInfo.intelByteOrder = false;

		return decodingTags;

	}

	public String getDicomInfo() {
		return new String(dicomInfo);
	}

	void addInfo(int tag, String value) throws IOException {
		String info = getHeaderInfo(tag, value);
		if (info != null) {
			int group = tag >>> 16;
			if (group != previousGroup)
				dicomInfo.append("\n");
			previousGroup = group;
			dicomInfo.append(tag2hex(tag) + info + "\n");
		}

	}

	void addInfo(int tag, int value) throws IOException {
		addInfo(tag, Integer.toString(value));
	}

	private String getHeaderInfo(int tag, String value) throws IOException {
		String key = i2hex(tag);
		// while (key.length()<8)
		// key = '0' + key;
		String id = (String) dictionary.get(key);
		if (id != null) {
			if (vr == IMPLICIT_VR && id != null)
				vr = (id.charAt(0) << 8) + id.charAt(1);
			id = id.substring(2);
		}
		if (value != null)
			return id + ": " + value;
		switch (vr) {
		case AE:
		case AS:
		case AT:
		case CS:
		case DA:
		case DS:
		case DT:
		case IS:
		case LO:
		case LT:
		case PN:
		case SH:
		case ST:
		case TM:
		case UI:
			value = getString(elementLength);
			break;
		case US:
			if (elementLength == 2)
				value = Integer.toString(getShort());
			else {
				value = "";
				int n = elementLength / 2;
				for (int i = 0; i < n; i++)
					value += Integer.toString(getShort()) + " ";
			}
			break;
		default:
			long skipCount = (long) elementLength;
			while (skipCount > 0)
				skipCount -= f.skip(skipCount);
			location += elementLength;
			value = "";
		}
		if (id == null)
			return null;
		else
			return id + ": " + value;
	}

	static char[] buf8 = new char[8];

	/** Converts an int to an 8 byte hex string. */
	String i2hex(int i) {
		for (int pos = 7; pos >= 0; pos--) {
			buf8[pos] = Tools.hexDigits[i & 0xf];
			i >>>= 4;
		}
		return new String(buf8);
	}

	char[] buf10;

	private String tag2hex(int tag) {
		if (buf10 == null) {
			buf10 = new char[11];
			buf10[4] = ',';
			buf10[9] = ' ';
		}
		int pos = 8;
		while (pos >= 0) {
			buf10[pos] = Tools.hexDigits[tag & 0xf];
			tag >>>= 4;
			pos--;
			if (pos == 4)
				pos--; // skip coma
		}
		return new String(buf10);
	}

	private double s2d(String s) {
		Double d;
		try {
			d = new Double(s);
		} catch (NumberFormatException e) {
			d = null;
		}
		if (d != null)
			return (d.doubleValue());
		else
			return (0.0);
	}

	private void getSpatialScale(String scale) {
		double xscale = 0, yscale = 0;
		int i = scale.indexOf('\\');
		if (i > 0) {
			xscale = s2d(scale.substring(0, i));
			yscale = s2d(scale.substring(i + 1));
		}
		if (xscale != 0.0 && yscale != 0.0) {
			DicomImageInfo.pixelWidth = xscale;
			DicomImageInfo.pixelHeight = yscale;
			DicomImageInfo.unit = "mm";
		}
	}

	@SuppressWarnings("unused")
	private boolean dicmFound() {
		return dicmFound;
	}

}
