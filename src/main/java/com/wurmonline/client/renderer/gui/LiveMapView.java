package com.wurmonline.client.renderer.gui;

import org.gotti.wurmonline.clientmods.livehudmap.LiveMap;

import com.wurmonline.client.renderer.backend.Queue;

public class LiveMapView extends FlexComponent {

	private final LiveMap liveMap;

	LiveMapView(String name, LiveMap liveMap, int width, int height) {
		super(name);
		setInitialSize(width, height, false);
		sizeFlags = FlexComponent.FIXED_WIDTH | FlexComponent.FIXED_HEIGHT;
		
		this.liveMap = liveMap;
	}
	
	@Override
	protected void renderComponent(Queue queue, float alpha) {
		super.renderComponent(queue, alpha);
		
		liveMap.update(x, y);
		liveMap.render(queue, 0.0F, 0.0F, 1.0F);
	}
	
	
	
	

}
