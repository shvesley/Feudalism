/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.chunk.types;

/**
 *
 * @author ZNickq
 */
public class Merchant extends ChunkType{

	@Override
	public String getName() {
		return "Merchant";
	}

	@Override
	public int getInitial() {
		return 10;
	}

	@Override
	public int getIncrease() {
		return 0;
	}
	
}
