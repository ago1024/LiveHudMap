package org.gotti.wurmonline.clientmods.livehudmap;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.gotti.wurmonline.clientmods.livehudmap.renderer.RenderType;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;

import com.wurmonline.client.game.PlayerPosition;
import com.wurmonline.client.game.TerrainChangeListener;
import com.wurmonline.client.game.World;
import com.wurmonline.client.renderer.PickData;
import com.wurmonline.client.renderer.backend.Queue;
import com.wurmonline.client.renderer.cave.CaveBufferChangeListener;
import com.wurmonline.client.renderer.gui.Renderer;
import com.wurmonline.client.resources.textures.ImageTexture;
import com.wurmonline.client.resources.textures.ImageTextureLoader;
import com.wurmonline.client.resources.textures.PreProcessedTextureData;
import com.wurmonline.client.resources.textures.TextureLoader;

public class LiveMap implements TerrainChangeListener, CaveBufferChangeListener {
	
	private int size;
	private final World world;
	
	private MapLayerView surface;
	private MapLayerView cave;

	private boolean dirty = true;
	private BufferedImage image;
	private ImageTexture texture;

	private int x;
	private int y;
	
	private int px = 0, py = 0;
	private final Method preprocessImage;
	
	public LiveMap(World world, int size) {
		this.size = size;
		
		this.world = world;
		
		this.world.getNearTerrainBuffer().addListener(this);
		this.world.getCaveBuffer().addCaveBufferListener(this);
		
		this.surface = new MapLayerView(world, RenderType.FLAT);
		this.cave = new MapLayerView(world, RenderType.CAVE);
		
		try {
			this.preprocessImage = ReflectionUtil.getMethod(TextureLoader.class, "preprocessImage", new Class[] { BufferedImage.class, boolean.class, boolean.class });
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public void update(int windowX, int windowY) {
		x = windowX;
		y = windowY;

		PlayerPosition pos = world.getPlayer().getPos();
		if (dirty || px != pos.getTileX() || py != pos.getTileY()) {
			px = pos.getTileX();
			py = pos.getTileY();
			
			image = getLayer().render(px, py);
			if (texture == null) {
				texture = ImageTextureLoader.loadNowrapNearestTexture(image, false);
			} else {
				try {
					PreProcessedTextureData data = ReflectionUtil.callPrivateMethod(TextureLoader.class, preprocessImage, image, false, true);
					texture.deferInit(data, TextureLoader.Filter.NEAREST, false, false, false);
				} catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
					throw new RuntimeException(e);
				}
			}
			
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
	
	public void render(Queue queue, float textureX, float textureY, float textureScale) {
		float resultX = textureX / this.size;
		float resultY = textureY / this.size;

		if (texture != null) {
			Renderer.texturedQuadAlphaBlend(queue, texture, 1.0f, 1.0f, 1.0f, 1.0f, (float) this.x,
					(float) this.y, (float) this.size, (float) this.size, resultX, resultY, textureScale,
					textureScale);
		}
	}

	@Override
	public void caveChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
		dirty = true;
	}

	@Override
	public void terrainUpdated(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2) {
		dirty = true;
	}

	public void pick(final PickData pickData, final float xMouse, final float yMouse) {
		this.getLayer().pick(pickData, xMouse, yMouse);
	}
}
