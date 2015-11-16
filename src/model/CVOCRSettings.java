package model;

import java.io.File;

import org.opencv.core.Scalar;

public class CVOCRSettings {
	
	// OCR Strategy
	public enum FINDING_MODE {
		ALL_IMAGE,
		DETECT_FACE;
	}
	
	public enum OCR_MODE {
		EACH_NUMBER,
		TOTAL_NUMBER;
	}
	
	public enum OCR_ENGINE {
		TESSERACT;
	}
	
	public static String DEBUG_DIRECTORY = "debug" + File.pathSeparator;
	
	/**
	 * Location of the face detection xml
	 */
	public static String FACE_DETECTION_PATH = "res/tessdata/haarcascade_frontalface_alt2.xml";

}
