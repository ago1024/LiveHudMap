package org.gotti.wurmonline.clientmods.livehudmap.renderer;

import java.awt.image.BufferedImage;

public interface MapRenderer {
	BufferedImage createMapDump(int xo, int yo, int lWidth, int lHeight, int px, int py);
}
