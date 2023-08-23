package othello.util;

/**
 * Classe utilitaire pour la manipulation de chaînes de caractères.
 * Cette classe fournit une méthode pour centrer une chaîne de caractères dans une largeur donnée.
 */
public class StringUtility {
	
	private StringUtility() {}
	
	/**
	 * Source : https://stackoverflow.com/questions/8154366/how-to-center-a-string-using-string-format
	 * Centre une chaîne de caractères dans une largeur donnée.
	 * @param width La largeur de la chaîne centrée.
	 * @param s La chaîne de caractères à centrer.
	 * @return La chaîne centrée.
	 */
	public static String centerString(int width, String s) {
	    return String.format("%-" + width  + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
	}
	
}
