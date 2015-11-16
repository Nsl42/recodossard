/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author nsl
 */
public class DataModel {

private ArrayList<HashMap<String, String>> EXIF_VALUES;
private HashMap<String,String> DATA;


/* Business Methods */
	public JsonObject getJSONObject()
	{
			JsonObject ret = Json.createObjectBuilder();
			ret.add("EXIF_VAL", this.EXIF_VALUES);
			ret.add("MORE", this.DATA);
			return ret;
	}

}
