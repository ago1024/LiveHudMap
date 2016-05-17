package com.wurmonline.client.renderer.gui;

import org.gotti.wurmonline.clientmods.livehudmap.LiveMap;
import org.lwjgl.opengl.GL11;

public class LiveMapView extends FlexComponent {

	private final LiveMap liveMap;

	LiveMapView(String name, LiveMap liveMap, int width, int height) {
		super(name);
		setInitialSize(width, height, false);
		sizeFlags = FlexComponent.FIXED_WIDTH | FlexComponent.FIXED_HEIGHT;
		
		this.liveMap = liveMap;
	}
	
	protected void renderComponent(float alpha) {
		super.renderComponent(alpha);
		
		liveMap.update(x, y);
				
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(r, g, b, 1.0F);

		liveMap.render(0.0F, 0.0F, 1.0F);
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	
	
	

}
