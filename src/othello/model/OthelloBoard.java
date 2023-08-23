package othello.model;

import java.io.Serializable;
import java.util.Stack;

import othello.util.StringUtility;

/**
 * Classe représentant un plateau de jeu pour le jeu Othello.
 * 
 * Le plateau de jeu est représenté comme une matrice de pièces de jeu Othello.
 * La classe fournit des méthodes pour manipuler le plateau, telles que le
 * déplacement des pièces, la récupération de l'état du plateau, etc.
 */
public class OthelloBoard implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	private static final Integer DEFAULT_ROWS = 8;
	private static final Integer DEFAULT_COLUMNS = 8;

	private OthelloPiece[][] board;
	private Integer rows;
	private Integer columns;
	private Stack<OthelloMove> moveHistory;
	
	/**
	 * Crée un nouveau plateau de jeu avec un nombre spécifié de rangées et de
	 * colonnes.
	 * 
	 * @param rows Le nombre de rangées du plateau.
	 * @param columns Le nombre de colonnes du plateau.
	 */
	private OthelloBoard(Integer rows, Integer columns) {
		this.rows = rows;
		this.columns = columns;
		this.board = new OthelloPiece[rows][columns];
		this.moveHistory = new Stack<OthelloMove>();
		this.initializeBoard();
	}
	
	/**
	 * Crée un nouveau plateau de jeu avec le nombre de rangées et de colonnes par défaut.
	 */
	private OthelloBoard() {
		this(DEFAULT_ROWS, DEFAULT_COLUMNS);
	}
	
	/**
	 * Crée un nouveau plateau de jeu avec un nombre spécifié de rangées et de
	 * colonnes. Le nombre total de cellules du plateau doit être égal au nombre total
	 * de cellules du plateau par défaut.
	 * 
	 * @param rows Le nombre de rangées du plateau.
	 * @param columns Le nombre de colonnes du plateau.
	 * @return Un nouveau plateau de jeu avec les spécifications données, ou
	 *                 {@code null} si le nombre total de cellules ne correspond pas
	 *                 au nombre total de cellules du plateau par défaut.
	 */
	public static OthelloBoard createBoard(Integer rows, Integer columns) {
		if (rows * columns != getDefaultRows() * getDefaultColumns()) {
			return null;
		}
		return new OthelloBoard(rows, columns);
	}
	
	/**
	 * Renvoie le nombre de lignes par défaut du plateau.
	 * 
	 * @return Le nombre de lignes par défaut du plateau.
	 */
	public static Integer getDefaultRows() {
		return DEFAULT_ROWS;
	}
	
	/**
	 * Renvoie le nombre de colonnes par défaut du plateau.
	 * 
	 * @return Le nombre de colonnes par défaut du plateau.
	 */
	public static Integer getDefaultColumns() {
		return DEFAULT_COLUMNS;
	}
	
	/**
	 * Renvoie le nombre de lignes du plateau.
	 * 
	 * @return Le nombre de lignes du plateau.
	 */
	public int getRows() {
		return this.rows;
	}
	
	/**
	 * Renvoie le nombre de colonnes du plateau.
	 * 
	 * @return Le nombre de colonnes du plateau.
	 */
	public int getColumns() {
		return this.columns;
	}

	/**
	 * Clonage en profondeur pour éviter ça : ob.getBoard()[7][7] = new
	 * OthelloPiece(OthelloColor.WHITE);
	 * 
	 * @return le tableau de pièces
	 */
	public OthelloPiece[][] getPieces() {
		return this.deepCopyBoard(this.board);
	}
	
	/**
	 * Renvoie une copie de la pièce de jeu à la position spécifiée.
	 * 
	 * @param move  Le mouvement spécifiant la position de la pièce.
	 * @return      Une copie de la pièce de jeu à la position spécifiée.
	 */
	public OthelloPiece getPiece(OthelloMove move) {
		return this.deepCopyPiece(this.board[move.getRow()][move.getColumn()]);
	}
	
	/**
	 * Modifie le tableau de pièces de jeu du plateau.
	 * 
	 * @param board Le nouveau tableau de pièces de jeu.
	 */
	public void setBoard(OthelloPiece[][] board) {
		this.board = board;
		moveHistory.clear();
	}
	
	/**
	 * Définit le plateau de jeu avec le coup joué et la pièce placée.
	 *
	 * @param move  Le coup joué.
	 * @param piece La pièce à placer.
	 */
	public void setBoard(OthelloMove move, OthelloPiece piece) {
		final Integer row = move.getRow();
		final Integer column = move.getColumn();
		board[row][column] = piece;
		moveHistory.push(move);
	}
	
	/**
	 * Définit le nombre de lignes du plateau de jeu.
	 *
	 * @param rows Le nombre de lignes.
	 */
	public void setRows(Integer rows) {
		this.rows = getDefaultRows();
	}
	
	/**
	 * Définit le nombre de colonnes du plateau de jeu.
	 *
	 * @param columns Le nombre de colonnes.
	 */
	public void setColumns(Integer columns) {
		this.columns = getDefaultColumns();
	}
	

	/**
	 * Retourne le coup le plus récent joué.
	 *
	 * @return Le coup plus récent joué.
	 */
	public OthelloMove getRecentMove() {
		if (moveHistory.isEmpty()) {
			return null;
		}
		return moveHistory.peek();
	}
	
	/**
	 * Annule le dernier coup joué en retirant la pièce du plateau.
	 */
	public void popMove() {
		if (!moveHistory.isEmpty()) {
			OthelloMove lastMove = moveHistory.pop();
			final Integer row = lastMove.getRow();
			final Integer column = lastMove.getColumn();
			board[row][column] = OthelloPiece.EMPTY;
		}
	}
	
	/**
	 * Initialise le plateau de jeu avec les pièces vides et les pièces de départ pour une nouvelle partie d'Othello.
	 */
	private void initializeBoard() {
		for (Integer i = 0; i < this.getRows(); i++) {
			for (Integer j = 0; j < this.getColumns(); j++) {
				this.board[i][j] = OthelloPiece.EMPTY;
			}
		}
		this.board[3][3] = this.board[4][4] = OthelloPiece.WHITE;
		this.board[3][4] = this.board[4][3] = OthelloPiece.BLACK;
	}
	
	/**
	 * Effectue une copie en profondeur du plateau de jeu.
	 *
	 * @param original Le plateau de jeu original à copier.
	 * @return Une copie en profondeur du plateau de jeu original.
	 */
	private OthelloPiece[][] deepCopyBoard(OthelloPiece[][] original) {
		final Integer rows = original.length;
		final Integer columns = original[0].length;
		OthelloPiece[][] copy = new OthelloPiece[rows][columns];
		for (Integer i = 0; i < rows; i++) {
			for (Integer j = 0; j < columns; j++) {
				copy[i][j] = OthelloPiece.valueOf(original[i][j].name());
			}
		}
		return copy;
	}
	
	/**
	 * Effectue une copie en profondeur d'une pièce d'Othello.
	 *
	 * @param piece La pièce d'origine à copier.
	 * @return Une copie en profondeur de la pièce d'origine.
	 */
	private OthelloPiece deepCopyPiece(OthelloPiece piece) {
		return OthelloPiece.valueOf(piece.name());
	}
	
	/**
	 * Retourne un clone de l'objet OthelloBoard. La méthode utilise la méthode
	 * native clone() pour créer une copie profonde de l'objet.
	 *
	 * @return Un clone de l'objet OthelloBoard.
	 */
	public OthelloBoard getClone() {
		OthelloBoard clone = null;
		try {
			clone = (OthelloBoard) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
	

	/**
	 * Retourne une chaîne de caractères représentant l'état actuel du plateau de jeu
	 * sous la forme d'un tableau. Les pièces du jeu sont représentées par les caractères
	 * "B" pour les pièces noires, "W" pour les pièces blanches, et " " pour les cases vides.
	 *
	 * @return Une chaîne de caractères représentant le plateau de jeu.
	 */
	public String toStringBoard() {
		StringBuilder sb = new StringBuilder();
		int boardLength = board.length;
		int minCellWidth = 5;
		int maxCharSize = Math.max(Integer.toString(boardLength - 1).length(), 1);
		maxCharSize = maxCharSize < minCellWidth ? minCellWidth : maxCharSize;
		/*------------------------------------------*/
		for (int i = 0; i < boardLength + 1; i++) {
			for (int j = 0; j < maxCharSize; j++) {
				sb.append("-");
			}
			sb.append("|");
		}
		sb.append("\n");
		/*------------------------------------------*/
		for (int i = 0; i < maxCharSize; i++) {
			sb.append("-");
		}
		sb.append("|");
		for (int i = 0; i < boardLength; i++) {
			String colIndex = StringUtility.centerString(maxCharSize, Integer.toString(i));
			sb.append(colIndex + "|");
		}
		sb.append("\n");
		/*------------------------------------------*/
		for (int i = 0; i < boardLength + 1; i++) {
			for (int j = 0; j < maxCharSize; j++) {
				sb.append("-");
			}
			sb.append("|");
		}
		sb.append("\n");
		/*------------------------------------------*/
		String rowIndex, cellContent, centeredContent;
		int rowLength;
		for (int row = 0; row < boardLength; row++) {
			rowIndex = StringUtility.centerString(maxCharSize, Integer.toString(row));
			sb.append(rowIndex + "|");
			rowLength = board[row].length;
			for (int col = 0; col < rowLength; col++) {
				OthelloPiece piece = board[row][col];
				if (piece == null || piece == OthelloPiece.EMPTY) {
					cellContent = " ";
				} else if (piece == OthelloPiece.BLACK) {
					cellContent = "B";
				} else if (piece == OthelloPiece.WHITE) {
					cellContent = "W";
				} else {
					cellContent = " ";
				}
				centeredContent = StringUtility.centerString(maxCharSize, cellContent);
				sb.append(centeredContent + "|");
			}
			sb.append("\n");
			/*----------------------------------------*/
			for (int i = 0; i < boardLength + 1; i++) {
				for (int j = 0; j < maxCharSize; j++)
					sb.append("-");
				sb.append("|");
			}
			sb.append("\n");
			/*-----------------------------------------*/
		}
		return sb.toString();
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		OthelloBoard clone = (OthelloBoard) super.clone();
		clone.setBoard(deepCopyBoard(this.board));
		clone.setRows(this.getRows());
		clone.setColumns(this.getColumns());
		return clone;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
