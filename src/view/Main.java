package view;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import javax.imageio.ImageIO;

import controller.PhotoListCtrl;
import controller.ProcessingCtrl;

public class Main{

	private static PhotoListCtrl photoListController = new PhotoListCtrl();
	private static ProcessingCtrl processingController = new ProcessingCtrl();

	private static boolean debugIsEnabled = false;
	private static boolean verboseIsEnabled  = false;
	
	private static void usage() {
		System.out.println("Usage : java Main [-h] [--help] [-v] <image file> [<output file>]");
	}

	public static void main(String args[]) throws Exception{

		ArrayList<Integer> res = new ArrayList();

		if (args.length == 0) {
			usage();
		} else if (args.length == 1) {
			File file = new File(args[0]);
			if(args[0].equals("-h") || args[0].equals("--help") || args[0].equals("-v")){
				usage();
			}else if (file.exists() & file.isFile()){ //fichier
				res = analyseFile(args[0]);
			}else if(file.exists() & file.isDirectory()){ //répertoire
				res = analyseDirectory(args[0], res);
			}else{
				usage();
			}

		}else if(args.length == 2){
			File file = new File(args[0]);
			if(args[0].equals("-v")){
				verboseIsEnabled = true;
				file = new File(args[1]);
				if(file.exists() & file.isFile()) { // fichier
					res = analyseFile(args[1]);
				}else if (file.exists() & file.isDirectory()) { //repertoire
					res = analyseDirectory(args[1], res);
				}else{
					System.err.println("Err: File doesn't exsit");
					usage();
				}
			}else if(file.exists() & file.isFile()) { //fichier
				res=analyseFile(args[0]);
			}else if(file.exists() & file.isDirectory()) { //repertoire
				res = analyseDirectory(args[0], res);
			}else{
				System.err.println("Err: File doesn't exsit");
				usage();
			}

		}else{
			usage();
		}
		if(!res.isEmpty()){
			for(int i=0; i < res.size(); i++){
				System.out.println(res.get(i));
			}
		}
	}
	
	
	public static ArrayList<Integer> analyseFile(String file) throws Exception{
		boolean valid=true;
		ArrayList<Integer> result = new ArrayList<Integer>();
		try {
			Image image=ImageIO.read(new File(file));
			if (image == null) {
				valid = false;
				System.out.println("The file "+file+" could not be opened , it is not an image");
			}
			// ---------  Call function for OCR

			UUID galleryId = photoListController.add("dummyGallery");
			UUID imgId = photoListController.addPhotoToPhotoList(galleryId, file);
			String results = processingController.imgProcessing(imgId);
			System.out.println(results);

			if(verboseIsEnabled){
				System.out.println("File " + file + " done");
			}
		} catch(IOException ex) {
			valid=false;
			System.out.println("The file "+file+" could not be opened , an error occurred.");
		}

		return result; // return arrayList with number of bibs 
	}

	public static ArrayList<Integer> analyseDirectory(String directory, ArrayList<Integer> result){
		File file = new File(directory);
		File[] files = file.listFiles();
		ArrayList<Integer> res;
		if (result.isEmpty()){
			res = new ArrayList();
		}else {
			res = result;
		}
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory() == true) {
					res = analyseDirectory(files[i].getAbsolutePath(), res);
				} else {
					try {
						Image image=ImageIO.read(files[i]);
						if (image == null) {
							System.err.println("The file "+files[i]+" could not be opened , it is not an image");
						}
						// ---------  Call function for OCR
						if(verboseIsEnabled){
							System.out.println("File " + files[i] + " done");
						}
					} catch(IOException ex) {
						System.err.println("The file "+files[i]+" could not be opened , an error occurred.");
					}
				}
			}
		}
		return res;
	}
}

