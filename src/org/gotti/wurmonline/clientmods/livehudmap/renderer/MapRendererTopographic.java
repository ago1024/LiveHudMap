package org.gotti.wurmonline.clientmods.livehudmap.renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.wurmonline.client.game.NearTerrainDataBuffer;
import com.wurmonline.mesh.Tiles.Tile;

public class MapRendererTopographic extends AbstractSurfaceRenderer {
	private short interval;

	public MapRendererTopographic(NearTerrainDataBuffer buffer) {
		super(buffer);
		this.interval = 250;
	}

	@Override
	public BufferedImage createMapDump(int xo, int yo, int lWidth, int lHeight, int px, int py) {
		if (yo < 0)
			yo = 0;
		if (xo < 0)
			xo = 0;

		final BufferedImage bi2 = new BufferedImage(lWidth, lWidth, BufferedImage.TYPE_INT_RGB);
		final float[] data = new float[lWidth * lWidth * 3];

		for (int x = 0; x < lWidth; x++) {
			for (int y = lWidth - 1; y >= 0; y--) {
				final short height = getSurfaceHeight(x + xo, y + yo);
				final short nearHeightNX = x == 0 ? height : getSurfaceHeight(x + xo - 1, y + yo);
				final short nearHeightNY = y == 0 ? height : getSurfaceHeight(x + xo, y + yo - 1);
				final short nearHeightX = x == lWidth - 1 ? height : getSurfaceHeight(x + xo + 1, y + yo);
				final short nearHeightY = y == lWidth - 1 ? height : getSurfaceHeight(x + xo, y + yo + 1);
				boolean isControur = checkContourLine(height, nearHeightNX, interval)
						|| checkContourLine(height, nearHeightNY, interval)
						|| checkContourLine(height, nearHeightX, interval)
						|| checkContourLine(height, nearHeightY, interval);

				final Tile tile = getTileType(x + xo, y + yo);
				final Color color;
				if (tile != null) {
					color = tile.getColor();
				}
				else {
					color = Tile.TILE_DIRT.getColor();
				}
				int r = color.getRed();
				int g = color.getGreen();
				int b = color.getBlue();
				if (isControur) {
					r = 0;
					g = 0;
					b = 0;
				}
				else if (height < 0) {
					r = (int) (r * 0.2f + 0.4f * 0.4f * 256f);
					g = (int) (g * 0.2f + 0.5f * 0.4f * 256f);
					b = (int) (b * 0.2f + 1.0f * 0.4f * 256f);
				}

				if (px == x + xo && py == y + yo) {
					r = Color.RED.getRed();
					g = 0;
					b = 0;
				}

				data[(x + y * lWidth) * 3 + 0] = r;
				data[(x + y * lWidth) * 3 + 1] = g;
				data[(x + y * lWidth) * 3 + 2] = b;
			}
		}

		bi2.getRaster().setPixels(0, 0, lWidth, lWidth, data);
		return bi2;
	}

	private boolean checkContourLine(short h0, short h1, short interval) {
		if (h0 == h1) {
			return false;
		}
		for (int i = h0; i <= h1; i++) {
			if (i % interval == 0) {
				return true;
			}
		}
		return false;
	}

}
