package model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;


/**
 * Analyse an image to get the bibs number.
 * Based on the work of Mathieu THEBAUD <math.thebaud@gmail.com>.
 */
public class CVOCR {

	/** Whole image */
	private Mat imageMat;
	
	/** Original image */
	private Mat imageOriginalMat;
	
	/** Whole image but on a simple channel */
	private Mat imageGrayMat;
	
	/** Lumi of the current analysed mat object. */
	private int currentMatLumi;
	
	/** Face detector */
	private CascadeClassifier faceDetector;
	
	/** White color as double value. */
	private static final double[] WHITE_COLOR = { 255.0 };

	/** Black color as double value. */
	private static final double[] BLACK_COLOR = { 0.0 };
	
	/** Face as a Rect object currently analysed. */
	private Rect currentFaceRect;
		
	/** Location of the face detection xml */
	private static String FACE_DETECTION_CLASSIFIER_PATH = "res/tessdata/haarcascade_frontalface_alt2.xml";

	/** Mode to find number on a image. */
	private FINDING_MODE currentFindingMode = FINDING_MODE.DETECT_FACE;

	/** OCR to use to recognize the numbers */
	private OCR_ENGINE currentOCREngine = OCR_ENGINE.TESSERACT;

	/** Mode to recognize the numbers on a image. */
	private OCR_MODE currentModeOcr = OCR_MODE.EACH_NUMBER;
	
	/**  Enumeration of available number searching strategy.*/
	private enum FINDING_MODE {
		ALL_IMAGE,
		DETECT_FACE;
	}
	
	/** Enumeration of available OCR mode. */
	private enum OCR_MODE {
		EACH_NUMBER,
		TOTAL_NUMBER;
	}
	
	/** Enumeration of available OCR engine. */
	private enum OCR_ENGINE {
		TESSERACT;
	}
	
	// Parameters for debug. ==================================================
	
	/** Debug directory path. */
	private static String DEBUG_DIRECTORY = "debug" + File.separator;

	/** Enable debug. */
	private Boolean isDebugEnabled;
	
	/** Debug image count (use to save different image). */
	private int debugImageCount;
	
	/** Color of rectangle to draw around the face for debug. */
	private Scalar colorFace = new Scalar(255, 0, 0);
	/** Color of rectangle to draw around the body for debug. */
	private Scalar colorBody = new Scalar(0, 255, 0);
	/** Color of the text to write on the Mat object for debug. */
	private Scalar colorText = new Scalar(255, 255,0);
	/** Color of the number to write on the Mat object for debug. */
	private Scalar colorNoNumber = new Scalar(0,0,255);
	
	/** Random object to generate random color. */
	private static Random random = new Random();
	
	// Parameters to adjust detection. ========================================

	/** Parameter : canny threshold */
	private int paramCannyThresHold = 170;

	 /** Minimum of vertices that a contour shape must have to be accept as a 
	 * potential letter. */
	private int paramMinVertices = 5;

	/** The number of pixel to add on each possible letter side. */
	private int paramLetterBoundAdd = 1;

	/** The threshold of a color to be considered as white. */
	private int paramWhiteThreshold = 100;

	/** The percentage of white color found to consider a part as a race bib. */
	private int paramMinWhiteColorPercentageBib = 20;

	/** The percentage of white color found to consider that it can't be a race bib. */
	private int paramMaxWhiteColorPercentageBib = 75;

	/** Vertical tolerance to find a rect groups. */
	private int paramVerticalTolerance = 12;
	
	/** Horizontal tolerance to find a rect groups. */
	private int paramHorizontalTolerance = 23;

	/** Minimum area of a letter. */
	private int paramMinLetterArea = 120;
	
	/** Maximum area of a letter. */
	private int paramMaxLetterArea = 150000;
	
	/** Space between two digit when put together. */
	private int paramSpaceBetweenDigits = 4;

	/** The maximum length of a detected number. */
	private int paramNumberMaxLength = 10;

