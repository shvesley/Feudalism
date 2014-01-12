/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.chunk.types;

/**
 *
 * @author ZNickq
 */
public class Farm extends ChunkType{

	@Override
	public String getName() {
		return "Farm";
	}

	@Override
	public int getInitial() {
		return 5;
	}

	@Override
	public int getIncrease() {
		return 2;
	}
	
}
