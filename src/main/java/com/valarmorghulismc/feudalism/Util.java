/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.valarmorghulismc.feudalism;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

/**
 *
 * @author ZNickq
 */
public class Util {
	
	public static ChatColor getChatFromDye(DyeColor color) {
		try{
			return ChatColor.valueOf(color.name());
		} catch(Exception ex) {
			if(color == DyeColor.ORANGE) {
				return ChatColor.GOLD;
			}
			if(color == DyeColor.MAGENTA) {
				return ChatColor.LIGHT_PURPLE;
			}
			if(color == DyeColor.LIGHT_BLUE) {
				return ChatColor.BLUE;
			}
			if (color == DyeColor.LIME) {
				return ChatColor.YELLOW;
			}
			if (color == DyeColor.PINK) {
				return ChatColor.RED;
			}
			if(color == DyeColor.SILVER) {
				return ChatColor.GRAY;
			}
			if(color == DyeColor.CYAN) {
				return ChatColor.AQUA;
			}
			if(color == DyeColor.BLUE) {
				return ChatColor.DARK_BLUE;
			}
			if(color == DyeColor.BROWN) {
				return ChatColor.DARK_GRAY;
			}
			return ChatColor.WHITE;
		}
	}
	
}
