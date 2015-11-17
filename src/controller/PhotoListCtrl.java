package controller;

import java.util.ArrayList;
import java.util.UUID;
import model.PhotoList;

public class PhotoListCtrl extends Controller {
	
	public void write(String name, String pathToJSON) {}
	
	public UUID add(String name, ArrayList<String> photoPat) {
		PhotoList pl = new PhotoList(name);
		loadedPhotoLists.put(pl.getId(), pl);
		return  pl.getId();
	}
	
	public void delete(UUID idPhotoList) {
		loadedPhotoLists.remove(idPhotoList);
	}
	
	public void loadImagesFromJSON(String pathToJSON) {
		
		
	}
	
	public UUID addPhotoToPhotoList (UUID idPhotoList, String photoPath) {
		PhotoList pl = loadedPhotoLists.get(idPhotoList);
		UUID photoID = pl.addPhoto(photoPath);
		return photoID;
	}
	
	public ArrayList<String> getPhotoPathFromPhotoList(UUID idPhotoList) {
		return null;
	}
	
	
}
