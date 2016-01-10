package org.gotti.wurmonline.clientmods.livehudmap.renderer;

import com.wurmonline.client.game.World;

public enum RenderType {
	
	FLAT {
		@Override
		public MapRenderer createMapRenderer(World world) {
			return new MapRendererFlat(world.getNearTerrainBuffer());
		}
	},
	
	ISOMETRIC {
		@Override
		public MapRenderer createMapRenderer(World world) {
			return new MapRendererIsometric(world.getNearTerrainBuffer());
		}
	},
	
	TOPOGRAPHIC {
		@Override
		public MapRenderer createMapRenderer(World world) {
			return new MapRendererTopographic(world.getNearTerrainBuffer());
		}
	},
	
	CAVE {
		@Override
		public MapRenderer createMapRenderer(World world) {
			return new MapRendererCave(world.getCaveBuffer());
		}
		
		@Override
		public int getMapSize() {
			return 32;
		}
	};

	public static boolean highRes = false;

	public abstract MapRenderer createMapRenderer(World world);
	
	public int getMapSize() {
		if (highRes) {
			return 256;
		} else {
			return 128;
		}
	}

}
