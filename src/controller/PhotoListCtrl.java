package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.UUID;


import model.ImgModel;
import model.PhotoList;
import model.RaceData;

public class PhotoListCtrl extends Controller {
	
	/**
	 * writelist given a photoListId and the path to a JSON file, writes
	 * the JSON output of the photolist id at the specified path.
	 * @author Anas Alaoui M'Darhri
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
	 * @author Anas Alaoui M'Darhri
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
		PhotoList pl = new PhotoList(name); loadedPhotoLists.put(pl.getId(), pl);
		return  pl.getId();
	}
	*/
	

	/**
	 * Deletes the photoList corresponding to the UUID given 
	 * @author Anas Alaoui M'Darhri
	 * @param idPhotoList the ID of the photolist you want to remove
	 */
	public void delete(UUID idPhotoList) {
		loadedPhotoLists.remove(idPhotoList);
	}
	public void deleteImage(UUID idPhotoList, UUID idImage) {
		loadedPhotoLists.get(idPhotoList).removeImage(idImage);
	}
	
	/**
	 * Reads from a JSON file, and adds the items to the acknowledged content
	 * @author Anas Alaoui M'Darhri
	 * @param pathToJSON path to a JSON file
	 */
	public void loadImagesFromJSON(String pathToJSON) {
	}
		
	
	/**
	 * Adds a photo to a PhotoList 
	 * @author Anas Alaoui M'Darhri
	 * @param idPhotoList The UUID of a PhotoList you want
	 * @param f The file object of a image you want to add to the photoList
	 * @return the object ImgModel corresponding to the UUID you've given
	 */
	public ImgModel acknowledgeToPhotoList (UUID idPhotoList, File f) {
		return loadedPhotoLists.get(idPhotoList).addPhoto(f);
	}
	
	/**
	 * Add and parse a file with data on the race (bibs list, contestant 
	 * name,...).
	 * @author Anas Alaoui M'Darhri
	 * @param idPhotoList - ID of the photolist to add.
	 * @param file - file to parse.
	 */
	public void addRaceData(UUID idPhotoList, File file) {
		RaceData raceData = new RaceData();
		raceData.parseDataRaceFile(file);
		loadedPhotoLists.get(idPhotoList).setRaceData(raceData);
	}
	
	/**
	 * Process advanced analysis.
	 * @author Anas Alaoui M'Darhri
	 * @param idPhotoList - photo list to process.
	 */
	public void processAdditionalData(UUID idPhotoList) {
		PhotoList pl = loadedPhotoLists.get(idPhotoList);
		if (pl != null) {
			removeFalsePositiveResult(pl);
		}
	}
	
	/**
	 * Remove bibs detected that are not present in the race data.
	 * @author Anas Alaoui M'Darhri
	 * @param idPhotoList 
	 */
	private void removeFalsePositiveResult(PhotoList pl) {
		RaceData raceData = pl.getRaceData();
		Iterator<Integer> i = null;
		if (raceData != null) {
			for (ImgModel photo : pl.getPhotolist()) {
				i = photo.getResult().iterator() ;
				while(i.hasNext()) {
					// Detect false positive.
					if (!raceData.getContestantList().containsKey(i.next())) {
						i.remove();
					}
				}
			}
		}
	}
	
	
}
