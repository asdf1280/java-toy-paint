package main;

import java.awt.Graphics2D;

public abstract class Drawer {
	public abstract void draw(Graphics2D g2, int width, int height);
	public abstract void toggle(boolean newMode);
}
