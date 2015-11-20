package controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.UUID;
import model.ImgModel;
import model.PhotoList;

public class PhotoListCtrl extends Controller {
	
	/**
	 * writelist given a photoListId and the path to a JSON file, writes
	 * the JSON output of the photolist id at the specified path.
	 * @param photolistid The photoList ID you want
	 * @param pathToJSON The path to the file you want
	 * @throws Exception 
	 */
	public void writelist(UUID photolistid, String pathToJSON) throws Exception{
	
		FileWriter fw = new FileWriter(pathToJSON);
		BufferedWriter out = new BufferedWriter(fw);
		out.write(loadedPhotoLists.get(photolistid).toJSON());
		out.close();
		
	}
	/**
	 * writeimg given a ImgModel id and the path to a JSON file, writes
	 * the JSON output of the imgModel at the specified path.
	 * @param photoid The photo ID you want
	 * @param pathToJSON The path to the file you want
	 * @throws Exception 
	 */
	public void writeimg(UUID photoid, String pathToJSON) throws Exception{
	
		FileWriter fw = new FileWriter(pathToJSON);
		BufferedWriter out = new BufferedWriter(fw);
			for(UUID photolistid : loadedPhotoLists.keySet()){
				ImgModel im = loadedPhotoLists.get(photolistid).getPhoto(photoid) ;
				if(im != null)
					out.write(im.toJSON());
			}

		out.close();
		
	}
	
	/*
	public UUID add(String name) {
		PhotoList pl = new PhotoList(name);
		loadedPhotoLists.put(pl.getId(), pl);
		return  pl.getId();
	}
	*/
	
	public void delete(UUID idPhotoList) {
		loadedPhotoLists.remove(idPhotoList);
	}
	
	public void loadImagesFromJSON(String pathToJSON) {
		
		
	}
	
	public UUID addPhotoToPhotoList (UUID idPhotoList, String photoPath) {
		return loadedPhotoLists.get(idPhotoList).addPhoto(photoPath);
	}
	
	public ArrayList<String> getPhotoPathFromPhotoList(UUID idPhotoList) {
		return null;
	}
	
	
}
