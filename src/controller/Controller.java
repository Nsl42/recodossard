package controller;

import java.io.File;
import java.util.ArrayList;

import model.NumberFinder;

public class Controller {
	
	private NumberFinder nf = new NumberFinder();
    
    public ArrayList<Integer> findNumber(String filepath) throws Exception {
    	ArrayList<Integer> results = nf.launchDetection(filepath);
    	return results;
    }
}
