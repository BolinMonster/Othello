package othello.gui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;

public class OthelloPieceLabelIcon extends JLabel {

	private static final long serialVersionUID = 1L;
	private Color color;
	private Color textColor;
	
	private Boolean isActive;

	public OthelloPieceLabelIcon(Color color, String text) {
		super();
		this.color = color;
		this.textColor = Color.BLACK;
		this.isActive = false;
		super.setText(text);
		this.setPreferredSize(new Dimension(50, 50));
	}

	public Color getColor() {
		return this.color;
	}

	public Color getTextColor() {
		return this.textColor;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}
	
	public void setColor(Color color) {
		this.color = color;
		repaint();
	}
	
	public void setTextColor(Color textColor) {
		this.textColor = textColor != null ? textColor : Color.BLACK;
		repaint();
	}	
	
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive != null ? isActive : false;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		final int width = getWidth();
		final int height = getHeight();
		// Dessin du cercle de bordure
		g2d.setColor(getColor());
		g2d.fillOval(0, 0, width - 1, height - 1);
		// Dimension du cercle central
		final int diameter = Math.min(width, height) - 10;
		final int x = (width - diameter) / 2;
		final int y = (height - diameter) / 2;
		final Color colorFrom = getColor();
		final Color colorTo = Color.GRAY;
		final GradientPaint gradient = new GradientPaint(0, 0, colorFrom, width, height, colorTo);
		g2d.setPaint(gradient);
		g2d.fillOval(x, y, diameter, diameter);
		// Dessin du texte centré
		g2d.setColor(getTextColor());
		// Dessin du texte
		String text = super.getText();
		final int fontSize = getIsActive() ? 18 : 12;
		Font font = new Font("Arial", Font.BOLD, fontSize);
		FontMetrics fm = getFontMetrics(font);
		int textWidth = fm.stringWidth(text);
		int textHeight = fm.getHeight();
		if (fm.stringWidth(text) > diameter) {
			int endIndex = text.length() - 1;
			while (fm.stringWidth(text.substring(0, endIndex) + "...") > diameter && endIndex > 0) {
				endIndex--;
			}
			text = text.substring(0, endIndex) + "...";
		}
		g2d.setFont(font);
		int textX = x + (diameter - textWidth) / 2;
		int textY = y + (diameter - textHeight) / 2 + fm.getAscent();
		g2d.drawString(text, textX, textY);
		g2d.dispose();
	}
}
