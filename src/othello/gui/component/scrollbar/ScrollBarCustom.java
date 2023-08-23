package othello.gui.component.scrollbar;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollBar;

/**
 * Source : https://github.com/DJ-Raven/raven-project/blob/main/src/scrollbar/ScrollBarCustom.java
 */

public class ScrollBarCustom extends JScrollBar {

	private static final long serialVersionUID = 1L;

	public ScrollBarCustom() {
        setUI(new ModernScrollBarUI());
        setPreferredSize(new Dimension(8, 8));
        setForeground(new Color(48, 144, 216));
        setBackground(Color.WHITE);
    }
}