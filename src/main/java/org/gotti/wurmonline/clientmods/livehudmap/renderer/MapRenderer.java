package org.gotti.wurmonline.clientmods.livehudmap.renderer;

import java.awt.image.BufferedImage;

import com.wurmonline.client.renderer.PickData;

public interface MapRenderer {
	
	/**
	 * Render the map into an image.
	 * 
	 * The map is rendered starting from (x,y) to (x+width,y+height).
	 * 
	 * @param x Left tile
	 * @param y Top tile
	 * @param width Map view width in tiles
	 * @param height Map view height in tiles
	 * @param playerX Player tile x
	 * @param playerY Player tile y
	 * @return
	 */
	BufferedImage createMapDump(int x, int y, int width, int height, int playerX, int playerY);

	/**
	 * Get tooltip information.
	 * @param pickData Tooltip data
	 * @param xMouse mouse position
	 * @param yMouse mouse position
	 * @param width Map view width in tiles
	 * @param height Map view height in tiles
	 * @param playerX Player tile x
	 * @param playerY Player tile y
	 */
	default void pick(PickData pickData, float xMouse, float yMouse, int width, int height, int playerX, int playerY) {
	}
	
}
