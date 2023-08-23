package othello.model;

import java.io.Serializable;

/**
 * Classe représentant un mouvement dans le jeu Othello.
 * Cette classe implémente l'interface Serializable pour permettre la sérialisation des objets,
 * et l'interface Cloneable pour permettre la duplication d'objets.
 */
public class OthelloMove implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	private Integer row;
	private Integer column;
	
    /**
     * Constructeur privé pour créer un objet OthelloMove avec une ligne et une colonne spécifiées.
     * @param row la ligne du mouvement
     * @param column la colonne du mouvement
     */
	private OthelloMove(Integer row, Integer column) {
		this.row = row;
		this.column = column;
	}
	
    /**
     * Constructeur par défaut privée pour créer un objet OthelloMove avec les valeurs par défaut de la grille Othello.
     */
	private OthelloMove() {
		this(OthelloBoard.getDefaultRows(), OthelloBoard.getDefaultColumns());
	}
	
    /**
     * Méthode statique pour créer un objet OthelloMove avec une ligne et une colonne spécifiées.
     * @param row la ligne du mouvement
     * @param column la colonne du mouvement
     * @return un objet OthelloMove créé avec la ligne et la colonne spécifiées, ou null si les valeurs sont invalides
     */
	public static OthelloMove createMove(Integer row, Integer column) {
		if (OthelloMove.isInvalidRow(row) || OthelloMove.isInvalidColumn(column)) {
			return null;
		}
		return new OthelloMove(row, column);
	}
	
    /**
     * Méthode statique pour vérifier si une valeur de ligne est invalide.
     * @param row la valeur de ligne à vérifier
     * @return true si la valeur de ligne est invalide, sinon false
     */
	private static boolean isInvalidRow(Integer row) {
		return row == null || row < 0 || row >= OthelloBoard.getDefaultRows();
	}
	
    /**
     * Méthode statique pour vérifier si une valeur de colonne est invalide.
     * @param column la valeur de colonne à vérifier
     * @return true si la valeur de colonne est invalide, sinon false
     */
	private static boolean isInvalidColumn(Integer column) {
		return column == null || column < 0 || column >= OthelloBoard.getDefaultColumns();
	}
	
    /**
     * Méthode pour obtenir la valeur de ligne du mouvement.
     * @return la valeur de ligne du mouvement
     */
	public Integer getRow() {
		return this.row;
	}
	
    /**
     * Méthode pour obtenir la valeur de colonne du mouvement.
     * @return la valeur de colonne du mouvement
     */
	public Integer getColumn() {
		return this.column;
	}
	
    /**
     * Méthode pour définir la valeur de ligne du mouvement.
     * Si la valeur de ligne est invalide, elle sera ignorée et la valeur actuelle sera conservée.
     * @param row la nouvelle valeur de ligne du mouvement
     * @return void
     */
	public void setRow(Integer row) {
		this.row = OthelloMove.isInvalidRow(row) ? getRow() : row;
	}
	
	/**
	 * Méthode pour définir la valeur de la colonne de ce mouvement.
	 * Si la colonne spécifiée est invalide, la colonne actuelle sera conservée.
	 * @param column La colonne à définir.
	 * @return void
	*/
	public void setColumn(Integer column) {
		this.column = OthelloMove.isInvalidColumn(column) ? getColumn() : column;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		OthelloMove clone = (OthelloMove) super.clone();
		clone.setRow(this.getRow());
		clone.setColumn(this.getColumn());
		return clone;
	}
	
	/**
	 * Crée et renvoie une copie profonde de cet objet OthelloMove.
	 * @return Une copie de cet objet OthelloMove.
	 * @throws CloneNotSupportedException Si la clonage n'est pas pris en charge pour cet objet.
	*/
	public OthelloMove getClone() {
		OthelloMove clone = null;
		try {
			clone = (OthelloMove) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
	
	@Override
	public boolean equals(Object other) {
		boolean result = false;
		if (other instanceof OthelloMove) {
			OthelloMove that = (OthelloMove) other;
			result = that.canEquals(this)
					&& that.getRow() == this.getRow()
					&& that.getColumn() == this.getColumn();
		}
		return result;
	}
	
	/**
	 * Vérifie si cet objet OthelloMove peut être égal à un autre objet donné.
	 * @param obj L'autre objet à comparer.
	 * @return true si les deux objets peuvent être égaux, false sinon.
	*/
	private boolean canEquals(Object obj) {
		return obj instanceof OthelloMove;
	}

	@Override
	public String toString() {
		return "OthelloMove [row=" + getRow() + ", column=" + getColumn() + "]";
	}
}
