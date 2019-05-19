package main;

import java.awt.Graphics2D;

/**
 * This class is used to draw something automatically.
 * 
 * @author User
 *
 */
public abstract class Drawer {
	/**
	 * Draw the paint of this instance to graphics2d.
	 * 
	 * @param g2
	 * @param width
	 * @param height
	 */
	public abstract void draw(Graphics2D g2, int width, int height);

	/**
	 * Toggle paint
	 * 
	 * @param newMode
	 */
	public abstract void toggle(boolean newMode);
}
