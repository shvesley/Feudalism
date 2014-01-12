/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.house;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author ZNickq
 */
public class Treasury {
	private int totalMoney;
	private int htp;

	public int getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(int totalMoney) {
		this.totalMoney = totalMoney;
	}
	
	public void addMoney(int amount) {
		totalMoney += amount;
	}
	
	public void remMoney(int amount) {
		totalMoney -= amount;
	}

	public int getHtp() {
		return htp;
	}

	public void setHtp(int htp) {
		this.htp = htp;
	}

	public void save(DataOutputStream dos) throws IOException {
		dos.writeInt(totalMoney);
		dos.writeInt(htp);
	}

	public void load(DataInputStream dis) throws IOException {
		totalMoney = dis.readInt();
		htp = dis.readInt();
	}
	
}
