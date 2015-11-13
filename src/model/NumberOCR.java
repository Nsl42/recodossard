package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.KNearest;

//import android.util.Log;


/**
 * This class represent the NumberOCR<br>
 * @author Mathieu THEBAUD <math.thebaud@gmail.com>
 * @version 1.00
 */
public class NumberOCR {


	// ============================================ATTRIBUTE(S)============================================
	/**
	 * Size
	 */
	private static final int SAMPLE_WIDTH = 18;
	private static final int SAMPLE_HEIGHT = 22;

	/**
	 * Name of datas files
	 */
	public static final String DATA_FILE_NAME = "datas.bin";
	
	/**
	 * Path to the directory where trained datas are located
	 */
	public static final File TRAINED_DATAS_DIR = new File(Settings.OCR_DATA_PATH + "/ocr/"); 
	
	/**
	 * Path to trained datas
	 */
	public static final String TRAINED_DATAS_PATH = TRAINED_DATAS_DIR.getAbsolutePath()+"/"+DATA_FILE_NAME;
	
	/**
	 * Training directory path
	 */
	public static final String TRAINING_DIRECTORY_PATH = "/ocr/training/";

	/**
	 * The size of all mat to be ocr
	 */
	private static final Size SAMPLE_SIZE = new Size(SAMPLE_WIDTH, SAMPLE_HEIGHT);

	/**
	 * List of answer to improve answer add, save, etc
	 */
	private ArrayList<Integer> listAnswer;

	/**
	 * List of training data, to improve add, save, etc
	 */
	private ArrayList<double[]> listTrainDatas;

	/**
	 * Mat that contains answer
	 */
	private Mat matAnswer;

	/**
	 * Mat that contains train datas
	 */
	private Mat matTrainDatas;

	/**
	 * K-nearest algo.
	 */
	private KNearest kNearestAlgo;

	/**
	 * The threshold of a color to be considered as white
	 */
	private static int paramWhiteThreshold = 150;

	/**
	 * White color as double value
	 */
	private static final double[] WHITE_COLOR = { 255.0 };

	/**
	 * Black color as double value
	 */
	private static final double[] BLACK_COLOR = { 0.0 };
	// ====================================================================================================


	// ===========================================CONSTRUCTOR(S)===========================================
	/**
	 * Just create the list and the empty kNearest.<br>
	 * <strong>Before OCR something, you have to use loadAndTrain()</strong>
	 */
	public NumberOCR() {
		// Create the list
		this.listAnswer = new ArrayList<Integer>(2500);
		this.listTrainDatas = new ArrayList<double[]>(2500);
		this.kNearestAlgo = KNearest.create();
	}
	// ====================================================================================================


	// =============================================GETTER(S)=============================================
	/**
	 * This will load the trained datas and train the kNearest algo.
	 */
	public void loadAndTrain() {
		long s = System.currentTimeMillis();
		// Load
		this.loadTrainedDatas();
		// Convert
		this.convertListInMat();
		// Delete list to allows GC to delete it
		this.listAnswer = null;
		this.listTrainDatas = null;
		// Train
		this.kNearestAlgo.train(matTrainDatas, 0, matAnswer);
	//	Log.i(RecoDossardApplication.LOG_TAG, "[TIME]NumberOCR load and train time : " + (System.currentTimeMillis() - s) + " ms");
	}
	// ====================================================================================================


	// =============================================SETTER(S)=============================================

	// ====================================================================================================


	// =============================================DIGIT OCR=============================================
	/**
	 * To get the number contains in this mat
	 * @param m the mat, must be a binary image with only black and white and as clean as possible (noise, etc...)
	 * @return the number found in the mat, or -1 if didn't found any number
	 */
	public int getNumber(Mat m) {
		int number = -1;
		// Resize
		Imgproc.resize(m, m, SAMPLE_SIZE);
		// Transform
		Mat transform = new Mat(1, NumberOCR.SAMPLE_HEIGHT * NumberOCR.SAMPLE_WIDTH, CvType.CV_32FC1);
		transform.put(0, 0, NumberOCR.getMatValue(m));
		// Apply the knearest algo
		Mat result = new Mat(1, 1, CvType.CV_32FC1);
		this.kNearestAlgo.findNearest(transform, 1, result, new Mat(), new Mat());
		number = (int) result.get(0, 0)[0];
		return number;
	}
	// ====================================================================================================


	// ===========================================NEURAL NETWORK===========================================
	/**
	 * Make the full training : search for directory in TRAINING_DIRECTORY_PATH, load images inside and set the result to the directory name. <br>
	 */
	public void doFullTraining() {
		File[] trainingDirectories = (new File(NumberOCR.TRAINING_DIRECTORY_PATH)).listFiles();
		// For each directory, it correspond a digit
		for (File digitDir : trainingDirectories) {
			int currentDigit = Integer.parseInt(digitDir.getName());
			// For each sample in dir
			File[] samples = digitDir.listFiles();
			for (File sample : samples) {
				// Load mat
				Mat currentTrained = Imgcodecs.imread(sample.getAbsolutePath());
				// Add to list
				this.addAnswer(currentTrained, currentDigit);
			}
		}
	}

