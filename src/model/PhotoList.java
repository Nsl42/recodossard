package model;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
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
	
	public PhotoList(String name) {
		Random r = new Random();
		this.id = new UUID(r.nextLong(), r.nextLong());
		this.name = name;
		this.photolist = new ArrayList<ImgModel>();
		this.sublists = new ArrayList<PhotoList>();
		this.data = new DataModel();
	}
	/* Getters & Setters */
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
	/* Business Methods */
	public UUID addPhoto(String path) {
		//Adding the EXIF data when we have to
		ImgModel np = new ImgModel(path);
		if(Settings.getEXIF())
		{
			File f = new File(path);
			try{

			Metadata meta = ImageMetadataReader.readMetadata(f);
			// obtain the Exif directory
			String date;
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
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("Date", date);
			this.getData().getEXIF_VALUES().add(hm);
			}catch ( Exception e){e.printStackTrace();}
		}
		this.photolist.add(np);
		return np.getId();
	}
	public ImgModel getPhoto(UUID photoId)
	{
		ImgModel ret = null;
		for(ImgModel im : this.getPhotolist())
			if(photoId.equals(im.getId()))
				ret = im;
		return ret;
	}
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
	
	public String toJSON() {
		
		JsonObject ret = this.getJsonObject();
		return ret.toString();
		
	}
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
}