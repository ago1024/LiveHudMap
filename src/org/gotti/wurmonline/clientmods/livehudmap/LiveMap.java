package org.gotti.wurmonline.clientmods.livehudmap;

import java.awt.image.BufferedImage;

import org.gotti.wurmonline.clientmods.livehudmap.renderer.AbstractSurfaceRenderer;
import org.gotti.wurmonline.clientmods.livehudmap.renderer.MapRendererCave;
import org.gotti.wurmonline.clientmods.livehudmap.renderer.MapRendererFlat;
import org.lwjgl.opengl.GL11;

import com.wurmonline.client.game.PlayerPosition;
import com.wurmonline.client.game.World;
import com.wurmonline.client.resources.textures.ImageTexture;
import com.wurmonline.client.resources.textures.ImageTextureLoader;

public class LiveMap {

	private int size;
	private int mapsize;
	private final World world;
	private final AbstractSurfaceRenderer surfaceRenderer;
	private final MapRendererCave caveRenderer;

	private boolean dirty = true;
	private BufferedImage image;
	private ImageTexture texture;

	private float[] textureCoord = { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F };
	private int x;
	private int y;
	private int cavemapsize;
	
	public LiveMap(World world, int size, int mapsize) {
		this.size = size;
		this.mapsize = mapsize;
		this.cavemapsize = 32;
		this.world = world;
		this.surfaceRenderer = new MapRendererFlat(world.getNearTerrainBuffer(), 0x1000, 0x1000, this.mapsize);
		this.caveRenderer = new MapRendererCave(world.getCaveBuffer(), 0x1000, 0x1000, this.cavemapsize);
	}

	public void update(int windowX, int windowY) {
		x = windowX + 3;
		y = windowY + 21;
		if (dirty) {
			PlayerPosition pos = world.getPlayer().getPos();
			
			if (pos.getLayer() >= 0)
				image = surfaceRenderer.createMapDump(pos.getTileX() - mapsize / 2, pos.getTileY() - mapsize / 2, mapsize, mapsize, pos.getTileX(), pos.getTileY());
			else
				image = caveRenderer.createMapDump(pos.getTileX() - cavemapsize / 2, pos.getTileY() - cavemapsize / 2, cavemapsize, cavemapsize, pos.getTileX(), pos.getTileY());
			texture = ImageTextureLoader.loadNowrapNearestTexture(image);
		}
	}
	
	protected void gameTick(int windowX, int windowY, int textureX, int textureY, float textureScale) {
	}
	

	public void render(float textureX, float textureY, float textureScale) {
		float resultX = textureX / this.size;
		float resultY = textureY / this.size;

		textureCoord[0] = resultX;
		textureCoord[1] = resultY;

		textureCoord[2] = resultX;
		textureCoord[3] = (resultY + textureScale);

		textureCoord[4] = (resultX + textureScale);
		textureCoord[5] = (resultY + textureScale);

		textureCoord[6] = (resultX + textureScale);
		textureCoord[7] = resultY;

		if (texture != null) {
			renderTexture(x, y, size, size, texture);
		}
	}

	protected void renderTexture(int xPosition, int yPosition, int width1, int height1, ImageTexture texture) {
		if (texture != null) {
			texture.switchTo();
			GL11.glBegin(7);

			GL11.glTexCoord2f(textureCoord[0], textureCoord[1]);
			GL11.glVertex2f(xPosition + 0, yPosition + 0);

			GL11.glTexCoord2f(textureCoord[2], textureCoord[3]);
			GL11.glVertex2f(xPosition + 0, yPosition + height1);

			GL11.glTexCoord2f(textureCoord[4], textureCoord[5]);
			GL11.glVertex2f(xPosition + width1, yPosition + height1);

			GL11.glTexCoord2f(textureCoord[6], textureCoord[7]);
			GL11.glVertex2f(xPosition + width1, yPosition + 0);

			GL11.glEnd();
		}
	}

}
