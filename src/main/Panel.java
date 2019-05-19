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
	private Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
	BufferedImage img = new BufferedImage(ss.width, ss.height, BufferedImage.TYPE_4BYTE_ABGR);
	BufferedImage mt;
	{
		try {
			mt = ImageIO.read(getClass().getResourceAsStream("/main/material.png"));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static int a() {
		return (int) (Math.random() * 256);
	}

	public static Color ac() {
		return new Color(a(), a(), a());
	}

	private BasicStroke ttbs = new BasicStroke(1);
	public void as(int x, int y) {
//		int n = (int) (Math.random() * 10); 
		c = ac();
		g2.setColor(c);
		g2.setStroke(ttbs);
		g2.drawLine(x, y, x, y);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2.drawImage(img, 0, 0, null);

//		g.setColor(c);
//		g.fillRect(0, 140, 100, 30);

		if (mx >= 0 && my >= 0) {
			g2.setColor(c);
			g2.fillRect(mx + 5, my + 5, 20, 20);

			g2.setColor(Color.black);
			int strokei = (int) stroke;
			g2.drawOval(mx - strokei / 2, my - strokei / 2, strokei, strokei);
		}

		if (guide) {
			guides.draw((Graphics2D) g2, getWidth(), getHeight());
		}
	}

	int px = -1;
	int py = -1;
	Graphics2D g2 = img.createGraphics();

	public void erase() {
		g2.clearRect(0, 0, img.getWidth(), img.getHeight());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setColor(new Color(248, 245, 141));
		g2.fillRect(0, 170, img.getWidth(), img.getHeight());

		Random r = new Random(1);
		for (int i = 0; i < img.getHeight(); i++) {
			g2.setColor(new Color(228, 225, 121));
			g2.fillOval((int) (r.nextDouble() * ss.width), (int) (r.nextDouble() * ss.height), 2, 2);
		}

		g2.setColor(new Color(228, 225, 138));
		for (int i = 220; i <= img.getHeight(); i += 50) {
			g2.drawLine(0, i, img.getWidth(), i);
			g2.drawLine(0, i + 1, img.getWidth(), i + 1);
		}

		g2.drawImage(mt, 0, 0, ss.width, ss.height, null);

		overLay();
		repaint();
	}

	int mx = -5, my = -5;

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

	private Color c = new Color(0, 0, 0);
	private boolean guide = false;
	private final Drawer guides = new Drawer() {
		long showed = 0;

		@Override
		public void draw(Graphics2D g2, int width, int height) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

			int tm = (int) Math.min(System.currentTimeMillis() - showed, 128);
			g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), tm));
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

	float stroke = 7f;

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

		var d = frm.getRootPane();
		this.d = d;
		Object obj = "hello";
		d.getActionMap().put(obj, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				erase();
			}
		});
		d.getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), obj);
		obj = new Object();
		d.getActionMap().put(obj, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				c = new Color(a(), a(), a());
			}
		});
		d.getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), obj);

		obj = new Object();
		d.getActionMap().put(obj, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				c = new Color(0, 0, 0);
			}
		});
		d.getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), obj);

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
				stroke = (float) (Math.random() * 30) + 5;
			}
		});
		addHotkey(KeyEvent.VK_S, false, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stroke = 7f;
			}
		}, InputEvent.SHIFT_DOWN_MASK);
		addHotkey(KeyEvent.VK_S, false, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ImageIO.write(img, "png", new File(System.currentTimeMillis() + ".png"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}, InputEvent.CTRL_DOWN_MASK);
		addHotkey(KeyEvent.VK_S, false, new AbstractAction() {
			private void d(int x, int y, int x2, int y2) {
				for (int yy = y; yy < y2; yy++) {
					for (int xx = x; xx < x2; xx++) {
						as(xx, yy);
					}
				}
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				g2.fillRect(0, 170, img.getWidth(), img.getHeight() - 170);
				g2.setStroke(new BasicStroke(1));

				int parts = Runtime.getRuntime().availableProcessors() - 1;
				parts = img.getHeight();
				int divided = (img.getHeight() - 170) / parts;

				Holder<Integer> done = new Holder<Integer>(0);
				for (int i = 0; i < parts; i++) {
					int idx = i;
					Thread tr = new Thread(() -> {
						d(0, 170 + (divided * idx), img.getWidth(), 170 + (divided * idx) + divided);
						done.t++;
					});
					tr.setPriority(Thread.MAX_PRIORITY);
					tr.start();
				}
				d(0, 170 + (divided * parts) + divided, img.getWidth(), img.getHeight());
			}
		}, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK);

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				px = -1;
				py = -1;
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
				px = -1;
				py = -1;
				mx = e.getX();
				my = e.getY();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				mouseDrag(e);
			}
		});
	}

	JRootPane d;

	public void addHotkey(int keycode, boolean release, Action a) {
		addHotkey(keycode, release, a, 0);
	}

	public void addHotkey(int keycode, boolean release, Action a, int modifier) {
		var obj = new Object();
		d.getActionMap().put(obj, a);
		d.getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keycode, modifier, release), obj);
	}

	public void mouseDrag(MouseEvent e) {
		g2.setStroke(new BasicStroke(stroke));
		g2.setColor(c);
		if (px == -1 && py == -1) {
			px = e.getX();
			py = e.getY();
		} else {
		}
		g2.drawLine(px, py, e.getX(), e.getY());
		px = e.getX();
		py = e.getY();
		overLay();

		mx = e.getX();
		my = e.getY();
	}
}
