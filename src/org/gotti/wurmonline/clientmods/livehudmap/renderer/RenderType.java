package org.gotti.wurmonline.clientmods.livehudmap.renderer;

import com.wurmonline.client.game.World;

public enum RenderType {
	
	FLAT {
		@Override
		public MapRenderer createMapRenderer(World world) {
			return new MapRendererFlat(world.getNearTerrainBuffer());
		}
		
		@Override
		public int getMapSize() {
			return 128;
		}
	},
	
	ISOMETRIC {
		@Override
		public MapRenderer createMapRenderer(World world) {
			return new MapRendererIsometric(world.getNearTerrainBuffer());
		}
		
		@Override
		public int getMapSize() {
			return 128;
		}

	},
	
	TOPOGRAPHIC {
		@Override
		public MapRenderer createMapRenderer(World world) {
			return new MapRendererTopographic(world.getNearTerrainBuffer());
		}
		
		@Override
		public int getMapSize() {
			return 128;
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
	
	public abstract MapRenderer createMapRenderer(World world);
	
	public abstract int getMapSize(); 

}
