/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author nsl
 */
class BenchData {
	private int procTime;
	private boolean fauxPositif;
	private boolean fauxNegatif;

	public int getProcTime() {
		return procTime;
	}

	public void setProcTime(int procTime) {
		this.procTime = procTime;
	}

	public boolean isFauxPositif() {
		return fauxPositif;
	}

	public void setFauxPositif(boolean fauxPositif) {
		this.fauxPositif = fauxPositif;
	}

	public boolean isFauxNegatif() {
		return fauxNegatif;
	}

	public void setFauxNegatif(boolean fauxNegatif) {
		this.fauxNegatif = fauxNegatif;
	}
    
	/* Business Methods */
	public JsonObject getJSONObject()
	{
			JsonObject ret = Json.createObjectBuilder()
			.add("ProcTime", this.getProcTime())
			.add("FauxPositif", this.isFauxPositif())
			.add("FauxNegatif", this.isFauxNegatif()).build();
			return ret;
	}
}
