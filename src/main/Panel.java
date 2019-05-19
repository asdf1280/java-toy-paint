package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public class Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	BufferedImage drawnImage = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_4BYTE_ABGR);
	BufferedImage material;
	{
		try {
			material = ImageIO.read(getClass().getResourceAsStream("/main/material.png"));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static int getRandomColorNumber() {
		return (int) (Math.random() * 256);
	}

	public static Color getRandomColor() {
		return new Color(getRandomColorNumber(), getRandomColorNumber(), getRandomColorNumber());
	}

	private BasicStroke randomStroke = new BasicStroke(1);
	public void drawRandomPixel(int x, int y) {
//		int n = (int) (Math.random() * 10); 
		currentColor = getRandomColor();
		g2.setColor(currentColor);
		g2.setStroke(randomStroke);
		g2.drawLine(x, y, x, y);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2.drawImage(drawnImage, 0, 0, null);

//		g.setColor(c);
//		g.fillRect(0, 140, 100, 30);

		if (cursorX >= 0 && cursorY >= 0) {
			g2.setColor(currentColor);
			g2.fillRect(cursorX + 5, cursorY + 5, 20, 20);

			g2.setColor(Color.black);
			int strokei = (int) currentStroke;
			g2.drawOval(cursorX - strokei / 2, cursorY - strokei / 2, strokei, strokei);
		}

		if (guide) {
			guides.draw((Graphics2D) g2, getWidth(), getHeight());
		}
	}

	int prevX = -1, prevY = -1;
	Graphics2D g2 = drawnImage.createGraphics();

	public void erase() {
		g2.clearRect(0, 0, drawnImage.getWidth(), drawnImage.getHeight());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setColor(new Color(248, 245, 141));
		g2.fillRect(0, 170, drawnImage.getWidth(), drawnImage.getHeight());

		Random r = new Random(1);
		for (int i = 0; i < drawnImage.getHeight(); i++) {
			g2.setColor(new Color(228, 225, 121));
			g2.fillOval((int) (r.nextDouble() * screenSize.width), (int) (r.nextDouble() * screenSize.height), 2, 2);
		}

		g2.setColor(new Color(228, 225, 138));
		for (int i = 220; i <= drawnImage.getHeight(); i += 50) {
			g2.drawLine(0, i, drawnImage.getWidth(), i);
			g2.drawLine(0, i + 1, drawnImage.getWidth(), i + 1);
		}

		g2.drawImage(material, 0, 0, screenSize.width, screenSize.height, null);

		overLay();
		repaint();
	}

	int cursorX = -5, cursorY = -5;

	public void overLay() {
		g2.setColor(new Color(53, 35, 28));
		g2.fillRect(0, 0, 10000, 170);

		g2.setColor(Color.white);
		g2.setFont(new Font("", 0, 100));
		g2.drawString("Idea Paint Brush", 0, 80);
		g2.setStroke(new BasicStroke(4f));
		g2.setFont(new Font("", 0, 30));
		g2.drawString("M to see controls", 0, 130);
	}

	private Color currentColor = new Color(0, 0, 0);
	private boolean guide = false;
	private final Drawer guides = new Drawer() {
		long showed = 0;

		@Override
		public void draw(Graphics2D g2, int width, int height) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

			int tm = (int) Math.min(System.currentTimeMillis() - showed, 128);
			g2.setColor(new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), tm));
			g2.fillRoundRect(width / 2 - 250, height / 2 - 250, 500, 500, 40, 40);

			int cy = height / 2 - 250;
			g2.setColor(Color.white);
			g2.setFont(new Font("Segoe UI", 3, 60));
			FontMetrics fm = g2.getFontMetrics();
			cy += fm.getHeight();
			String txt = "Guides";
			g2.drawString(txt, (width - fm.stringWidth(txt)) / 2, cy);

			cy += 30;

			g2.setFont(new Font("Segoe UI", 0, 25));
			fm = g2.getFontMetrics();
			cy += fm.getHeight();
			txt = "Space: erase";
			g2.drawString(txt, (width - fm.stringWidth(txt)) / 2, cy);

			cy += fm.getHeight();
			txt = "R: change color random";
			g2.drawString(txt, (width - fm.stringWidth(txt)) / 2, cy);

			cy += fm.getHeight();
			txt = "E: reset color";
			g2.drawString(txt, (width - fm.stringWidth(txt)) / 2, cy);

			cy += fm.getHeight();
			txt = "M: see more controls";
			g2.drawString(txt, (width - fm.stringWidth(txt)) / 2, cy);

			cy += fm.getHeight();
			txt = "S: randomize stroke";
			g2.drawString(txt, (width - fm.stringWidth(txt)) / 2, cy);

			cy += fm.getHeight();
			txt = "Shift + S: reset stroke";
			g2.drawString(txt, (width - fm.stringWidth(txt)) / 2, cy);

			cy += fm.getHeight();
			txt = "Ctrl + S: Save your masterpiece";
			g2.drawString(txt, (width - fm.stringWidth(txt)) / 2, cy);

			cy += fm.getHeight();
			txt = "Ctrl + Shift + S: Shit your masterpiece";
			g2.drawString(txt, (width - fm.stringWidth(txt)) / 2, cy);
		}

		@Override
		public void toggle(boolean tg) {
			showed = System.currentTimeMillis();
		}
	};

	float currentStroke = 7f;

	@SuppressWarnings("serial")
	public Panel(JFrame frm) {
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		for (int i = 0; i < 3; i++) {
			erase();
			System.out.println("Loading " + (i + 1) + " / 50");
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					repaint();
				}
			}
		}).start();

		var rootPane = frm.getRootPane();
		this.rootPane = rootPane;
		Object obj = "hello";
		rootPane.getActionMap().put(obj, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				erase();
			}
		});
		rootPane.getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), obj);
		obj = new Object();
		rootPane.getActionMap().put(obj, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				currentColor = new Color(getRandomColorNumber(), getRandomColorNumber(), getRandomColorNumber());
			}
		});
		rootPane.getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), obj);

		obj = new Object();
		rootPane.getActionMap().put(obj, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				currentColor = new Color(0, 0, 0);
			}
		});
		rootPane.getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), obj);

		addHotkey(KeyEvent.VK_M, false, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (guide)
					return;
				guide = true;
				guides.toggle(guide);
			}
		});
		addHotkey(KeyEvent.VK_M, true, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				guide = false;
				guides.toggle(guide);
			}
		});
		addHotkey(KeyEvent.VK_S, false, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				currentStroke = (float) (Math.random() * 30) + 5;
			}
		});
		addHotkey(KeyEvent.VK_S, false, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				currentStroke = 7f;
			}
		}, InputEvent.SHIFT_DOWN_MASK);
		addHotkey(KeyEvent.VK_S, false, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ImageIO.write(drawnImage, "png", new File(System.currentTimeMillis() + ".png"));
				} catch (IOException exception) {
					// TODO Auto-generated catch block
					exception.printStackTrace();
				}
			}
		}, InputEvent.CTRL_DOWN_MASK);
		addHotkey(KeyEvent.VK_S, false, new AbstractAction() {
			private void fillRandomPixel(int x1, int y1, int x2, int y2) {
				for (int y = y1; y < y2; y++) {
					for (int x = x1; x < x2; x++) {
						drawRandomPixel(x, y);
					}
				}
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				g2.fillRect(0, 170, drawnImage.getWidth(), drawnImage.getHeight() - 170);
				g2.setStroke(new BasicStroke(1));

				int parts = Runtime.getRuntime().availableProcessors() - 1;
				parts = drawnImage.getHeight();
				int divided = (drawnImage.getHeight() - 170) / parts;

				Holder<Integer> done = new Holder<Integer>(0);
				for (int index = 0; index < parts; index++) {
					int i = index;
					Thread thread = new Thread(() -> {
						fillRandomPixel(0, 170 + (divided * i), drawnImage.getWidth(), 170 + (divided * i) + divided);
						done.t++;
					});
					thread.setPriority(Thread.MAX_PRIORITY);
					thread.start();
				}
				fillRandomPixel(0, 170 + (divided * parts) + divided, drawnImage.getWidth(), drawnImage.getHeight());
			}
		}, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK);

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				prevX = -1;
				prevY = -1;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				mouseDrag(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				mouseDrag(e);
			}
		});
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				prevX = -1;
				prevY = -1;
				cursorX = e.getX();
				cursorY = e.getY();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				mouseDrag(e);
			}
		});
	}

	JRootPane rootPane;

	public void addHotkey(int keycode, boolean release, Action a) {
		addHotkey(keycode, release, a, 0);
	}

	public void addHotkey(int keycode, boolean release, Action a, int modifier) {
		var obj = new Object();
		rootPane.getActionMap().put(obj, a);
		rootPane.getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keycode, modifier, release), obj);
	}

	public void mouseDrag(MouseEvent e) {
		g2.setStroke(new BasicStroke(currentStroke));
		g2.setColor(currentColor);
		if (prevX == -1 && prevY == -1) {
			prevX = e.getX();
			prevY = e.getY();
		} else {
		}
		g2.drawLine(prevX, prevY, e.getX(), e.getY());
		prevX = e.getX();
		prevY = e.getY();
		overLay();

		cursorX = e.getX();
		cursorY = e.getY();
	}
}
