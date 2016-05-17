package org.gotti.wurmonline.clientmods.livehudmap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gotti.wurmonline.clientmods.livehudmap.renderer.RenderType;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmClientMod;
import org.gotti.wurmunlimited.modsupport.console.ConsoleListener;
import org.gotti.wurmunlimited.modsupport.console.ModConsole;

import com.wurmonline.client.game.World;
import com.wurmonline.client.renderer.gui.HeadsUpDisplay;
import com.wurmonline.client.renderer.gui.LiveMapWindow;
import com.wurmonline.client.renderer.gui.MainMenu;
import com.wurmonline.client.renderer.gui.WurmComponent;
import com.wurmonline.client.settings.SavePosManager;

public class LiveHudMapMod implements WurmClientMod, Initable, PreInitable, Configurable, ConsoleListener {

	private static Logger logger = Logger.getLogger(LiveHudMapMod.class.getName());
	
	private boolean hiResMap = false;
	
	private Object liveMap;
	
	@Override
	public void configure(Properties properties) {
		hiResMap = Boolean.valueOf(properties.getProperty("hiResMap", String.valueOf(hiResMap)));
		
		logger.log(Level.INFO, "hiResMap: " + hiResMap);

		RenderType.highRes = hiResMap;
	}

	@Override
	public void preInit() {
	}

	@Override
	public void init() {

		// com.wurmonline.client.renderer.gui.HeadsUpDisplay.init(int, int)
		HookManager.getInstance().registerHook("com.wurmonline.client.renderer.gui.HeadsUpDisplay", "init", "(II)V",
				new InvocationHandlerFactory() {

					@Override
					public InvocationHandler createInvocationHandler() {
						return new InvocationHandler() {

							@Override
							public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
								method.invoke(proxy, args);

								initLiveMap((HeadsUpDisplay) proxy);

								return null;
							}
						};
					}
				});

		ModConsole.addConsoleListener(this);
	}
	
	private void initLiveMap(HeadsUpDisplay hud) {
		
		new Runnable() {
			
			@Override
			public void run() {
				try {
					World world = ReflectionUtil.getPrivateField(hud, ReflectionUtil.getField(hud.getClass(), "world"));
		
					LiveMapWindow liveMapWindow = new LiveMapWindow(world);
					liveMap = liveMapWindow;
		
					MainMenu mainMenu = ReflectionUtil.getPrivateField(hud, ReflectionUtil.getField(hud.getClass(), "mainMenu"));
					mainMenu.registerComponent("Live map", liveMapWindow);
		
					List<WurmComponent> components = ReflectionUtil.getPrivateField(hud, ReflectionUtil.getField(hud.getClass(), "components"));
					components.add(liveMapWindow);
					
					SavePosManager savePosManager = ReflectionUtil.getPrivateField(hud, ReflectionUtil.getField(hud.getClass(), "savePosManager"));
					savePosManager.registerAndRefresh(liveMapWindow, "livemapwindow");
				}
				catch (IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
					throw new RuntimeException(e);
				}
			}
		}.run();
	}
	
	@Override
	public boolean handleInput(String string, Boolean silent) {
		if (string != null && string.startsWith("toggle livemap") && liveMap instanceof LiveMapWindow) {
			((LiveMapWindow)liveMap).toggle();
			return true;
		}
		return false;
	}

}
