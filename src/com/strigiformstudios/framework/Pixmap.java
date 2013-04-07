package com.strigiformstudios.framework;

import com.strigiformstudios.framework.Graphics.PixmapFormat;

public interface Pixmap {
	public int getWidth();

	public int getHeight();

	public PixmapFormat getFormat();

	public void dispose();
}
