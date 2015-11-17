package model;

import java.awt.Color;
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
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;

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

import model.CVOCRSettings.FINDING_MODE;
import model.CVOCRSettings.OCR_ENGINE;
import model.CVOCRSettings.OCR_MODE;



public class CVOCR {
	
	/**
	 * Random object to generate random color
	 */
	private static Random random = new Random();

	/**
	 * Face detector
	 */
	private CascadeClassifier faceDetector;

	/**
	 * To know if a detection is currently running
	 */
	private boolean detectionRunning;

	/**
	 * Whole image but on a simple channel
	 */
	private Mat imageGrayMat;

	/**
	 * Whole image
	 */
	private Mat imageMat;

	/**
	 * Colors
	 */
	private Scalar colorFace = new Scalar(255, 0, 0);
	private Scalar colorBody = new Scalar(0, 255, 0);
	private Scalar colorText = new Scalar(255, 255,0);
	private Scalar colorNoNumber = new Scalar(0,0,255);

	/**
	 * White color as double value
	 */
	private static final double[] WHITE_COLOR = { 255.0 };

	/**
	 * Black color as double value
	 */
	private static final double[] BLACK_COLOR = { 0.0 };


	// OPENCV Parameters ======================================================

	/**
	 * Parameter : canny threshold
	 */
	private int paramCannyThresHold = 170;

	/**
	 * Minimum of vertices that a contour shape must have to be accept as a 
	 * potential letter
	 */
	// 12-5 before
	private int paramMinVertices = 3;

	/**
	 * The number of pixel to add on each possible letter side
	 */
	private int paramLetterBoundAdd = 1;

	/**
	 * Minimum area of a letter
	 */
	private int paramMinLetterArea = 120;

	/**private OCR_MODE currentOCRMODE = OCR_MODE.EACH_NUMBE
	 * The threshold of a color to be considered as white
	 */
	//130-110 before
	private int paramWhiteThreshold = 100;

	/**
	 * The percentage of white color found to consider a part as a race bib (if than )
	 */
	//25-20 before
	private int paramMinWhiteColorPercentageBib = 20;

	/**
	 * The percentage of white color found to consider that it can't be a race bib
	 */
	private int paramMaxWhiteColorPercentageBib = 75;

	/**
	 * Param to find rect groups
	 */
	private int paramVerticalTolerance = 12;
	private int paramHorizontalTolerance = 23;

	/**
	 * Maximum area of a letter
	 */
	private int paramMaxLetterArea = 150000;

	/**
	 * Space between two digit when put together
	 */
	private int paramSpaceBetweenDigits = 4;

	/**
	 * The maximum length of a detected number
	 */
	private int paramNumberMaxLength = 10;
	// ========================================================================

	private FINDING_MODE currentFindingMode = FINDING_MODE.DETECT_FACE;

	private OCR_ENGINE currentOCREngine = OCR_ENGINE.TESSERACT;

	/**
	 * Mode to recognize the numbers on a image
	 */
	private OCR_MODE currentModeOcr = OCR_MODE.EACH_NUMBER;

	/**
	 * OCR Custom engine
	 */
	//gprivate NumberOCR customEngine;

	/**
	 * Debug image count (use to save different image)
	 */
	private int debugImageCount;
	
	/**
	 * Enable debug
	 */
	private Boolean debugEnabled;
	
	

	// CONSTRUCTORS ===========================================================

