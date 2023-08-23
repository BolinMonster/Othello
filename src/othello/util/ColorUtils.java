package othello.util;

import java.awt.Color;
import java.io.Serializable;

/**
 * Classe utilitaire pour la gestion des couleurs.
 * Cette classe fournit une méthode pour obtenir une couleur en fonction de son nom en utilisant la réflexion.
 */
public class ColorUtils implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private ColorUtils() {}
	
	/**
	 * Renvoie une couleur en fonction de son nom.
	 * @param name Le nom de la couleur en majuscules.
	 * @return La couleur correspondante, ou null si la couleur n'est pas trouvée.
	 */
	public static Color getColorByName(String name) {
		try {
			return (Color) Color.class.getField(name.toUpperCase()).get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
}
