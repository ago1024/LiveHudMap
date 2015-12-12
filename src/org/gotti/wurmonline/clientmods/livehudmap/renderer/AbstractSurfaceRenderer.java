package org.gotti.wurmonline.clientmods.livehudmap.renderer;

import java.awt.image.BufferedImage;

import com.wurmonline.client.game.NearTerrainDataBuffer;
import com.wurmonline.mesh.Tiles.Tile;

public abstract class AbstractSurfaceRenderer implements MapRenderer {

	protected static final float MAP_HEIGHT = 1000;

	public abstract BufferedImage createMapDump(int xo, int yo, int lWidth, int lHeight, int px, int py);

	protected final NearTerrainDataBuffer buffer;
	protected final int mapWidth;
	protected final int mapHeight;
	protected final int size;


	public AbstractSurfaceRenderer(NearTerrainDataBuffer buffer, int mapWidth, int mapHeight, int size) {
		this.buffer = buffer;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.size = size;
	}

	protected short getSurfaceHeight(int x, int y) {
		return (short) (buffer.getHeight(x, y) * 10);
	}

	protected Tile getTileType(int x, int y) {
		return buffer.getTileType(x, y);
	}
	
	protected int getWidth() {
		return mapWidth;
	}

	protected int getHeight() {
		return mapHeight;
	}

}
