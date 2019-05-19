package main;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Main {
	public Main() {
		JFrame frm = new JFrame();
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setTitle("Simple Paint");
		frm.setContentPane(new Panel(frm));
		frm.getContentPane().setPreferredSize(new Dimension(800, 800));
		frm.pack();
		frm.setVisible(true);
	}
	public static void main(String[] args) {
		new Main();
	}
}
