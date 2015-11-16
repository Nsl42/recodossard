package model;

import java.util.ArrayList;
import java.util.UID;
import javax.json.JsonObject;

public class PhotoList{
	
	private UID id;
	private String name;
	private ArrayList<ImgModel> photolist;
	private ArrayList<PhotoList> sublists;
	private Settings settings;
	private DataModel data;
	
	public PhotoList(String name) {
		
		this.id = new UID();
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
	
	public UID getId() {
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
	public boolean contains(id){
		boolean ret = false;
		for(ImgModel im : this.getPhotolist())
		{
			ret = ret || (im.getId().equals(id));
		}
		if(!this.getSublists().isEmpty()))
		for(PhotoList pl : this.getSublists())
			ret = ret || pl.contains(id);
	}
	
	public String toJSON() {
		
		JsonObject ret = this.getJsonObject();
		return ret.toString();
		
	}
	public JsonObject getJsonObject()
	{
		JsonObject ret = Json.createObjectBuilder();
		JsonObject inside = Json.createObjectBuilder();
		inside.add("ID", this.getId());
		inside.add("DataModel", this.getData().getJsonObject());
		// Sublist array creation
		ArrayList<String> subIDS = new ArrayList<String>();
		for(PhotoList pl : this.getSublists())
			subIDS.add(pl.getId().toString());
		inside.add("sublists", subIDS.toArray());
		// Photos array creation
		ArrayList<JsonObject> allPhotos = new ArrayList<JsonObject>();
		for(ImgModel im : this.getPhotolist())
			allPhotos.add(im.getJsonObject());
		inside.add("data", allPhotos.toArray());
		
		ret.add(this.getName(), inside);
		for(PhotoList pl : this.getSublists())
			ret.add(pl.getName(), pl.getJsonObject());
	}
}