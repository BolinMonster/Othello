package othello.gui.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JCheckBox;

/**
 * Classe personnalisée de case à cocher pour le jeu Othello.
 * 
 * <p>
 * Cette classe hérite de la classe JCheckBox et permet de créer une case à cocher
 * personnalisée avec une apparence spécifique pour le jeu Othello.
 * </p>
 * 
 * <p>
 * Source : https://github.com/DJ-Raven/raven-project/blob/main/src/checkbox/JCheckBoxCustom.java
 * </p>
 * 
 */
public class OthelloCheckBox extends JCheckBox {

	private final int border = 4;
	
	/**
     * Constructeur par défaut de la case à cocher OthelloCheckBox.
     */
	public OthelloCheckBox() {
		super();
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setOpaque(false);
		setBackground(new Color(69, 124, 235));
	}
	
	/**
     * Constructeur avec texte de la case à cocher OthelloCheckBox.
     * 
     * @param text Le texte à afficher à côté de la case à cocher.
     */
	public OthelloCheckBox(String text) {
		super(text, false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setOpaque(false);
		setBackground(new Color(69, 124, 235));
	}

	@Override
	public void paint(Graphics grphcs) {
		super.paint(grphcs);
		Graphics2D g2 = (Graphics2D) grphcs;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int ly = (getHeight() - 16) / 2;
		if (isSelected()) {
			if (isEnabled()) {
				g2.setColor(getBackground());
			} else {
				g2.setColor(Color.GRAY);
			}
			g2.fillRoundRect(1, ly, 16, 16, border, border);
			// Draw Check icon
			int px[] = { 4, 8, 14, 12, 8, 6 };
			int py[] = { ly + 8, ly + 14, ly + 5, ly + 3, ly + 10, ly + 6 };
			g2.setColor(Color.WHITE);
			g2.fillPolygon(px, py, px.length);
		} else {
			g2.setColor(Color.GRAY);
			g2.fillRoundRect(1, ly, 16, 16, border, border);
			g2.setColor(Color.WHITE);
			g2.fillRoundRect(2, ly + 1, 14, 14, border, border);
		}
		g2.dispose();
	}
}