/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author nsl
 */
public class Settings {
	
	private boolean EXIF;
	private boolean ADDITONNAL_DATA;
	private boolean BENCH;
	private String ALGO;

	public boolean getEXIF() {
		return EXIF;
	}

	public void setEXIF(boolean EXIF) {
		this.EXIF = EXIF;
	}

	public boolean getADDITONNAL_DATA() {
		return ADDITONNAL_DATA;
	}

	public void setADDITONNAL_DATA(boolean ADDITONNAL_DATA) {
		this.ADDITONNAL_DATA = ADDITONNAL_DATA;
	}

	public boolean getBENCH() {
		return BENCH;
	}

	public void setBENCH(boolean BENCH) {
		this.BENCH = BENCH;
	}

	public String getALGO() {
		return ALGO;
	}

	public void setALGO(String ALGO) {
		this.ALGO = ALGO;
	}
	
	/* Business Methods */
	public JsonObject getJSONObject()
	{
			JsonObject ret = Json.createObjectBuilder();
			ret.add("EXIF", this.EXIF);
			ret.add("ADDITONNAL_DATA", this.ADDITONNAL_DATA);
			ret.add("BENCH", this.BENCH);
			ret.add("ALGO", this.ALGO);
			return ret;
	}
}
