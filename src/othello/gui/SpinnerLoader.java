package othello.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Source : https://stackoverflow.com/questions/48620030/how-to-make-the-youtubes-rotating-spinner-loading-screen-on-java-swing
 */
public class SpinnerLoader extends JPanel {

	private static final long serialVersionUID = 1L;
	private double angle;
	private double extent;
	private double angleDelta = -1;
	private double extentDelta = -5;
	private boolean flip = false;
	private Timer timer;

	public SpinnerLoader() {
		setOpaque(false);
		setPreferredSize(new Dimension(50, 50));
		initArc();
		this.timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				angle += angleDelta;
				extent += extentDelta;
				if (Math.abs(extent) % 360.0 == 0) {
					angle = angle - extent;
					flip = !flip;
					if (flip) {
						extent = 360.0;
					} else {
						extent = 0.0;
					}
				}
				repaint();
			}
		});
		timer.start();
		timer.stop();
	}

	public void initArc() {
		angle = 0.0;
		extent = 0.0;
		angleDelta = -1;
		extentDelta = -5;
		flip = false;
	}
	
	public void restartSpinnerLoader() {
		initArc();
		timer.restart();
	}

	public void stopSpinnerLoader() {
		timer.stop();
		initArc();
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(50, 50);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		final int marginX = 5;
		final int marginY = 5;
		Insets insets = getInsets();
		final int width = getWidth() - insets.left - insets.right - 2 * marginX;
		final int height = getHeight() - insets.top - insets.bottom - 2 * marginY;
		final int x = insets.left + marginX + (getWidth() - insets.left - insets.right - width) / 2;
		final int y = insets.top + marginY + (getHeight() - insets.top - insets.bottom - height) / 2;
		final Arc2D.Double arc = new Arc2D.Double(x, y, width, height, angle, extent, Arc2D.OPEN);
		final BasicStroke stroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
		g2d.setStroke(stroke);
		g2d.setColor(Color.WHITE);
		g2d.draw(arc);
		g2d.dispose();
	}

}