package model;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;

public class ImgModel{
    
    private UUID id;
    private String path;
    private ArrayList<Integer> result;
    private BenchData benchData;
    private boolean processed;
    private Settings settings;


    public ImgModel(String pth){
    	Random r = new Random();
	    this.id = new UUID(r.nextLong(), r.nextLong());
	    this.path = pth;
	    this.result = new ArrayList<Integer>();
	    this.benchData = null;
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

	public BenchData getBenchData() {
		return benchData;
	}

	public void seBenchData(BenchData benchData) {
		this.benchData = benchData;
	}

	/* ID, Result and Settings only have getters : One can perform some operations 
	on result or settings by getting the object, and the ID should never be set 
	somewhere else.
	*/
	public UUID getId() {
		return id;
	}

	public ArrayList<Integer> getResult() {
		return result;
	}

	public Settings getSettings() {
		return settings;
	}
    
    
 
	/* Business Methods */
	
	public void process() {
		CVOCR cvocr = new CVOCR(path);
		cvocr.launchDetection(false);
	}

	/**
	 * toJSON() : Returns the object as a JSON string
	 */
	public String toJSON() {
		JsonObject ret = this.getJsonObject();
		return ret.toString();
	}

	/**
	 * toJSON() : Returns the object as a JSONObject 
	 */
	public JsonObject getJsonObject() {
		
		JsonObject ret = (JsonObject) Json.createObjectBuilder();
		JsonObject inside = (JsonObject) Json.createObjectBuilder();
		inside.add(this.path, this.result.toArray());
		inside.add("processed", this.processed);
		// Writing the benchmarking data
		if(benchData != null)
			inside.add("benchData", this.benchData.getJSONObject());
		inside.add("settings", this.settings.getJSONObject());
		ret.add(this.id.toString(), inside);
		return ret;
	}
}