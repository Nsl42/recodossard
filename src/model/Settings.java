/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author nsl
 */
public class Settings {
	
	public enum Algo{
		DEFAULT,
		MANUAL;
	}
		
	static private boolean EXIF = false;
	static private boolean ADDITONNAL_DATA = false;
	static private boolean BENCH = false;
	static private boolean DEBUG = false;
	static private Algo ALGO = Algo.DEFAULT;

	static public boolean getEXIF() {
		return Settings.EXIF;
	}

	static private void setEXIF(boolean EXIF) {
		Settings.EXIF = EXIF;
	}

	static public boolean getADDITONNAL_DATA() {
		return ADDITONNAL_DATA;
	}

	static private void setADDITONNAL_DATA(boolean ADDITONNAL_DATA) { Settings.ADDITONNAL_DATA = ADDITONNAL_DATA; }

	static public boolean getBENCH() {
		return BENCH;
	}

	static private void setBENCH(boolean BENCH) {
		Settings.BENCH = BENCH;
	}

	static public boolean getDEBUG() {
		return DEBUG;
	}

	static private void setDEBUG(boolean DEBUG) {
		Settings.DEBUG = DEBUG;
	}

	static public Algo getALGO() {
		return ALGO;
	}

	static private void setALGO(Algo ALGO) {
		Settings.ALGO = ALGO;
	}
	static public void setSettings(boolean exif, boolean data, boolean bench, boolean debug, Algo algo)
	{
		Settings.setEXIF(exif);
		Settings.setADDITONNAL_DATA(data);
		Settings.setBENCH(bench);
		Settings.setDEBUG(debug);
		Settings.setALGO(algo);

	}
	
	// Params for bibs detections
	
	/** True to enable user settings. */
	static private boolean enableUserSettings = false;
	
	/** White threshold. Increase if the photo is lightly. */
	static private int ocvParamWhiteThreshold = -1;
	
	/** Set the minimum size of the number to detect. */
	static private int ocvMinLetterSize = -1;
	
	/** Increase this number if the photo use an large zoom. */
	static private int ocvFaceSize = -1;
	
	// Setters & Getters
	static public boolean getEnableUserSettings() {
		return Settings.enableUserSettings;
	}
	
	static public void setEnableUserSettings(boolean enableUserSettings) {
		Settings.enableUserSettings = enableUserSettings;
	}
	
	static public void setOCVParamWitheThreshold(int ocvParamWhiteThreshold) {
		Settings.ocvParamWhiteThreshold = ocvParamWhiteThreshold;
	}
	
	static public int getOCVParamWitheThreshold() {
		return Settings.ocvParamWhiteThreshold;
	}
	
	public static void setOCVMinLetterSize(int ocvMinLetterSize) {
		Settings.ocvMinLetterSize = ocvMinLetterSize;
	}
	
	public static int getOCVMinLetterSize() {
		return Settings.ocvMinLetterSize;
	}

	public static int getOCVFaceSize() {
		return Settings.ocvFaceSize;
	}
	
	public static void setOCVFaceSize(int ocvFaceSize) {
		Settings.ocvFaceSize = ocvFaceSize;
	}	
}
