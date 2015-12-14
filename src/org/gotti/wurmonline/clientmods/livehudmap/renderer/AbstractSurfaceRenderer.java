package org.gotti.wurmonline.clientmods.livehudmap.renderer;

import java.awt.image.BufferedImage;

import com.wurmonline.client.game.NearTerrainDataBuffer;
import com.wurmonline.mesh.Tiles.Tile;

public abstract class AbstractSurfaceRenderer implements MapRenderer {

	protected static final float MAP_HEIGHT = 1000;

	public abstract BufferedImage createMapDump(int xo, int yo, int lWidth, int lHeight, int px, int py);

	protected final NearTerrainDataBuffer buffer;

	public AbstractSurfaceRenderer(NearTerrainDataBuffer buffer) {
		this.buffer = buffer;
	}

	protected short getSurfaceHeight(int x, int y) {
		return (short) (buffer.getHeight(x, y) * 10);
	}

	protected Tile getTileType(int x, int y) {
		return buffer.getTileType(x, y);
	}
}
