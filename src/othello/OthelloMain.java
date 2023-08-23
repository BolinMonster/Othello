package othello;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import othello.gui.OthelloMainMenu;

public class OthelloMain {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new OthelloMainMenu().display();
			}
		});
	}
}
