package controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.UUID;
import model.ImgModel;
import model.PhotoList;

public class PhotoListCtrl extends Controller {
	
	public void writelist(UUID photolistid, String pathToJSON) throws Exception{
	
		FileWriter fw = new FileWriter(pathToJSON);
		BufferedWriter out = new BufferedWriter(fw);
		out.write(loadedPhotoLists.get(photolistid).toJSON());
		out.close();
		
	}
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
