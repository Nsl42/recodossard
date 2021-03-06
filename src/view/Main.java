package view;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import java.io.File;
import java.util.UUID;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Options;


import controller.PhotoListCtrl;
import controller.ProcessingCtrl;

public class Main{
	
	private static ProcessingCtrl processingController = new ProcessingCtrl();
	private static PhotoListCtrl photoListCtrl = new PhotoListCtrl();
	
	private static Options options = new Options();
	private static boolean debugIsEnabled = false;
	private static boolean verboseIsEnabled  = false;
	private static boolean exifIsEnabled  = false;
	private static boolean raceDataIsEnabled = false;
	
	private static void usage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "java Main [-h] [--help] [-v] <image file> [<output file>]"
			, options );
	}
	
	public static void main(String args[]) throws Exception{
		
		options.addOption("h","help",false, "Display the help.");
		options.addOption("v", "verbose", false, "Display more messages.");
		options.addOption("f", "file", true, "Image or directory to analyse.");
		options.addOption("d", "debug", false, "Enable debug mode.");
		options.addOption("e", "exif", false, "Turns on exif analysis.");
		options.addOption("rd", "race-data", false, "Turns on race data analysis.");
		
		File file = null;
		File fileToAnalyse = null;
		
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("h")) {
				usage();
				System.exit(0);
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
			if (line.hasOption("e")) {
				exifIsEnabled = true;
			}
			if (line.hasOption("rd")) {
				raceDataIsEnabled = true;
				fileToAnalyse = new File(line.getOptionValue("rd"));
			}
			
		} catch(MissingArgumentException e) {
			System.err.println("Option <" + e.getOption().getOpt() + "> need an argument!");
			usage();
			System.exit(1);
		} catch(ParseException e) {
			System.out.println("Unexpected exception: " + e.getMessage() );
		}
		
		if (file == null) {
			System.err.println("Please specify a file or a directory with option <f>.");
			usage();
			System.exit(1);
		}
		
		processingController.setProcessSettings(exifIsEnabled, raceDataIsEnabled, debugIsEnabled);
		UUID plID = processingController.acknowledge(file);
		processingController.processing(plID);
		if (raceDataIsEnabled) {
			photoListCtrl.addRaceData(plID, fileToAnalyse);
			photoListCtrl.processAdditionalData(plID);
		}
		if (file.isFile()) {
			System.out.println(processingController.loadedPhotoLists.get(processingController.getPlidFromImgid(plID)).toJSON());
		} else {
			System.out.println(ProcessingCtrl.loadedPhotoLists.get(plID).toJSON());
		}
		
		
	}
}

