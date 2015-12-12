package org.gotti.wurmonline.clientmods.livehudmap.renderer;

import java.awt.image.BufferedImage;

import com.wurmonline.client.game.CaveDataBuffer;
import com.wurmonline.mesh.Tiles.Tile;

public abstract class AbstractCaveRenderer implements MapRenderer {

	protected static final float MAP_HEIGHT = 1000;

	public abstract BufferedImage createMapDump(int xo, int yo, int lWidth, int lHeight, int px, int py);

	protected final CaveDataBuffer buffer;

	protected final int mapWidth;

	protected final int mapHeight;

	protected final int size;

	public AbstractCaveRenderer(CaveDataBuffer buffer, int mapWidth, int mapHeight, int size) {
		this.buffer = buffer;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.size = size;
	}

	protected short getHeight(int x, int y) {
		int sum = buffer.getRawFloor(x, y) +
				buffer.getRawFloor(x + 1, y) +
				buffer.getRawFloor(x, y + 1) +
				buffer.getRawFloor(x + 1, y + 1);
		return (short) (sum / 4);
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
