/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author nsl
 */
public class DataModel {

private ArrayList<HashMap<String, String>> EXIF_VALUES;
private HashMap<String,String> DATA;


/* Business Methods */
	public JsonObject getJsonObject()
	{
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for(HashMap<String,String> hm : EXIF_VALUES)
		{
			JsonObjectBuilder job = Json.createObjectBuilder();
			for(String hmks : hm.keySet())
				job.add(hmks, hm.get(hmks));
			jab.add(job);
		}
		JsonObjectBuilder jobData = Json.createObjectBuilder();
			for(String hmks : DATA.keySet())
				jobData.add(hmks, DATA.get(hmks));
	
			JsonObject ret = Json.createObjectBuilder().
				add("EXIF_VAL", jab).
				add("MORE", jobData).build();
			return ret;
	}

}
