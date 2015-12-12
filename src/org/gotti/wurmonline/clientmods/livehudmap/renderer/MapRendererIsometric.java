package org.gotti.wurmonline.clientmods.livehudmap.renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.wurmonline.client.game.NearTerrainDataBuffer;
import com.wurmonline.mesh.Tiles.Tile;

public class MapRendererIsometric extends AbstractSurfaceRenderer {
	
	public MapRendererIsometric(NearTerrainDataBuffer buffer, int mapWidth, int mapHeight, int size) {
		super(buffer, mapWidth, mapHeight, size);
	}
	
    public BufferedImage createMapDump(int xo, int yo, int lWidth, int lHeight, int px, int py) {
        if (yo < 0)
            yo = 0;
        if (xo < 0)
            xo = 0;

        final BufferedImage bi2 = new BufferedImage(lWidth, lHeight, BufferedImage.TYPE_INT_RGB);
        final float[] data = new float[lWidth * lHeight * 3];
        
        for (int x = 0; x < lWidth; x++) {
            int alt = lWidth - 1;
            for (int y = lHeight - 1; y >= 0; y--) {
                float node = (float) (getSurfaceHeight(x + xo, y + yo) / (Short.MAX_VALUE / 3.3f));
                float node2 = x == lWidth - 1 || y == lHeight - 1 ? node : (float) (getSurfaceHeight(x + 1 + xo, y + 1 + yo) / (Short.MAX_VALUE / 3.3f));


                final float hh = node;

                float h = ((node2 - node) * 1500) / 256.0f * getWidth() / 128 + hh / 2 + 1.0f;
                h *= 0.4f;

                float r = h;
                float g = h;
                float b = h;

                final Tile tile = getTileType(x + xo, y + yo);
                final Color color;
                if (tile != null) {
                    color = tile.getColor();
                }
                else {
                    color = Tile.TILE_DIRT.getColor();
                }
                r *= (color.getRed() / 255.0f) * 2;
                g *= (color.getGreen() / 255.0f) * 2;
                b *= (color.getBlue() / 255.0f) * 2;

                if (r < 0)
                    r = 0;
                if (r > 1)
                    r = 1;
                if (g < 0)
                    g = 0;
                if (g > 1)
                    g = 1;
                if (b < 0)
                    b = 0;
                if (b > 1)
                    b = 1;

                if (node < 0) {
                    r = r * 0.2f + 0.4f * 0.4f;
                    g = g * 0.2f + 0.5f * 0.4f;
                    b = b * 0.2f + 1.0f * 0.4f;
                }

				if (px == x + xo && py == y + yo) {
					r = Color.RED.getRed();
					g = 0;
					b = 0;
				}
                
                final int altTarget = y - (int) (getSurfaceHeight(x, y) * MAP_HEIGHT / 4  / (Short.MAX_VALUE / 3.3f));
                while (alt > altTarget && alt >= 0) {
                    data[(x + alt * lWidth) * 3 + 0] = r * 255;
                    data[(x + alt * lWidth) * 3 + 1] = g * 255;
                    data[(x + alt * lWidth) * 3 + 2] = b * 255;
                    alt--;
                }
            }
        }

        bi2.getRaster().setPixels(0, 0, lWidth, lHeight, data);
        return bi2;
    }
	

}
