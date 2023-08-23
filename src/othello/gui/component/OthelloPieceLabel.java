package othello.gui.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import othello.model.OthelloBoard;

public class OthelloPieceLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	private static final Color FIRST_GREEN_BACKGROUND_COLOR = new Color(0, 130, 92);
	private static final Color SECOND_GREEN_BACKGROUND_COLOR = new Color(0, 100, 70);
	
	private static final Color HOVER_POSSIBLE_MOVE_COLOR = new Color(136, 197, 160);
	
	private static Integer cellLabelNumber = Integer.valueOf(0);
	private Integer cellLabelId = Integer.valueOf(0);
	
	private Color pieceColor;
	private Color pieceBorderColor;
	
	private Boolean isOccupied;
	private Boolean isEnabledPossibleMove;
	private Boolean isPossibleMove;
	
	public OthelloPieceLabel(Color pieceColor, Color pieceBorderColor, Boolean isOccupied, Boolean isEnabledPossibleMove, Boolean isPossibleMove) {
		super();
		this.cellLabelId = OthelloPieceLabel.cellLabelNumber++;
		this.pieceColor = pieceColor == null ? FIRST_GREEN_BACKGROUND_COLOR : pieceColor;
		this.isOccupied = isOccupied == null ? false : isOccupied;
		this.pieceBorderColor = !isOccupied ? HOVER_POSSIBLE_MOVE_COLOR : pieceBorderColor;
		this.isEnabledPossibleMove = isEnabledPossibleMove == null ? false : isEnabledPossibleMove;
		this.isPossibleMove = isPossibleMove == null ? false : isPossibleMove;
		createModel();
		createView();
		placeComponents();
		createController();
	}

	public static Integer getCellLabelNumber() {
		return OthelloPieceLabel.cellLabelNumber;
	}

	public Integer getCellLabelId() {
		return this.cellLabelId;
	}

	public static Color getFirstGreenBackgroundColor() {
		return OthelloPieceLabel.FIRST_GREEN_BACKGROUND_COLOR;
	}

	public static Color getSecondGreenBackgroundColor() {
		return OthelloPieceLabel.SECOND_GREEN_BACKGROUND_COLOR;
	}
	
	public Color getPieceColor() {
		return this.pieceColor;
	}
	
	public Color getPieceBorderColor() {
		return this.pieceBorderColor;
	}
	
	public boolean getIsOccupied() {
		return this.isOccupied;
	}
	
	public boolean getIsPossibleMove() {
		return this.isPossibleMove;
	}
	
	public boolean getIsEnabledPossibleMove() {
		return this.isEnabledPossibleMove;
	}

	public Color getDefaultBackgroundColor() {
		final Integer maxRows = OthelloBoard.getDefaultRows();
		final Integer maxColumns = OthelloBoard.getDefaultRows();
		final Integer row = this.getCellLabelId() / maxRows;
		final Integer column = this.getCellLabelId() % maxColumns;
		if (row % 2 == 0) {
			if (column % 2 == 0) {
				return getFirstGreenBackgroundColor();
			} else {
				return getSecondGreenBackgroundColor();
			}
		} else {
			if (column % 2 == 0) {
				return getSecondGreenBackgroundColor();
			} else {
				return getFirstGreenBackgroundColor();
			}
		}
	}
	
	public void setPieceColor(Color color) {
		if (color == null) return;
		this.pieceColor = color;
		this.repaint();
	}
	
	public void setPieceBorderColor(Color color) {
		if (color == null) return;
		this.pieceBorderColor = color;
		this.repaint();
	}
	
	public void setIsOccupied(Boolean isOccupied) {
		if (isOccupied == null) return;
		this.isOccupied = isOccupied;
		this.repaint();
	}
	
	public void setIsPossibleMove(Boolean isPossibleMove) {
		if (isPossibleMove == null) return;
		this.isPossibleMove = isPossibleMove;
		this.repaint();
	}
	
	public void setIsEnabledPossibleMove(Boolean isEnabledPossibleMove) {
		if (isPossibleMove == null) return;
		this.isEnabledPossibleMove = isEnabledPossibleMove;
		this.repaint();
	}

	private void createModel() {
	}

	private void createView() {
		this.setOpaque(true);
		this.setBackground(this.getDefaultBackgroundColor());
	}

	private void placeComponents() {
	}

	private void createController() {
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		final int width = getWidth();
		final int height = getHeight();
		// Dimension du cercle central
		final int diameter = Math.min(width, height) - 10;
		final int x = (width - diameter) / 2;
		final int y = (height - diameter) / 2;
		// Fond couleur verte
		g2d.setColor(getBackground());
		g2d.fillRect(0, 0, width, height);
		if (isOccupied) {
			// Dessin de la bordure du cercle
			g2d.setColor(this.getPieceBorderColor());
			g2d.setStroke(new BasicStroke(diameter / 5 > 0 ? diameter / 5 : 1));
			g2d.drawOval(x + diameter / 20, y + diameter / 20, diameter - diameter / 10, diameter - diameter / 10);
			// Dessin du cercle
			// final Float xSource = 0F, ySource = 0F, xDest = (float) width, yDest = (float) height;
			final Color colorFrom = this.getPieceColor();
			final Color colorTo = Color.GRAY;
			final GradientPaint gradient = new GradientPaint(0, 0, colorFrom, width, height, colorTo);
			g2d.setPaint(gradient);
			g2d.fillOval(x + diameter / 20, y + diameter / 20, diameter - diameter / 10, diameter - diameter / 10);
		}
		// Dessin du cercle d'assistance des mouvements possibles
		if (getIsEnabledPossibleMove() && isPossibleMove) {
			g2d.setColor(this.getPieceBorderColor());
	        g2d.setStroke(new BasicStroke(5));
			g2d.drawOval(x, y, diameter, diameter);
		}
		g2d.dispose();
	}

}
