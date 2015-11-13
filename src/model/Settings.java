package model;

import java.io.File;
import java.net.URL;

public class Settings {
	
	/**
	 * Location of the face detection xml
	 */
	//public final static URL FACE_DETECTION_PATH = 
		//	Configuration.class.getResource("/res/tessdata" + File.separator + "haarcascade_frontalface_alt2.xml");
	
	public final static String FACE_DETECTION_PATH = "res/tessdata/haarcascade_frontalface_alt2.xml";
	
	public static boolean debugEnabled;
	
	public static enum OCR_MODE {
		EACH_NUMBER,
		TOTAL_NUMBER
	}
	
	public static enum FINDING_MODE {
		DETECT_FACE,
		ALL_IMAGE
	}
	
	public static enum OCR_ENGINE {
		CUSTOM,
		TESSERACT
	}
	
	public final static File DEBUG_DIRECTORY = new File("debug/");
	
	public final static String OCR_DATA_PATH = "res/";
}
