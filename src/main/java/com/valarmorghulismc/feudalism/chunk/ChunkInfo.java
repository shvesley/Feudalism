/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism.chunk;

import com.valarmorghulismc.feudalism.Feudalism;
import com.valarmorghulismc.feudalism.chunk.types.ChunkType;
import com.valarmorghulismc.feudalism.player.FeudalPlayer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ZNickq
 */
public class ChunkInfo {
	private static Map<ChunkLocation, ChunkInfo> allChunks = new HashMap<ChunkLocation, ChunkInfo>();

	public static Map<ChunkLocation, ChunkInfo> getAllChunks() {
		return Collections.unmodifiableMap(allChunks);
	}

	public static void save(File dataFolder) throws FileNotFoundException, IOException {
		File saveFile = new File(dataFolder, "chunkinfo.dat");
		if(saveFile.exists()) {
			saveFile.delete();
		}
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(saveFile));
		dos.writeInt(Feudalism.CURRENT_VERSION);
		dos.writeInt(allChunks.size());
		for(ChunkLocation cl : allChunks.keySet()) {
			ChunkInfo cinfo = allChunks.get(cl);
			dos.writeUTF(cl.getWorld());
			dos.writeInt(cl.getX());
			dos.writeInt(cl.getZ());
			dos.writeBoolean(cinfo.isOnRent());
			dos.writeBoolean(cinfo.isOnSale());
			dos.writeInt(cinfo.getSalePrice());
			dos.writeInt(cinfo.getShareState());
			dos.writeUTF(cinfo.getRenter() == null ? "[null]" : cinfo.getRenter());
			dos.writeUTF(cinfo.getOwner() == null ? "[null]" : cinfo.getOwner());
			dos.writeUTF(cinfo.getType() == null ? "[null]" : cinfo.getType().getName());
		}
		dos.flush();
		dos.close();
		
	}

	public static void load(File dataFolder) throws FileNotFoundException, IOException {
		File loadFile = new File(dataFolder, "chunkinfo.dat");
		if(!(loadFile.exists())) {
			return;
		}
		DataInputStream dis = new DataInputStream(new FileInputStream(loadFile));
		int version = dis.readInt();
		int howMany = dis.readInt();
		for(int i=1;i<=howMany;i++) {
			ChunkInfo cii = new ChunkInfo();
			String w = dis.readUTF();
			int x = dis.readInt();
			int z = dis.readInt();
			ChunkLocation cll = new ChunkLocation(w, x, z);
			cii.setOnRent(dis.readBoolean());
			cii.setOnSale(dis.readBoolean());
			cii.setSalePrice(dis.readInt());
			cii.setShareState(dis.readInt());
			String renter = dis.readUTF();
			String owner = dis.readUTF();
			String chunkType = dis.readUTF();
			if(!(renter.equals("[null]"))) {
				cii.setRenter(renter);
			}
			if(!(owner.equals("[null]"))) {
				cii.setOwner(owner);
			}
			if(!(chunkType.equals("[null]"))) {
				cii.setType(ChunkType.getByName(chunkType));
			}
			allChunks.put(cll, cii);
		}
		dis.close();
	}
	
	private boolean onRent, onSale;
	private int salePrice, shareState = 1;
	private String renter, owner;
	private ChunkType type;
	
	public static ChunkInfo getChunkInfo(ChunkLocation ch) {
		ChunkInfo toRet = allChunks.get(ch);
		if(toRet == null) {
			toRet = new ChunkInfo();
			allChunks.put(ch, toRet);
		}
		return toRet;
	}
	
	private ChunkInfo() {
	}
	
	public ChunkType getType() {
		return type;
	}
	
	public void setType(ChunkType newType) {
		type = newType;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public boolean isOnRent() {
		return onRent;
	}

	public void setOnRent(boolean onRent) {
		this.onRent = onRent;
	}
	
	public int getShareState() {
		return shareState;
	}
	
	public void setShareState(int nS) {
		shareState = nS;
	}
	
	public boolean isOnSale() {
		return onSale;
	}
	
	public void setOnSale(boolean onSale) {
		this.onSale = onSale;
	}

	public String getRenter() {
		return renter;
	}

	public void setRenter(String renter) {
		this.renter = renter;
	}
	
	public void setSalePrice(int nS) {
		salePrice = nS;
	}
	
	public int getSalePrice() {
		return salePrice;
	}
	
	public int getRentPrice() {
		FeudalPlayer fplr = FeudalPlayer.getPlayer(renter, false);
		return fplr.getRentPrice();
	}

	public String getShareStateName() {
		if(shareState == 0) {
			return "private";
		} 
		if(shareState == 1) {
			return "house";
		}
		if(shareState == 2) {
			return "all";
		}
		return "";
		
	}
	
}
