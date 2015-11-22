package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ImgModel{
    
    private UUID id;
    private String path;
    private ArrayList<Integer> result;
    private boolean processed;
    private HashMap<String, String> exif;


    public ImgModel(File f){
    	Random r = new Random();
	    this.id = new UUID(r.nextLong(), r.nextLong());
	    this.path = f.getPath();
	    this.result = new ArrayList<Integer>();
    }

    
    /* Getters & Setters */
    
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}


	/* ID, and Result only have getters : One can perform some operations 
	on result by getting the object, and the ID should never be set 
	somewhere else.
	*/
	public UUID getId() {
		return id;
	}

	public ArrayList<Integer> getResult() {
		return result;
	}
    
    
 
	/* Business Methods */
	/**
	 * Returns the object as a JSON string
	 * @return the JsonString representing the ImgModel object
	 */
	public String toJSON() {
		JsonObject ret = this.getJsonObject();
		return ret.toString();
	}

	/**
	 * Returns the object as a JSONObject 
	 * @return JsonObject the object ImgModel as JsonObject
	 */
	public JsonObject getJsonObject() {
		
		JsonObjectBuilder inside = Json.createObjectBuilder();
		//Number Array Builder
		JsonArrayBuilder jab = Json.createArrayBuilder();

		int[] results = new int[this.result.size()];
		for(int i = 0; i < this.result.size(); i++)
		{
			results[i] = this.result.get(i);
			jab.add(results[i]);
		}
		JsonArray numbers;
	    numbers = jab.build();
		//Adding path, results, processed...
		inside.add(this.path, numbers).
		add("processed", this.processed);
		}

		JsonObject ret = Json.createObjectBuilder()
			.add(this.id.toString(), inside).build();
		return ret;
	}
}