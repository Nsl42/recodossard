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
    
}
