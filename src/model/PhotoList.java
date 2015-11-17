package model;

import java.util.ArrayList;
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
		ImgModel np = new ImgModel(path);
		this.photolist.add(np);
		return np.getId();
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