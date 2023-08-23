package othello.model;

import java.io.Serializable;
import othello.OthelloGame;

/**
 * Cette classe représente un joueur dans le jeu Othello.
 */
public class OthelloPlayer implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	private String name; // Le nom du joueur
	private OthelloPiece piece; // La pièce utilisée par le joueur
	private Integer nbPiece; // Le nombre de pièce(s) du joueur
	
    /**
     * Constructeur de la classe OthelloPlayer.
     * @param name Le nom du joueur
     * @param piece La pièce utilisée par le joueur
     * @param nbPiece Le nombre de pièce(s) du joueur
     */
	private OthelloPlayer(String name, OthelloPiece piece, Integer nbPiece) {
		super();
		this.name = name;
		this.piece = piece;
		this.nbPiece = nbPiece;
	}
	
	public static OthelloPlayer createPlayer(String name, OthelloPiece piece, Integer nbPiece) {
		if (/* !OthelloPlayer.isValidName(name) || */
				!OthelloPlayer.isValidPiece(piece) 
				|| !OthelloPlayer.isValidNbPiece(nbPiece)) {
			return null;
		}
		return new OthelloPlayer(name, piece, nbPiece);
	}
	
	private static boolean isValidName(String name) {
		return name != null 
				&& name.length() > 1 
				&& name.length() < 50 
				&& name.matches("^[a-zA-Z][a-zA-Z0-9-]{0,49}$");
	}
	
	private static boolean isValidPiece(OthelloPiece piece) {
		return piece != null;
	}
	
	private static boolean isValidNbPiece(Integer nbPiece) {
		return nbPiece != null 
				&& nbPiece >= OthelloGame.getMinNbPiecePerPlayer() 
				&& nbPiece <= OthelloGame.getMaxNbPiecePerPlayer();
	}
	
	/**
     * Retourne le nom du joueur.
     * @return Le nom du joueur
     */
	public String getName() {
		return name;
	}
	
	/**
     * Retourne la pièce utilisée par le joueur.
     * @return La pièce utilisée par le joueur
     */
	public OthelloPiece getPiece() {
		return piece;
	}
	
	/**
     * Retourne le nombre de pièce(s) du joueur
     * @return le nombre de pièce(s) du joueur
     */
	public Integer getNbPiece() {
		return nbPiece;
	}

    /**
     * Définit le nom du joueur.
     * @param name Le nouveau nom du joueur
     */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
     * Définit la pièce utilisée par le joueur.
     * @param piece La nouvelle pièce utilisée par le joueur
     */
	public void setPiece(OthelloPiece piece) {
		this.piece = piece;
	}
	
	/**
     * Définit le nombre de pièece(s) utilisée(s) par le joueur.
     * @param nbPiece Le nombre de pièce(s)
     */
	public void setNbPiece(Integer nbPiece) {
		this.nbPiece = nbPiece;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		OthelloPlayer clone = (OthelloPlayer) super.clone();
		clone.setName(this.getName());
		clone.setPiece(this.getPiece());
		clone.setNbPiece(this.getNbPiece());
		return clone;
	}
	
	public OthelloPlayer getClone() {
		OthelloPlayer clone = null;
		try {
			clone = (OthelloPlayer) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
	

	@Override
	public boolean equals(Object other) {
		boolean result = false;
		if (other instanceof OthelloPlayer) {
			OthelloPlayer that = (OthelloPlayer) other;
			result = that.canEquals(this)
					&& that.getName().equals(this.getName())
					&& that.getPiece() == this.getPiece()
					&& that.getNbPiece() == this.getNbPiece();
		}
		return result;
	}
	
	private boolean canEquals(Object obj) {
		return obj instanceof OthelloPlayer;
	}
	
	/**
     * Retourne une chaîne de caractères représentant l'objet en cours.
     * @return Une chaîne de caractères représentant l'objet en cours
     */
    @Override
    public String toString() {
        return "OthelloPlayer [name=" + getName() + ", piece=" + getPiece() + ", nbPiece=" + getNbPiece() + "]";
    }
}
