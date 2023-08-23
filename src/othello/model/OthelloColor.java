package othello.model;

import java.awt.Color;
import java.io.Serializable;

import othello.OthelloGameMode;

/**
 * Cette énumération représente les couleurs possibles pour une case de l'Othello.
 */
public enum OthelloColor implements Serializable {
	
	/**
     * Couleur représentant une case vide.
     */
	EMPTY(null, "vide"),
	/**
     * Couleur représentant une case occupée par une pièce noir.
     */
	BLACK(Color.BLACK, "noir"),
	/**
     * Couleur représentant une case occupée par une pièce blanc.
     */
	WHITE(Color.WHITE, "blanche");
	
	private static final long serialVersionUID = 1L;
	private final Color color; // La couleur
	private final String colorName; // Le nom associé à cette couleur
	
	 /**
     * Constructeur pour une couleur de l'Othello.
     * @param color     La couleur associée à cette couleur de l'Othello.
     * @param colorName Le nom associé à cette couleur de l'Othello.
     */
	private OthelloColor(Color color, String colorName) {
		this.color = color;
		this.colorName = colorName;
	}
	
	/**
     * Retourne la couleur associée à cette couleur de l'Othello.
     * @return La couleur associée à cette couleur de l'Othello.
     */
	public Color getColor() {
		return color;
	}
	
	/**
     * Retourne le nom associé à cette couleur de l'Othello.
     * @return Le nom associé à cette couleur de l'Othello.
     */
	public String getColorName() {
		return colorName;
	}
	
	public static OthelloColor findByColorName(String colorName) {
	    for (OthelloColor color : OthelloColor.values()) {
	        if (color.getColorName().equals(colorName)) {
	            return color;
	        }
	    }
	    return null;
	}
	
	/**
     * Retourne une chaîne de caractères représentant l'objet en cours.
     * @return Une chaîne de caractères représentant l'objet en cours
     */
	@Override
	public String toString() {
        return "OthelloColor [color=" + getColor() + ", colorName=" + getColorName() + "]";
	}
}
