package org.gotti.wurmonline.clientmods.livehudmap;

import java.awt.image.BufferedImage;

import org.gotti.wurmonline.clientmods.livehudmap.renderer.MapRenderer;
import org.gotti.wurmonline.clientmods.livehudmap.renderer.RenderType;

import com.wurmonline.client.game.World;
import com.wurmonline.math.FastMath;

public class MapLayerView {
	
	private RenderType type;
	
	private MapRenderer renderer;
	
	private int zoom;

	private World world;

	public MapLayerView(World world, RenderType renderType) {
		this.world = world;
		this.zoom = 1;
		this.type = renderType;
		this.renderer = this.type.createMapRenderer(world);
	}
	
	public void zoomIn() {
		if (type.getMapSize() / zoom > 4) {
			zoom *= 2;
		}
	}
	
	public void zoomOut() {
		if (zoom > 1) {
			zoom /= 2;
		}
	}

	public BufferedImage render(int px, int py) {
		int sz = type.getMapSize() / zoom;
		return renderer.createMapDump(px - sz / 2, py - sz / 2, sz, sz, px, py);
	}

	public void setRenderer(RenderType renderType) {
		if (renderType != type) {
			type = renderType;
			renderer = type.createMapRenderer(world);
			if (type.getMapSize() / zoom < 4) {
				zoom = FastMath.nearestPowerOfTwo(type.getMapSize() / 4);
			}
			if (zoom == 0) {
				zoom = 1;
			}
		}
	}
}
