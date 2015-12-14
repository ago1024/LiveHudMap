package org.gotti.wurmonline.clientmods.livehudmap;

import java.awt.image.BufferedImage;

import org.gotti.wurmonline.clientmods.livehudmap.renderer.RenderType;
import org.lwjgl.opengl.GL11;

import com.wurmonline.client.game.PlayerPosition;
import com.wurmonline.client.game.TerrainChangeListener;
import com.wurmonline.client.game.World;
import com.wurmonline.client.renderer.cave.CaveBufferChangeListener;
import com.wurmonline.client.resources.textures.ImageTexture;
import com.wurmonline.client.resources.textures.ImageTextureLoader;

public class LiveMap implements TerrainChangeListener, CaveBufferChangeListener {
	
	private int size;
	private final World world;
	
	private MapLayerView surface;
	private MapLayerView cave;

	private boolean dirty = true;
	private BufferedImage image;
	private ImageTexture texture;

	private float[] textureCoord = { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F };
	private int x;
	private int y;
	
	private int px = 0, py = 0;
	
	public LiveMap(World world, int size) {
		this.size = size;
		
		this.world = world;
		
		this.world.getNearTerrainBuffer().addListener(this);
		this.world.getCaveBuffer().addCaveBufferListener(this);
		
		this.surface = new MapLayerView(world, RenderType.FLAT);
		this.cave = new MapLayerView(world, RenderType.CAVE);
	}

	public void update(int windowX, int windowY) {
		x = windowX;
		y = windowY;

		PlayerPosition pos = world.getPlayer().getPos();
		if (dirty || px != pos.getTileX() || py != pos.getTileY()) {
			px = pos.getTileX();
			py = pos.getTileY();
			
			image = getLayer().render(px, py);
			texture = ImageTextureLoader.loadNowrapNearestTexture(image);
			
			dirty = false;
		}
	}
	
	private MapLayerView getLayer() {
		if (isSurface()) {
			return surface;
		} else {
			return cave;
		}
	}
	
	private MapLayerView getLayer(MapLayer layer) {
		switch (layer) {
		case SURFACE:
			return surface;
		case CAVE:
			return cave;
		}
		throw new IllegalArgumentException(layer.name());
	}
	
	public void setRenderer(MapLayer layer, RenderType renderType) {
		getLayer(layer).setRenderer(renderType);
		dirty = true;
	}
	
	public void zoomIn() {
		getLayer().zoomIn();
		dirty = true;
	}

	public void zoomOut() {
		getLayer().zoomOut();
		dirty = true;
	}

	private boolean isSurface() {
		return world.getPlayerLayer() >= 0;
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

	@Override
	public void caveChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
		dirty = true;
	}

	@Override
	public void terrainUpdated(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2) {
		dirty = false;
	}

}