	// CONSTRUCTORS ===========================================================

	/**
	 * Constructor of the class.
	 */
	public CVOCR() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//Face detector
		this.faceDetector = new CascadeClassifier(FACE_DETECTION_CLASSIFIER_PATH);
	}

	// METHODS. ==============================================================
	
	/**
	 * Launch the analyse of an image.
	 * @param im - ImgModel object to analyse.
	 * @return an arraylist of found numbers.
	 */
	public ArrayList<Integer> launchDetection(ImgModel im) {
		long startTime = 0 ;
		if (Settings.getBENCH()) {
			startTime = System.nanoTime();
		}
		
		this.isDebugEnabled = Settings.getDEBUG();
		String path = im.getPath();
		this.imageMat = Imgcodecs.imread(path);
		this.imageOriginalMat = this.imageMat;
		this.imageGrayMat = this.imageMat.clone();
		Imgproc.cvtColor(this.imageGrayMat, this.imageGrayMat, Imgproc.COLOR_BGR2GRAY);
		
		if (Settings.getBENCH()) {
			long endTime = System.nanoTime();
			long processTime = endTime - startTime;
			BenchData bData = new BenchData();
			bData.setProcTime((int) processTime);
			//im.setBenchData(bData);
		}

		ArrayList<Integer> results = getImageNumbers();
		return results;
	}

	/**
	 * To get the list of numbers in the current image<br>
	 * @return the list with all numbers found in the current image, can be empty
	 * @throws Exception if something is wrong in detection
	 */
	public ArrayList<Integer> getImageNumbers() {
		//Function of configuration
		ArrayList<Integer> numbers = null;
		//Function of OCR engine, chose the best way to detect digit
		switch (this.currentOCREngine) {
		case TESSERACT:
			this.currentModeOcr = OCR_MODE.TOTAL_NUMBER;
			break;
		}
		//Now get with the chosen mode to find
		switch (this.currentFindingMode) {
		case ALL_IMAGE:
			numbers = this.getImageNumbersAllImage();
			break;
		case DETECT_FACE:
			numbers = this.getImageNumbersFaceDetection();
			break;
		}
		// Save image if debug
		if (this.isDebugEnabled) {
			Imgproc.putText(this.imageMat, numbers.toString(), new Point(20, 20), Core.FONT_HERSHEY_PLAIN, 1.5, this.colorText);
			this.saveMat(this.imageMat, DEBUG_DIRECTORY + File.pathSeparator + "lastDetectDebug.png");
		}

		return numbers;
	}


	/**
	 * To get the number in the image with searching digit in the whole image.<br>
	 * @return the found numbers
	 */
	private ArrayList<Integer> getImageNumbersAllImage() {
		ArrayList<Integer> foundNumbers = new ArrayList<Integer>();
		ArrayList<Integer> nbs = this.getMatNumbers(this.imageGrayMat, null);
		for (int nb : nbs) {
			if (!foundNumbers.contains(nb)) {
				foundNumbers.add(nb);
			}
		}
		return foundNumbers;
	}

	/**
	 * To get the numbers in the image with the face detection method
	 * @return the numbers found
	 */
	private ArrayList<Integer> getImageNumbersFaceDetection() {
		// Begin
		ArrayList<Integer> foundNumbers = new ArrayList<Integer>();
		// Search for race bib zone
		// Search face
		Rect[] facesAreaList = this.getFaces();
		for (Rect face : facesAreaList) {
			this.currentFaceRect = face;
			System.out.println(face.height);
			System.out.println(face.width);
			Rect bodyArea = this.getBodyArea(face);
			Mat bodyMat = getMatPart(imageOriginalMat, bodyArea);
			System.out.println("lu :" + this.getMatLuminance(bodyMat));
			// Draw to debug
			if (this.isDebugEnabled) {
				Imgproc.rectangle(this.imageMat, new Point(face.x, face.y), new Point(face.x + face.width, face.y + face.height), this.colorFace);
				Imgproc.rectangle(this.imageMat, bodyArea.tl(), bodyArea.br(), this.colorBody);
			}
			// Take the zone and search for possible letters
			Mat numberZoneMat = CVOCR.getMatPart(imageGrayMat, bodyArea);
			this.currentMatLumi = this.getMatLuminance(bodyMat);
			this.setDynamicsParams();
			// Check for already present numbers
			ArrayList<Integer> nbs = this.getMatNumbers(numberZoneMat, bodyArea);
			for (int nb : nbs) {
				if (!foundNumbers.contains(nb)) {
					foundNumbers.add(nb);
				}
			}
		}
		return foundNumbers;
	}

	/**
	 * Detect face in the image.
	 * @return an arraylist of all the face.
	 */
	private Rect[] getFaces() {
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(this.imageMat, faceDetections);
		return faceDetections.toArray();
	}
	
	/**
	 * Determine a possible area of the body according to face position.
	 * @param face - a Rect object representing a face
	 * @return a rect object representing the top of the body. 
	 */
	private Rect getBodyArea(Rect face) {
		Rect estimatePosition = new Rect();
		estimatePosition.y = face.y + face.height + face.height / 2;
		estimatePosition.width = face.width * 2;
		estimatePosition.x = face.x - face.width / 2;
		estimatePosition.height = face.height * 3;
		return estimatePosition;
	}

	/**
	 * This method help to make clean cut from a mat
	 * @param source the image where we want to cut a part
	 * @param area the cutting area
	 * @return the cut part of the image, or null if the cut part is complety out of the source image
	 */
	private static Mat getMatPart(Mat source, Rect area) {
		Mat part = null;
		CVOCR.cleanRectForCrop(area, source.width(), source.height());
		if (area.width != 1 || area.height != 1) {
			part = new Mat();
			Mat cleaned = new Mat(source, area);
			cleaned.copyTo(part);
		}
		return part;
	}

	/**
	 * To get a clean rect to subdivide a image. This will avoid cut out of the image.<br>
	 * If the rect will be out, set the width and the height to 1.
	 * @param area the area to cut
	 * @param w the maximum width
	 * @param h the maximum height
	 */
	private static void cleanRectForCrop(Rect area, int w, int h) {
		if (area.x < w && area.y < h) {
			if (area.x < 0) {
				area.width = area.width + area.x;
				area.x = 0;
			}
			if (area.y < 0) {
				area.height = area.height + area.y;
				area.y = 0;
			}
			if (area.x + area.width >= w) {
				area.width = area.width - (area.x + area.width - w);
			}
			if (area.y + area.height >= h) {
				area.height = area.height - (area.y + area.height - h);
			}
		} else {
			area.width = 1;
			area.height = 1;
		}
	}

	/**
	 * To get all the number that we can find in a mat
	 * @param src the bitmap to search for number
	 * @param sourceBitmapArea the rect to know what is the part of source image we took to have src, this will be use to debug. If null, consider that src have the same size that original bitmap.
	 * @return the list of found numbers
	 */
	private ArrayList<Integer> getMatNumbers(Mat src, Rect sourceBitmapArea) {
		ArrayList<Integer> foundNumbers = new ArrayList<Integer>();
		if (src != null) {
			ArrayList<Rect> letterZones = this.getPossibleLetterBounds(src);
			// Regroup in text zImgcodecs.imwriteone
			ArrayList<TextGroup> textZones = this.getPossibleTextZones(letterZones);
			if (textZones.size() > 1) {
				//textZones = this.selectMostLikelyBibsTextGroup(textZones, sourceBitmapArea);
			}
			// For each text zone, cut and try OCR
			for (TextGroup group : textZones) {
				int number = this.getGroupNumber(group, src);
				if (number != -1) {
					if (!foundNumbers.contains(number)) {
						foundNumbers.add(number);
					}
				}
				// Debug draw
				if (this.isDebugEnabled) {
					Scalar color = CVOCR.getRandomColor();
					// If it's not the original bitmap
					if (sourceBitmapArea != null) {
						if (number != -1) {
							for (Rect r : group.rectGroup) {
								Imgproc.rectangle(this.imageMat, CVOCR.getPointFrom(sourceBitmapArea.tl(), r.tl()), CVOCR.getPointFrom(sourceBitmapArea.tl(), r.br()), color);
							}
							Imgproc.putText(this.imageMat, "" + number, CVOCR.getPointFrom(sourceBitmapArea.tl(), group.getGroupBounds().br()), Core.FONT_HERSHEY_PLAIN, 1.5, this.colorText);
						}else{
							for (Rect r : group.rectGroup) {
								Imgproc.rectangle(this.imageMat, CVOCR.getPointFrom(sourceBitmapArea.tl(), r.tl()), CVOCR.getPointFrom(sourceBitmapArea.tl(), r.br()), this.colorNoNumber);
							}
						}
					} else {
						if (number != -1) {
							for (Rect r : group.rectGroup) {
								Imgproc.rectangle(this.imageMat, r.tl(), r.br(), color);
							}
							Imgproc.putText(this.imageMat, "" + number, group.getGroupBounds().br(), Core.FONT_HERSHEY_PLAIN, 1.5, this.colorText);
						} else {
							for (Rect r : group.rectGroup) {
								Imgproc.rectangle(this.imageMat, r.tl(), r.br(), this.colorNoNumber);
							}
						}
					}
				}
			}
		}
		return foundNumbers;
	}

	/**
	 * Try to remove textgroup that are to close to the borders of the body rectangle.
	 * @param textZones - arraylist of the textzones found.
	 * @param sourceRect - the rectangle of the body.
	 * @return an arraylist of a cleaned textones.
	 */
	private ArrayList<TextGroup> selectMostLikelyBibsTextGroup(ArrayList<TextGroup> textZones, Rect sourceRect) {
		TextGroup bibsTextGroup = new TextGroup();
		bibsTextGroup = textZones.get(0);
		
		ArrayList<TextGroup> textZonesClean = new ArrayList<CVOCR.TextGroup>();
		
		Rect tempRect = new Rect(sourceRect.x + 10, sourceRect.y + 10, sourceRect.width - 10, sourceRect.height - 10);
		
		//Mat test = this.getMatPart(this.imageOriginalMat, tempRect);
		//this.saveMat(test, debugImageCount+"temprect.png");
		
		for (TextGroup group : textZones) {
			Rect rect = group.getGroupBounds();
			if (tempRect.contains(new Point(rect.x, rect.y)) 
					&& tempRect.contains(new Point(rect.x + rect.width, rect.y))
					&& tempRect.contains(new Point(rect.x - rect.height, rect.y))
					&& tempRect.contains(new Point(rect.x + rect.width, rect.y - rect.height))
					) {
				textZonesClean.add(group);
			}	
		}
		return textZonesClean;	
	}

	/**
	 * To get a random color
	 * @return a random color
	 */
	private static Scalar getRandomColor() {
		return new Scalar(random.nextInt(255), random.nextInt(255), random.nextInt(255));
	}

	/**
	 * To get the number found in a TextGroup.<br>
	 * This will use the currentModeOcr to know the method to use
	 * @param group the group to OCR
	 * @param src the source mat where group is
	 * @return the number contained in group, if nothing is found, return -1
	 */
	private int getGroupNumber(TextGroup group, Mat src) {
		int number = -1;

		switch (this.currentModeOcr) {
		case TOTAL_NUMBER:
			//Mat groupMat = this.cleanGroupMatForOCR(group, src);
			this.sortTextGroup(group);
			Rect groupBounds = group.getGroupBounds();
			Mat groupMat = getMatPart(src, groupBounds);
			if (groupMat != null) {
					number = this.getNumber(groupMat);
				if (isDebugEnabled && number != -1) {
					this.saveMat(groupMat, DEBUG_DIRECTORY + File.separator + this.debugImageCount + "-r" + number + ".png");
					this.debugImageCount++;
				}
			}
			break;
		case EACH_NUMBER:
			String groupText = "";
			// Sort
			this.sortTextGroup(group);
			for (Rect groupRect : group.rectGroup) {
				Mat part = CVOCR.getMatPart(src, groupRect);
				if (part != null) {
					int nb = this.getNumber(part);
					if (nb != -1 )//&& nb < 10)
						groupText += nb;
					// Save if debug
					if (isDebugEnabled && nb != -1) {
						this.saveMat(part, DEBUG_DIRECTORY + File.pathSeparator + this.debugImageCount+ ".png");
						this.debugImageCount++;
					}
				}
			}
			// Try to convert to int
			try {
				number = Integer.parseInt(groupText);
			} catch (Exception e) {}
			break;
		}
		return number;
	}

	/**
	 * To OCR a mat and get the number
	 * @param part the mat to detect
	 * @return the found number or -1 if there is no number
	 */
	private int getNumber(Mat part) {
		int number = -1;

		switch (this.currentOCREngine) {
		case TESSERACT:
			BufferedImage img = null;

			MatOfByte byteMat = new MatOfByte();
			Imgcodecs.imencode(".jpg", part, byteMat);
			byte[] bytes = byteMat.toArray();
			InputStream in = new ByteArrayInputStream(bytes);
			try {
				img = ImageIO.read(in);
			} catch (IOException e) {
				System.err.println("IOExcection: " + e.getMessage());
			}

			Tesseract tesseractInstance = new Tesseract();
			tesseractInstance.setTessVariable("tessedit_char_whitelist", "0123456789");
			
			try {
				String recognized = tesseractInstance.doOCR(img);
				recognized = this.cleanText(recognized);
				if (!recognized.isEmpty()) {
					number = Integer.parseInt(recognized);
				}
			} catch (TesseractException e) {
				System.err.println("TesseractException: " + e.getMessage());			
			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
			}
			break;
		}
		return number;
	}

	/**
	 * To clean a recognized text
	 * @param text the recognized text
	 * @return the cleaned text
	 */
	private String cleanText(String text) {
		text = text.replaceAll(" ", "");
		text = text.replaceAll("[.,']", "");
		text = text.replace("\n","");
		if (text.length() > this.paramNumberMaxLength) {
			text = text.substring(0, this.paramNumberMaxLength);
		}
		return text;
	}

	/**
	 * This will detect contour and determine the place in the Mat that could contains numbers or letter.<br>
	 * The returned list contains bounds for each possible letter.<br>
	 * This will also add a distance to bounds to be able to better detect the letter and clean each possible digit.
	 * @param part the image to search in
	 * @return the list of bounds for each possible letter
	 */
	private ArrayList<Rect> getPossibleLetterBounds(Mat part) {
		ArrayList<Rect> letterZones = new ArrayList<Rect>(150);
		//this.saveMat(part, debugImageCount+"beforecanny.png");
		// Clone to avoid modify
		Mat workingMat = part.clone();
		// Change color
		Imgproc.Canny(workingMat, workingMat, this.paramCannyThresHold, this.paramCannyThresHold);
		//this.saveMat(workingMat, debugImageCount+"canny.png");
		// Find the contour
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>(1500);
		Imgproc.findContours(workingMat.clone(), contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		// For each contour, search for possible letter
		for (MatOfPoint contour : contours) {
			Rect contourRect = Imgproc.boundingRect(contour);
			int size = (int) Imgproc.contourArea(contour);
			if (size >= this.paramMinVertices && contourRect.area() > this.paramMinLetterArea 
					&& contourRect.area() < this.paramMaxLetterArea 
					&& contourRect.height > contourRect.width 
					&& contourRect.height < contourRect.width * 4.5 
					&& this.isValidPart(part, contourRect)) {
				// Modifiy bounds and add
				contourRect.x -= this.paramLetterBoundAdd;
				contourRect.y -= this.paramLetterBoundAdd;
				contourRect.width += this.paramLetterBoundAdd * 2;
				contourRect.height += this.paramLetterBoundAdd * 2;
				letterZones.add(contourRect);
			}
		}
		return letterZones;
	}
	
	/**
	 * To know if a part is considered as a potential black digit on white race bib.<br>
	 * This will also clean the mat part, as it, the pixel will be access just once.
	 * @param mat the mat where part is located
	 * @param part the part to analyse
	 * @return true if part considered as possible digit, else return false
	 */
	private boolean isValidPart(Mat mat, Rect part) {
		boolean valid = false;
		int maxX = part.x + part.width + 1;
		int maxY = part.y + part.height + 1;
		// Count number of white pixel in src
		int pixelCount = 0, whiteCount = 0;
		double color;
		for (int x = part.x - 1; x < maxX; x++) {
			for (int y = part.y - 1; y < maxY; y++) {
				color = mat.get(y, x)[0];
				// If considered as white pixel
				if (color > this.paramWhiteThreshold) {
					whiteCount++;
					mat.put(y, x, CVOCR.WHITE_COLOR);
				} else {
					mat.put(y, x, CVOCR.BLACK_COLOR);
				}
				pixelCount++;
			}
		}
		// Valid
		double perc = ((whiteCount * 1.0 / pixelCount * 1.0) * 100.0);
		valid = perc > this.paramMinWhiteColorPercentageBib && perc < this.paramMaxWhiteColorPercentageBib;
		return valid;
	}
	
	/**
	 * Set parameters for detection according to face height
	 * and luminance.
	 */
	private void setDynamicsParams() {
		
		// Set the settings choose by the user.
		if (Settings.getEnableUserSettings()) {
			if (Settings.getOCVParamWitheThreshold() != -1) {
				paramWhiteThreshold = Settings.getOCVParamWitheThreshold();
			}
			if (Settings.getOCVFaceSize() != -1) {
				this.paramHorizontalTolerance = Settings.getOCVFaceSize();
				this.paramVerticalTolerance = Settings.getOCVFaceSize();
			}
			if (Settings.getOCVMinLetterSize() != -1) {
				this.paramMinLetterArea = Settings.getOCVMinLetterSize();
			}
		} else {
			// Try to determine params automatically.

			if (currentMatLumi >= 30 && currentMatLumi < 40) {
				this.paramWhiteThreshold = 90;
			}	else if (currentMatLumi >= 40 && currentMatLumi < 50) {
				this.paramWhiteThreshold = 100;
			} else if (currentMatLumi >= 40 && currentMatLumi < 60) {
				this.paramWhiteThreshold = 160;
			} else if (currentMatLumi >= 60 && currentMatLumi < 70) {
				this.paramWhiteThreshold = 170;
			} else if (currentMatLumi >= 70 ) {
				this.paramWhiteThreshold = 200;
			}

			this.paramWhiteThreshold = 160;

			if (currentFaceRect != null) {
				int currentFaceHeight = this.currentFaceRect.height;
				if (currentFaceHeight >= 100 && currentFaceHeight < 200) {
					this.paramHorizontalTolerance = 50;
					this.paramVerticalTolerance = 50;
				} else if (currentFaceHeight >= 200 && currentFaceHeight < 300) {
					this.paramVerticalTolerance = 60;
					this.paramHorizontalTolerance = 60;
					this.paramMinLetterArea = 1000;
				} else if (currentFaceHeight >= 300 && currentFaceHeight < 500) {
					this.paramVerticalTolerance = 65;
					this.paramHorizontalTolerance = 65;
					this.paramMinLetterArea = 1000;
				} else if (currentFaceHeight >= 500 && currentFaceHeight < 550) {
					//this.paramHorizontalTolerance = 70;
					//this.paramVerticalTolerance = 70;
					//this.paramMinLetterArea = 25000;
				} else if (currentFaceHeight >= 550 && currentFaceHeight < 600) {
					this.paramHorizontalTolerance = 100;
					this.paramVerticalTolerance = 120;
					//this.paramMinLetterArea = 25000;
					this.paramMinLetterArea = 10000;
				} else if (currentFaceHeight >= 600 && currentFaceHeight < 650) {
					this.paramHorizontalTolerance = 150;
					this.paramVerticalTolerance = 250;
					this.paramMinLetterArea = 10000;
				} else if (currentFaceHeight >= 650) {
					this.paramHorizontalTolerance = 200;
					this.paramVerticalTolerance = 250;
					this.paramMinLetterArea = 10000;
				}
			}
		}
	}
	
	/**
	 * Calculate the average luminance of a RGB mat object.
	 * @param mat to caculate
	 * @return the luminance in int.
	 */
	private int getMatLuminance(Mat mat) {
		Mat matHSL = new Mat(); 
		Imgproc.cvtColor(mat, matHSL, Imgproc.COLOR_RGB2HLS);
		
		int size = (int) (mat.total() * mat.channels());
		double[] tempPix = new double[size];
		double lumi = 0;
		
		for (int i = 0 ; i < mat.rows() ; i++) {
			for (int j = 0 ; j < mat.cols() ; j++) {
				tempPix = matHSL.get(i, j);
				lumi = lumi + tempPix[1];
			}
		}
		return (int) Math.round(lumi / size);
	}
	
	/**
	 * Debug Function. Save image in choosen path.
	 * @param img image to save.
	 * @param path to write the file.
	 */
	private void saveMat(Mat img, String path) {
		Imgcodecs.imwrite(path, img);
	}

	/**
	 * To get the rectangle groups that could contains a full text.<br>
	 * This will search for all letter if it have neighbors and then will group all neighbors letter that could form a text
	 * @param possibleLetters the list of all found possible letters
	 * @return the list of all rect group that could contains a text
	 */
	private ArrayList<TextGroup> getPossibleTextZones(ArrayList<Rect> possibleLetters) {
		ArrayList<TextGroup> textGroups = new ArrayList<TextGroup>(20);
		Iterator<Rect> it = possibleLetters.iterator();
		while (it.hasNext()) {
			// Get the current letter and its side rects
			Rect currentLetter = it.next();
			ArrayList<Rect> sideRects = this.getNeighborsRect(currentLetter, possibleLetters);
			// Check if another group contains one of the letter in the side rect
			boolean contains = false;
			for (int i = 0; i < textGroups.size() && !contains; i++) {
				// For each rect of the current group
				for (int j = 0; j < sideRects.size() && !contains; j++) {
					contains = textGroups.get(i).rectGroup.contains(sideRects.get(j));
					// If contains add every side rect to the containing group
					if (contains) {
						textGroups.get(i).addAllRect(sideRects);
					}
				}
			}
			// If end without any letter that bellow to another group, then add to list
			if (!contains) {
				TextGroup rg = new TextGroup();
				rg.addAllRect(sideRects);
				textGroups.add(rg);
			}
		}
		return textGroups;
	}

	/**
	 * To get all the rect that can be considered as start neighbors.<br>
	 * This is used to determine rect group that can form a full text
	 * @param start the rect that we want to find the neighbors
	 * @param list the list where search the neighbors
	 * @return the list of all found neighbors, also contains the start itself
	 */
	private ArrayList<Rect> getNeighborsRect(Rect start, ArrayList<Rect> list) {
		// Create and add start
		ArrayList<Rect> found = new ArrayList<Rect>();
		found.add(start);
		// For each possible neighbors
		for (Rect r : list) {
			// Check if the size is quite the same
			if (r.width >= start.width - this.paramHorizontalTolerance && r.width <= start.width + this.paramHorizontalTolerance && r.height >= start.height - this.paramVerticalTolerance && r.height <= start.height + this.paramVerticalTolerance) {
				// Create the tolerance rectangle
				Point toleranceTL = new Point(start.tl().x - this.paramHorizontalTolerance, start.tl().y - this.paramVerticalTolerance);
				Point toleranceBR = new Point(start.br().x + this.paramHorizontalTolerance, start.br().y + this.paramVerticalTolerance);
				Rect toleranceRect = new Rect(toleranceTL, toleranceBR);
				// Check if in
				boolean widthOk = r.br().x > toleranceRect.x && r.tl().x < toleranceRect.br().x;
				boolean heightOk = r.tl().y > toleranceRect.tl().y && r.br().y < toleranceRect.br().y;
				if (widthOk && heightOk) {
					found.add(r);
				}
			}
		}
		return found;
	}

	/**
	 * To get a point that correspond to the origin of the rectTL Point
	 * @param rectTL the rectTL point
	 * @param point the wanted point
	 * @return a new point that really correspond to rectTL origin
	 */
	private static Point getPointFrom(Point rectTL, Point point) {
		return new Point(rectTL.x + point.x, rectTL.y + point.y);
	}

	
	// TextGroup internClass ==================================================

	/**
	 * A class that group several rect in a group.<br>
	 * This is use to find rect that are close to each other and determine full text position
	 * @author Mathieu THEBAUD <math.thebaud@gmail.com>
	 */
	public class TextGroup {
		ArrayList<Rect> rectGroup;

		/**
		 * Create a new text group
		 */
		public TextGroup() {
			this.rectGroup = new ArrayList<Rect>();
		}

		/**
		 * To add a new rect to the group, add only if the group doesn't already contains it
		 * @param r the rect to add
		 */
		public void addRect(Rect r) {
			if (!rectGroup.contains(r)) {
				rectGroup.add(r);
			}
		}

		/**
		 * To add every rect from the list, only add if not yet in the group
		 * @param list the list to add
		 */
		public void addAllRect(ArrayList<Rect> list) {
			for (Rect r : list) {
				this.addRect(r);
			}
		}

		/**
		 * To get a rectangle that contains all the group rectangles
		 * @return a rect that represent this group bounds
		 */
		public Rect getGroupBounds() {
			int xMin = 1000;
			int yMin = 1000;
			int xWMax = -1000;
			int yHMax = -1000;
			for (Rect r : this.rectGroup) {
				int xW = r.x + r.width;
				int yH = r.y + r.height;
				if (r.x < xMin)
					xMin = r.x;
				if (r.y < yMin)
					yMin = r.y;
				if (xW > xWMax)
					xWMax = xW;
				if (yH > yHMax)
					yHMax = yH;
			}
			return new Rect(new Point(xMin, yMin), new Point(xWMax, yHMax));
		}

		/**
		 * Get the width of the group when we put each element together<br>
		 * Not equals to the bounds rect width because in the group elements can intersect
		 * @return the width of the group
		 */
		public int getGroupWidth() {
			int w = 0;
			for (Rect r : this.rectGroup) {
				w += r.width;
			}
			return w;
		}
	}

	/**
	 * To sort rect of a TextGroup, to place the rect in the list from left to right (to correspond to a text)
	 * @param group the group to sort
	 */
	private void sortTextGroup(TextGroup group) {
		int size = group.rectGroup.size();
		if (size > 1) {
			ArrayList<Rect> list = group.rectGroup;
			int sorted = 0;
			while (sorted != size) {
				double minX = list.get(sorted).x;
				int minIndex = sorted;
				// Find the smaller x
				for (int i = sorted; i < size; i++) {
					if (list.get(i).x < minX) {
						minIndex = i;
						minX = list.get(i).x;
					}
				}
				// Replace
				Rect currentIndex = list.get(sorted);
				list.set(sorted, list.get(minIndex));
				list.set(minIndex, currentIndex);
				sorted++;
			}
		}
	}

}