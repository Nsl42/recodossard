package model;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.icc.IccDirectory;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import java.util.UUID;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class PhotoList{
	private UUID id;
	private String name;
	private ArrayList<ImgModel> photolist;
	private ArrayList<PhotoList> sublists;
	private Settings settings;
	private DataModel data;
	private RaceData raceData;
	private File f;
	
	public PhotoList(File f) {

		Random r = new Random();
		this.id = new UUID(r.nextLong(), r.nextLong());
		this.name = f.getName();
		this.photolist = new ArrayList<ImgModel>();
		this.sublists = new ArrayList<PhotoList>();
		this.data = new DataModel();
		this.f = f;
	}
	/* Getters & Setters */
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setRaceData(RaceData raceData) {
		this.raceData = raceData;
	}
	
	/* All objects except the name NEVER should be setted with a setter */
	
	public UUID getId() {
		return id;
	}
	
	public ArrayList<ImgModel> getPhotolist() {
		return photolist;
	}
	
	public ArrayList<PhotoList> getSublists() {
		return sublists;
	}
	
	public DataModel getData() {
		return data;
	}

	
	public RaceData getRaceData() {
		return this.raceData;
	}
	
	/* Business Methods */

	public File getFile() {
		return f;
	}
	
	/* Business Methods */
	/**
	 * getPhoto Returns a photo contained in the photoList, given its UUID
	 * @param photoId The ID of the photo to get
	 * @return imgModel a valid ImgModel object, matching the given UUID, 
	 * null if the UUID was not found
	 */
	public ImgModel getPhoto(UUID photoId)
	{
		ImgModel ret = null;
		for(ImgModel im : this.getPhotolist())
			if(photoId.equals(im.getId()))
				ret = im;
		return ret;
	}
	/**
	 * addPhoto Adds the photo corresponding to the File given to the current
	 * photoList
	 * @param File the file object you want to add to this list
	 * @return ImgModel The photo object added, null if the UUID does not correspond
	 */
	public ImgModel addPhoto(File f) {
		//Adding the EXIF data when we have to
		ImgModel np = new ImgModel(f);
		if(Settings.getEXIF())
		{
			try{
			Metadata meta = ImageMetadataReader.readMetadata(f);
			// obtain the Exif directory
			String date;
			String lumi;
			if(meta.containsDirectoryOfType(ExifSubIFDDirectory.class))
			{
				ExifSubIFDDirectory exifdir;
			exifdir = meta.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
			if(exifdir.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL))
				date = exifdir.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
			else
				date = "NO TIME INFO FOUND";

			}else
				date = "NO EXIF DATA FOUND";
			if(meta.containsDirectoryOfType(IccDirectory.class))
			{
				IccDirectory iccdir;
			iccdir = meta.getFirstDirectoryOfType(IccDirectory.class);
			if(iccdir.containsTag(IccDirectory.TAG_TAG_lumi))
				lumi = iccdir.getString(IccDirectory.TAG_TAG_lumi);
			else
				lumi = "NO LUMINOSITY INFO FOUND";

			}else
				lumi = "NO ICC DATA FOUND";
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("Date", date);
			hm.put("lumi", lumi);
			this.getData().getEXIF_VALUES().add(hm);
			}catch ( Exception e){e.printStackTrace();}
		}
		this.photolist.add(np);

		return np;
	}

	
	/**
	 * returns true if the photolist contains the ImgModel refered 
	 * to by the ID
	 * @param id ImgModel ID you want to check
	 * @return true if the photolist contains the object, false if not
	 */
	public boolean contains(UUID id){
		boolean ret = false;
		for(ImgModel im : this.getPhotolist())
		{
			ret = ret || (im.getId().equals(id));
		}
		if(!this.getSublists().isEmpty())
		for(PhotoList pl : this.getSublists())
			ret = ret || pl.contains(id);
		return ret;
	}
	
	/** * Returns the JSON string representing the object
	 * @return JSON string
	 */
	public String toJSON() {
		
		JsonObject ret = this.getJsonObject();
		return ret.toString();
		
	}
	/**
	 * Returns the JsonObject constructed around the object
	 * @return JsonObject
	 */
	public JsonObject getJsonObject()
	{
		JsonObjectBuilder ret =  Json.createObjectBuilder();
		JsonObjectBuilder inside = Json.createObjectBuilder();
		inside.add("ID", this.getId().toString());
		if(Settings.getEXIF())
			inside.add("DataModel", this.getData().getJsonObject());
		// Sublist array creation
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for(PhotoList pl : this.getSublists())
			jab.add(pl.getId().toString());
		inside.add("sublists", jab.build());
		// Photos array creation
		JsonArrayBuilder jabPhotos = Json.createArrayBuilder();
		for(ImgModel im : this.getPhotolist())
			jabPhotos.add(im.getJsonObject());
		inside.add("data", jabPhotos.build());
		
		ret.add(this.getName(), inside);
		for(PhotoList pl : this.getSublists())
			ret.add(pl.getName(), pl.getJsonObject());
		return ret.build();
	}
	
	/**
	 * removeImage removes an image refered by its UUID object
	 * @param idImage the UUID of the image you want to remove
	 */
	public void removeImage(UUID idImage) {
		for (ImgModel im : this.getPhotolist())
		{
			if(im.getId().equals(idImage))
				this.getPhotolist().remove(im);
		}
	}

	/**
	 * ToString override for display purposes only
	 */
	@Override
	public String toString()
	{
		return this.getName();
	}

}