	/**
	 * Save all the trained datas into a bin file.<br>
	 * Use bin to improve perf.
	 */
	public void saveTrainedDatas() {
		try {
			DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(NumberOCR.TRAINED_DATAS_PATH)));
			int size = this.listAnswer.size();
			for (int i = 0; i < size; i++) {
				dos.writeInt(this.listAnswer.get(i));
				double[] datas = this.listTrainDatas.get(i);
				for (double data : datas) {
					dos.writeDouble(data);
				}
			}
			dos.close();
		} catch (Exception e) {
			//Log.i(RecoDossardApplication.LOG_TAG, "[ERROR]Exception while writing trained datas : \n\t" + e.getClass() + "\n\t" + e.getMessage());
		}
	}

	/**
	 * Load all the trained datas into a bin file
	 */
	public void loadTrainedDatas() {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(NumberOCR.TRAINED_DATAS_PATH)));
			while (true) {
				int i = dis.readInt();
				this.listAnswer.add(i);
				double[] datas = new double[NumberOCR.SAMPLE_WIDTH * NumberOCR.SAMPLE_HEIGHT];
				for (int index = 0; index < datas.length; index++) {
					datas[index] = dis.readDouble();
				}
				this.listTrainDatas.add(datas);
			}
		} catch (EOFException eof) {
		} catch (Exception e) {
			//Log.i(RecoDossardApplication.LOG_TAG, "[ERROR]Exception while reading trained datas : \n\t" + e.getClass() + "\n\t" + e.getMessage());
		}
	}

	/**
	 * This will add a result to the trained datas.
	 * @param data the mat that contains the data
	 * @param result the result
	 */
	private void addAnswer(Mat data, int result) {
		// Clean and resize
		Imgproc.cvtColor(data, data, Imgproc.COLOR_BGR2GRAY);
		NumberOCR.cleanMat(data);
		Imgproc.resize(data, data, SAMPLE_SIZE);
		// Convert the mat into double[]
		double[] datas = NumberOCR.getMatValue(data);
		// Add the result
		this.listAnswer.add(result);
		this.listTrainDatas.add(datas);
	}

	/**
	 * This will convert the two list into mat to be used in the knearest algo.<br>
	 * We suppose that answer list and train datas list sizes are equals. (<strong>If not, will throw Exception !</strong>)
	 */
	private void convertListInMat() {
		int size = this.listAnswer.size();
		// First, answer list
		this.matAnswer = new Mat(size, 1, CvType.CV_32FC1);
		for (int i = 0; i < size; i++) {
			this.matAnswer.put(i, 0, this.listAnswer.get(i));
		}
		// Then, data list
		this.matTrainDatas = new Mat(size, NumberOCR.SAMPLE_WIDTH * NumberOCR.SAMPLE_HEIGHT, CvType.CV_32FC1);
		for (int i = 0; i < size; i++) {
			this.matTrainDatas.put(i, 0, this.listTrainDatas.get(i));
		}
	}
	// ====================================================================================================


	// ===============================================TOOLS===============================================
	/**
	 * To get the double tab that represent the mat value.<br>
	 * The mat must be of sample or the function will throw exception or errors.
	 * @param m the mat to convert in double
	 * @return a double table that contains the mat red pixel value for each column in each row. We use just the red pixel values because the image are just black or white so the pixel will just take 0 or 255.
	 */
	public static double[] getMatValue(Mat m) {
		double[] datas = new double[NumberOCR.SAMPLE_WIDTH * NumberOCR.SAMPLE_HEIGHT];
		int c = 0;
		double v = 0;
		for (int y = 0; y < NumberOCR.SAMPLE_HEIGHT; y++) {
			for (int x = 0; x < NumberOCR.SAMPLE_WIDTH; x++) {
				// Get the first pixel, it will be just 0 or 255 (black or white)
				v = m.get(y, x)[0];
				if (v != 0)
					v = 1;
				datas[c] = v;
				c++;
			}
		}
		return datas;
	}

	/**
	 * This will clean a mat as the number finder do.<br>
	 * This can be useful to save trained data as they will be possible used after number finder work.<br>
	 * We consider that the m is in COLOR_BGR2GRAY
	 * @param m the mat to clean
	 */
	private static void cleanMat(Mat m) {
		int mx = m.width();
		int my = m.height();
		// Change all pixel to black or white
		double color;
		for (int x = 0; x < mx; x++) {
			for (int y = 0; y < my; y++) {
				color = m.get(y, x)[0];
				// If considered as white pixel
				if (color > NumberOCR.paramWhiteThreshold) {
					m.put(y, x, NumberOCR.WHITE_COLOR);
				} else {
					m.put(y, x, NumberOCR.BLACK_COLOR);
				}
			}
		}
	}
	// ====================================================================================================
}