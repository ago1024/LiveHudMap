package com.wurmonline.client.renderer.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.gotti.wurmonline.clientmods.livehudmap.LiveMap;
import org.gotti.wurmonline.clientmods.livehudmap.MapLayer;
import org.gotti.wurmonline.clientmods.livehudmap.renderer.RenderType;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;

import com.wurmonline.client.game.World;
import com.wurmonline.client.options.Options;
import com.wurmonline.client.renderer.PickData;
import com.wurmonline.client.resources.textures.ImageTexture;
import com.wurmonline.client.resources.textures.ImageTextureLoader;
import com.wurmonline.client.resources.textures.ResourceTexture;
import com.wurmonline.client.resources.textures.ResourceTextureLoader;

public class LiveMapWindow extends WWindow {

	private WurmBorderPanel mainPanel;
	private LiveMap liveMap;
	private BufferedImage iconImage;
	private LiveMapView liveMapView;

	public LiveMapWindow(World world) {
		super("Live map", true);
		setTitle("Live map");
		mainPanel = new WurmBorderPanel("Live map");

		this.liveMap = new LiveMap(world, 256);
		resizable = false;

		iconImage = loadIconImage();

		WurmArrayPanel<WButton> buttons = new WurmArrayPanel<WButton>("Live map buttons", WurmArrayPanel.DIR_VERTICAL);
		buttons.setInitialSize(32, 256, false);
		buttons.addComponent(createButton("+", "Zoom in" , 0, new ButtonListener() {

			@Override
			public void buttonPressed(WButton p0) {
			}

			@Override
			public void buttonClicked(WButton p0) {
				liveMap.zoomIn();
			}
		}));

		buttons.addComponent(createButton("-", "Zoom out" , 1, new ButtonListener() {

			@Override
			public void buttonPressed(WButton p0) {
			}

			@Override
			public void buttonClicked(WButton p0) {
				liveMap.zoomOut();
			}
		}));

		buttons.addComponent(createButton("Flat", "Flat view" , 2, new ButtonListener() {

			@Override
			public void buttonPressed(WButton p0) {
			}

			@Override
			public void buttonClicked(WButton p0) {
				liveMap.setRenderer(MapLayer.SURFACE, RenderType.FLAT);
			}
		}));

		buttons.addComponent(createButton("3D", "Pseudo 3D view" , 3, new ButtonListener() {

			@Override
			public void buttonPressed(WButton p0) {
			}

			@Override
			public void buttonClicked(WButton p0) {
				liveMap.setRenderer(MapLayer.SURFACE, RenderType.ISOMETRIC);
			}
		}));

		buttons.addComponent(createButton("Topo", "Topographic view" , 4, new ButtonListener() {

			@Override
			public void buttonPressed(WButton p0) {
			}

			@Override
			public void buttonClicked(WButton p0) {
				liveMap.setRenderer(MapLayer.SURFACE, RenderType.TOPOGRAPHIC);
			}
		}));



		liveMapView = new LiveMapView("Live map", liveMap, 256, 256);

		mainPanel.setComponent(liveMapView, WurmBorderPanel.WEST);
		mainPanel.setComponent(buttons, WurmBorderPanel.EAST);

		setComponent(mainPanel);
		setInitialSize(256 + 6 + 32, 256 + 25, false);
		layout();
		sizeFlags = FlexComponent.FIXED_WIDTH | FlexComponent.FIXED_HEIGHT;
	}

	private BufferedImage loadIconImage() {
		try {
			URL url = this.getClass().getClassLoader().getResource("livemapicons.png");
			if (url == null && this.getClass().getClassLoader() == HookManager.getInstance().getLoader()) {
				url = HookManager.getInstance().getClassPool().find(LiveMapWindow.class.getName());
				if (url != null) {
					String path = url.toString();
					int pos = path.lastIndexOf('!');
					if (pos != -1) {
						path = path.substring(0, pos) + "!/livemapicons.png";
					}
					url = new URL(path);
				}
			}
			if (url != null) {
				return ImageIO.read(url);
			} else {
				return null;
			}
		} catch (IOException e) {
			Logger.getLogger(LiveMapWindow.class.getName()).log(Level.WARNING, e.getMessage(), e);
			return null;
		}
	}

	private WButton createButton(String label, String tooltip, int textureIndex, ButtonListener listener) {
		if (iconImage != null) {
			BufferedImage image = iconImage.getSubimage(textureIndex * 32, 0, 32, 32);
			ImageTexture texture = ImageTextureLoader.loadNowrapNearestTexture(image, false);
			return new LiveMapButton("", tooltip, 32, 32, texture, listener);
		} else {
			final String themeName = Options.guiSkins.options[Options.guiSkins.value()].toLowerCase(Locale.ENGLISH).replace(" ", "");
			final ResourceTexture backgroundTexture = ResourceTextureLoader.getTexture("img.gui.button.mainmenu." + themeName);
			return new WTextureButton(label, tooltip, backgroundTexture, listener);
		}
	}

	public void closePressed()
	{
		hud.toggleComponent(this);
	}

	public void toggle() {
		hud.toggleComponent(this);
	}
	
	public void pick(final PickData pickData, final int xMouse, final int yMouse) {
		if (this.liveMapView.contains(xMouse, yMouse)) {
			this.liveMap.pick(pickData, 1.0f * (xMouse - this.liveMapView.x) / this.liveMapView.width, 1.0f * (yMouse - this.liveMapView.y) / this.liveMapView.width);
		}
	}
	
}
