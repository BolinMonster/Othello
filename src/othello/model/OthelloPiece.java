package othello.model;

import java.io.Serializable;

/**
 * Cette classe représente les différentes pièces du jeu Othello.
 */
public enum OthelloPiece implements Serializable {
	
	EMPTY(OthelloColor.EMPTY, -1), 
	BLACK(OthelloColor.BLACK, 0), 
	WHITE(OthelloColor.WHITE, 1);
	
	private static final long serialVersionUID = 1L;
	private final OthelloColor color; // La couleur de la pièce
	private final Integer value; // La valeur entière associée à la pièce
	
	/**
     * Constructeur de la classe OthelloPlayer.
     * @param color La couleur de la pièce
     * @param value La valeur de la pièce
     */
	private OthelloPiece(OthelloColor color, Integer value) {
		this.color = color;
		this.value = value;
	}

	/**
	 * Retourne la couleur de la pièce.
	 * @return La couleur de la pièce
	 */
	public OthelloColor getColor() {
		return this.color;
	}
	
	/**
	 * Retourne la valeur de la pièce.
	 * @return La valeur de la pièce
	 */
	public Integer getValue() {
		return this.value;
	}
	
	/**
	 * Retourne une chaîne de caractères représentant l'objet en cours.
	 * @return Une chaîne de caractères représentant l'objet en cours
	 */
	@Override
	public String toString() {
		return super.toString();
	}
}
