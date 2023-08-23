package othello.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import javax.swing.JPanel;

public class OthelloBackgroundPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private Color color1;
	private Color color2;

	public OthelloBackgroundPanel(LayoutManager layout, Color color1, Color color2) {
		this(color1, color2);
		setLayout(layout);
	}

	public OthelloBackgroundPanel(LayoutManager layout) {
		super(layout);
		this.color1 = Color.WHITE;
		this.color2 = Color.WHITE;
	}

	public OthelloBackgroundPanel(Color color1, Color color2) {
		this.color1 = color1;
		this.color2 = color2;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		int width = getWidth();
		int height = getHeight();
		GradientPaint gp = new GradientPaint(0, 0, color1, width, height, color2);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, width, height);
	}
}
