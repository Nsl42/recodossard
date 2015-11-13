package view;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import controller.Controller;

public class Main{
	
	static Controller myController = new Controller();
	
	public static void main(String args[]) throws Exception{
		
		ArrayList<Integer> res = new ArrayList();
		boolean optionVactive=false;
		if (args.length == 0) {
			System.out.println("Usage : java Main [-h] [--help] [-v] <image file> [<output file>]");
		
		}else if (args.length == 1){
			File file = new File(args[0]);
			if(args[0].equals("-h") || args[0].equals("--help") || args[0].equals("-v")){
				System.out.println("Usage : java Main [-h] [--help] [-v] <image file> [<output file>]");
			}else if (file.exists() & file.isFile()){ //fichier
				res = analyseFile(args[0], optionVactive);
			}else if(file.exists() & file.isDirectory()){ //répertoire
				res = analyseDirectory(args[0], optionVactive, res);
			}else{
				System.out.println("Usage : java Main [-h] [--help] [-v] <image file> [<output file>]");
			}
		
		}else if(args.length == 2){
			File file = new File(args[0]);
			if(args[0].equals("-v")){
				optionVactive=true;
				file = new File(args[1]);
				if(file.exists() & file.isFile()) { // fichier
					res = analyseFile(args[1], optionVactive);
				}else if (file.exists() & file.isDirectory()) { //repertoire
					res = analyseDirectory(args[1], optionVactive, res);
				}else{
					System.out.println("Err: File doesn't exsit");
					System.out.println("Usage : java Main [-h] [--help] [-v] <image file> [<output file>]");
				}
			}else if(file.exists() & file.isFile()) { //fichier
				res=analyseFile(args[0], optionVactive);
			}else if(file.exists() & file.isDirectory()) { //repertoire
				res = analyseDirectory(args[0], optionVactive, res);
			}else{
				System.out.println("Err: File doesn't exsit");
				System.out.println("Usage : java Main [-h] [--help] [-v] <image file> [<output file>]");
			}
		
		}else{
			System.out.println("Usage : java Main [-h] [--help] [-v] <image file> [<output file>]");
		}
		if(!res.isEmpty()){
			for(int i=0; i < res.size(); i++){
				System.out.println(res.get(i));
			}
		}
	}

	public static ArrayList<Integer> analyseFile(String file, boolean optionV) throws Exception{
		boolean valid=true;
		ArrayList<Integer> result = new ArrayList<Integer>();
		try {
			Image image=ImageIO.read(new File(file));
			if (image == null) {
				valid = false;
				System.out.println("The file "+file+" could not be opened , it is not an image");
			}
			// ---------  Call function for OCR
			result = myController.findNumber(file);
			if(optionV){
				System.out.println("File " + file + "done");
			}
		} catch(IOException ex) {
			valid=false;
			System.out.println("The file "+file+" could not be opened , an error occurred.");
		}
		
		return result; // return arrayList with number of bibs 
	}

	public static ArrayList<Integer> analyseDirectory(String directory, boolean optionV, ArrayList<Integer> result){
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
                	res = analyseDirectory(files[i].getAbsolutePath(), optionV, res);
                } else {
                	try {
            			Image image=ImageIO.read(files[i]);
            			if (image == null) {
            				System.out.println("The file "+files[i]+" could not be opened , it is not an image");
            			}
            			// ---------  Call function for OCR
            			if(optionV){
            				System.out.println("File " + files[i] + "done");
            			}
            		} catch(IOException ex) {
            			System.out.println("The file "+files[i]+" could not be opened , an error occurred.");
            		}
                }
            }
        }
		return res;
	}

}

