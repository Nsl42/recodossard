package view;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Options;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import controller.PhotoListCtrl;
import controller.ProcessingCtrl;

public class Main{

	private static PhotoListCtrl photoListController = new PhotoListCtrl();
	private static ProcessingCtrl processingController = new ProcessingCtrl();

	private static Options options = new Options();
	private static boolean debugIsEnabled = false;
	private static boolean verboseIsEnabled  = false;
	private static boolean benchmarkIsEnabled = false;

	private static void usage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "java Main [-h] [--help] [-v] <image file> [<output file>]"
				, options );
	}

	public static void main(String args[]) throws Exception{

		options.addOption("h","help",false, "Display the help.");
		options.addOption("v", "verbose", false, "Display more messages.");
		options.addOption("f", "file", true, "Image or directory to analyse.");
		options.addOption("d", "debug", false, "Image or directory to analyse.");

		File file = null;

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("h")) {
				usage();
			}
			if (line.hasOption("v")) {
				verboseIsEnabled = true;
			}
			if (line.hasOption("f")) {
				file = new File(line.getOptionValue("f"));
			}
			if (line.hasOption("d")) {
				debugIsEnabled = true;
			}

		} catch(MissingArgumentException e) {
			System.err.println("Option <" + e.getOption().getOpt() + "> need an argument!");
			usage();
			System.exit(1);
		} catch(ParseException e) {
			System.out.println( "Unexpected exception:" + e.getMessage() );
		}
		
		processingController.setProcessSettings(false, false, benchmarkIsEnabled, debugIsEnabled);

		if (file.isDirectory()) {
			ArrayList<String> res = analyseDirectory(file.getAbsolutePath());
			System.out.println(res.toString());			
		} else {
			String res = analyseFile(file);
			System.out.println(res);
		}
	}

	public static String analyseFile(File file) {
		String result = null;
		try {
			Image image=ImageIO.read(file);
			if (image == null) {
				System.err.println("The file "+file.getAbsolutePath()+" could not be opened , it is not an image");
			}

			UUID galleryId = photoListController.add("dummyGallery");
			UUID imgId = photoListController.addPhotoToPhotoList(galleryId, file.getAbsolutePath());
			result = processingController.imgProcessing(imgId);

			if(verboseIsEnabled){
				System.out.println("File " + file + " done");
			}
		} catch(IOException ex) {
			System.err.println("The file "+file+" could not be opened , an error occurred.");
		}

		return result; // return arrayList with number of bibs 
	}


	public static ArrayList<String> analyseDirectory(String directory){
		File file = new File(directory);
		File[] files = file.listFiles();
		ArrayList<String> results = new ArrayList<String>();

		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory() == true) {
					results.addAll(analyseDirectory(files[i].getAbsolutePath()));
				} else {
					results.add(analyseFile(files[i]));
				}
			}
		}
		return results;
	}
}

