package com.wurmonline.client.renderer.gui;

import java.util.Locale;

import org.gotti.wurmonline.clientmods.livehudmap.LiveMap;
import org.gotti.wurmonline.clientmods.livehudmap.MapLayer;
import org.gotti.wurmonline.clientmods.livehudmap.renderer.RenderType;

import com.wurmonline.client.game.World;
import com.wurmonline.client.options.Options;
import com.wurmonline.client.resources.textures.ResourceTexture;
import com.wurmonline.client.resources.textures.ResourceTextureLoader;

public class LiveMapWindow extends WWindow {
	
	private WurmBorderPanel mainPanel;
	private LiveMap liveMap;

	public LiveMapWindow(World world) {
		super("Live map", true);
		setTitle("Live map");
		mainPanel = new WurmBorderPanel("Live map");
		
		this.liveMap = new LiveMap(world, 256);
		resizable = false;
		
        final String themeName = Options.guiSkins.options[Options.guiSkins.value()].toLowerCase(Locale.ENGLISH).replace(" ", "");
        final ResourceTexture texture = ResourceTextureLoader.getTexture("img.gui.button.mainmenu." + themeName);
        
        WurmArrayPanel<WButton> buttons = new WurmArrayPanel<WButton>("Live map buttons", WurmArrayPanel.DIR_VERTICAL);
        buttons.setInitialSize(35, 256, false);
        buttons.addComponent(new WTextureButton("+", "Zoom in" , texture, new ButtonListener() {

			@Override
			public void buttonPressed(WButton p0) {
			}

			@Override
			public void buttonClicked(WButton p0) {
				liveMap.zoomIn();
			}
        }));

        buttons.addComponent(new WTextureButton("-", "Zoom out" , texture, new ButtonListener() {

			@Override
			public void buttonPressed(WButton p0) {
			}

			@Override
			public void buttonClicked(WButton p0) {
				liveMap.zoomOut();
			}
        }));
        
        buttons.addComponent(new WTextureButton("Flat", "Flat view" , texture, new ButtonListener() {

			@Override
			public void buttonPressed(WButton p0) {
			}

			@Override
			public void buttonClicked(WButton p0) {
				liveMap.setRenderer(MapLayer.SURFACE, RenderType.FLAT);
			}
        }));
        
        buttons.addComponent(new WTextureButton("3D", "Pseudo 3D view" , texture, new ButtonListener() {

			@Override
			public void buttonPressed(WButton p0) {
			}

			@Override
			public void buttonClicked(WButton p0) {
				liveMap.setRenderer(MapLayer.SURFACE, RenderType.ISOMETRIC);
			}
        }));
        
        buttons.addComponent(new WTextureButton("Topo", "Topographic view" , texture, new ButtonListener() {

			@Override
			public void buttonPressed(WButton p0) {
			}

			@Override
			public void buttonClicked(WButton p0) {
				liveMap.setRenderer(MapLayer.SURFACE, RenderType.TOPOGRAPHIC);
			}
        }));
        
        
        
        LiveMapView liveMapView = new LiveMapView("Live map", liveMap, 256, 256);

        mainPanel.setComponent(liveMapView, WurmBorderPanel.WEST);
		mainPanel.setComponent(buttons, WurmBorderPanel.EAST);
		
		setComponent(mainPanel);
		setInitialSize();
	}
	
	public void setInitialSize() {
		setInitialSize(256 + 6 + 35, 256 + 25, false);
	}
	
	public void closePressed()
	{
		hud.toggleComponent(this);
	}

	public void toggle() {
		hud.toggleComponent(this);
	}
}