	public CVOCR() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//Face detector
		this.faceDetector = new CascadeClassifier(CVOCRSettings.FACE_DETECTION_PATH);
	}

	// ========================================================================
	
	// Getters ================================================================
	
	public ArrayList<Integer> launchDetection(Boolean enableDebug, ImgModel im) {
		this.debugEnabled = enableDebug;
		String path = im.getPath();
		this.imageMat = Imgcodecs.imread(path);
		this.imageGrayMat = this.imageMat.clone();
		Imgproc.cvtColor(this.imageGrayMat, this.imageGrayMat, Imgproc.COLOR_BGR2GRAY);

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

		//Current detection will chose to find in all image if it set a detection zone
		// TODO
		/*if (this.currentDetection != null) {
			if (this.currentDetection.isResized()) {
				this.currentModeFind = NumberFinder.MODE_FIND_ALL_IMAGE;
			}
		}*/

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
		if (this.debugEnabled) {
			Imgproc.putText(this.imageMat, numbers.toString(), new Point(20, 20), Core.FONT_HERSHEY_PLAIN, 1.5, this.colorText);
			this.saveMat(this.imageMat, CVOCRSettings.DEBUG_DIRECTORY + File.pathSeparator + "lastDetectDebug.png");
		}

		return numbers;
	}

	/**
	 * Debug Function. Save image in choosen path.
	 * @param img image to save.
	 * @param path ?
	 */
	private void saveMat(Mat img, String path) {
		Imgcodecs.imwrite(path, img);
	}

	/**
	 * To get the number in the image with searching digit in the whole image.<br>
	 * @return the found numbers
	 */
	private ArrayList<Integer> getImageNumbersAllImage() {
		ArrayList<Integer> foundNumbers = new ArrayList<Integer>();
		detectionRunning = true;
		ArrayList<Integer> nbs = this.getMatNumbers(this.imageGrayMat, null);
		for (int nb : nbs) {
			if (!foundNumbers.contains(nb)) {
				foundNumbers.add(nb);
			}
		}
		detectionRunning = false;
		return foundNumbers;
	}

	/**
	 * To get the numbers in the image with the face detection method
	 * @return the numbers found
	 */
	private ArrayList<Integer> getImageNumbersFaceDetection() {
		// Begin
		ArrayList<Integer> foundNumbers = new ArrayList<Integer>();
		this.detectionRunning = true;
		// Search for race bib zone
		ArrayList<Rect> numberZones = this.getPossibleNumberZone();
		for (Rect numberZone : numberZones) {
			// Take the zone and search for possible letters
			Mat numberZoneMat = CVOCR.getMatPart(this.imageGrayMat, numberZone);
			// Check for already present numbers
			ArrayList<Integer> nbs = this.getMatNumbers(numberZoneMat, numberZone);
			for (int nb : nbs) {
				if (!foundNumbers.contains(nb)) {
					foundNumbers.add(nb);
				}
			}
		}
		this.detectionRunning = false;
		return foundNumbers;
	}

	/**
	 * This will detect face and determine zone in the image that could have numbers
	 * @return the list contains all zone that can contains race bib numbers
	 */
	private ArrayList<Rect> getPossibleNumberZone() {
		ArrayList<Rect> numberZones = new ArrayList<Rect>();

		// Detect faces in the image.
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(this.imageMat, faceDetections);

		// For each face, compute the race bib zone
		for (Rect face : faceDetections.toArray()) {
			// Estimate the position
			Rect estimatePosition = new Rect();
			estimatePosition.y = face.y + face.height + face.height / 2;
			estimatePosition.width = face.width * 2;
			estimatePosition.x = face.x - face.width / 2;
			estimatePosition.height = face.height * 3;
			// Add to list
			numberZones.add(estimatePosition);

			// Draw to debug
			if (this.debugEnabled) {
				Imgproc.rectangle(this.imageMat, new Point(face.x, face.y), new Point(face.x + face.width, face.y + face.height), this.colorFace);
				Imgproc.rectangle(this.imageMat, estimatePosition.tl(), estimatePosition.br(), this.colorBody);
			}
		}
		return numberZones;
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
			// For each text zone, cut and try OCR
			for (TextGroup group : textZones) {
				int number = this.getGroupNumber(group, src);
				if (number != -1) {
					if (!foundNumbers.contains(number)) {
						foundNumbers.add(number);
					}
				}
				// Debug draw
				if (this.debugEnabled) {
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
				if (debugEnabled && number != -1) {
					this.saveMat(groupMat, CVOCRSettings.DEBUG_DIRECTORY + File.pathSeparator + this.debugImageCount + "-r" + number + ".png");
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
					if (debugEnabled && nb != -1) {
						this.saveMat(part, CVOCRSettings.DEBUG_DIRECTORY + File.pathSeparator + this.debugImageCount+ ".png");
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
				System.err.println("IOExecption: " + e.getMessage());
			}

			Tesseract tesseractInstance = new Tesseract();
			tesseractInstance.setTessVariable("tessedit_char_whitelist", "0123456789");

			try {
				String recognized = tesseractInstance.doOCR(img);
				recognized = this.cleanText(recognized);
				number = Integer.parseInt(recognized);
			} catch (TesseractException e) {
				System.err.println("TesseractException: " + e.getMessage());			
			} catch (Exception e) {
				System.err.println("Execption: " + e.getMessage());
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
		// Clone to avoid modify
		Mat workingMat = part.clone();
		// Change color
		Imgproc.Canny(workingMat, workingMat, this.paramCannyThresHold, this.paramCannyThresHold);
		// Find the contour
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>(1500);
		Imgproc.findContours(workingMat.clone(), contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		// For each contour, search for possible letter
		for (MatOfPoint contour : contours) {
			Rect contourRect = Imgproc.boundingRect(contour);
			int size = (int) Imgproc.contourArea(contour);
			if (size >= this.paramMinVertices && contourRect.area() > this.paramMinLetterArea && contourRect.area() < this.paramMaxLetterArea && contourRect.height > contourRect.width && contourRect.height < contourRect.width * 4.5 && this.isValidPart(part, contourRect)) {
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

	// TextGroup internClass

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