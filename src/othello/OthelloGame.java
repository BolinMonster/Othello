package othello;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import othello.ai.OthelloSearchAlgorithm;
import othello.model.OthelloBoard;
import othello.model.OthelloDifficulty;
import othello.model.OthelloMove;
import othello.model.OthelloPiece;
import othello.model.OthelloPlayer;

/**
 * Resources : http://vision.gel.ulaval.ca/~klein/lothello/othello.htm
 *
 */
public class OthelloGame implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_ROWS = 8;
	private static final int DEFAULT_COLUMNS = 8;
	private static final Integer MIN_NB_PIECE_PER_PLAYER = 2;
	private static final Integer MAX_NB_PIECE_PER_PLAYER = 32;

	private OthelloGameMode gameMode;
	private OthelloSearchAlgorithm searchAlgorithm;
	private OthelloDifficulty difficulty;
	private OthelloBoard board;
	private OthelloPlayer firstPlayer;
	private OthelloPlayer secondPlayer;
	private OthelloPlayer currentPlayer;
	
	private Map<OthelloPlayer, Stack<OthelloMove>> mapPlayersMoves;

	private EventListenerList listenerList;
	private ChangeEvent changeEvent;

	// DateTime timeSpend;

	private OthelloGame(OthelloGameMode gameMode, OthelloSearchAlgorithm searchAlgorithm, OthelloDifficulty difficulty,
			OthelloPiece pieceFirstPlayer, OthelloPiece pieceSecondPlayer) {
		this.gameInitialisation(gameMode, searchAlgorithm, difficulty, pieceFirstPlayer, pieceSecondPlayer);
		this.eventInitialisation();
	}

	private void gameInitialisation(OthelloGameMode gameMode, OthelloSearchAlgorithm searchAlgorithm,
			OthelloDifficulty difficulty, OthelloPiece pieceFirstPlayer, OthelloPiece pieceSecondPlayer) {
		this.gameMode = gameMode;
		this.searchAlgorithm = searchAlgorithm;
		this.difficulty = difficulty;
		this.board = OthelloBoard.createBoard(DEFAULT_ROWS, DEFAULT_COLUMNS);
		this.firstPlayer = OthelloPlayer.createPlayer("J1", pieceFirstPlayer, OthelloGame.getMinNbPiecePerPlayer());
		this.secondPlayer = OthelloPlayer.createPlayer("J2", pieceSecondPlayer, OthelloGame.getMinNbPiecePerPlayer());
		this.currentPlayer = firstPlayer;
		this.mapPlayersMoves = new HashMap<OthelloPlayer, Stack<OthelloMove>>(); {
			mapPlayersMoves.put(firstPlayer, new Stack<OthelloMove>());
			mapPlayersMoves.put(secondPlayer, new Stack<OthelloMove>());
		};
		for (int row = 0; row < board.getRows(); row++) {
			for (int col = 0; col < board.getColumns(); col++) {
				OthelloMove move = OthelloMove.createMove(row, col);
				if (board.getPiece(move) == firstPlayer.getPiece()) {
					mapPlayersMoves.get(firstPlayer).push(move);
				} if (board.getPiece(move) == secondPlayer.getPiece()) {
					mapPlayersMoves.get(secondPlayer).push(move);
				}
			}
		}
	}

	private void eventInitialisation() {
		this.listenerList = new EventListenerList();
		this.changeEvent = null;
	}

	public static OthelloGame createGame(OthelloGameMode gameMode, OthelloSearchAlgorithm searchAlgorithm,
			OthelloDifficulty difficulty, OthelloPiece pieceFirstPlayer, OthelloPiece pieceSecondPlayer) {
		return new OthelloGame(gameMode, searchAlgorithm, difficulty, pieceFirstPlayer, pieceSecondPlayer);
	}

	public static OthelloGame createGame(OthelloGame game) {
		final OthelloGame gameCreated = new OthelloGame(game.getGameMode(), game.getSearchAlgorithm(),
				game.getDifficulty(), game.getFirstPlayer().getPiece(), game.getSecondPlayer().getPiece());
		gameCreated.setCurrentPlayer(game.getCurrentPlayer());
		return gameCreated;
	}

	public static Integer getMinNbPiecePerPlayer() {
		return OthelloGame.MIN_NB_PIECE_PER_PLAYER;
	}

	public static Integer getMaxNbPiecePerPlayer() {
		return OthelloGame.MAX_NB_PIECE_PER_PLAYER;
	}

	public OthelloBoard getBoard() {
		return board.getClone();
	}

	public OthelloPlayer getFirstPlayer() {
		return firstPlayer.getClone();
	}

	public OthelloPlayer getSecondPlayer() {
		return secondPlayer.getClone();
	}

	public void setBoard(OthelloBoard board) {
		this.board = board;
		fireStateChanged();
	}

	public void placePiece(OthelloMove move, OthelloPiece piece) {
		this.board.setBoard(move, piece);
		fireStateChanged();
	}

	public void setFirstPlayer(OthelloPlayer firstPlayer) {
		this.firstPlayer = firstPlayer;
		fireStateChanged();
	}

	public void setSecondPlayer(OthelloPlayer secondPlayer) {
		this.secondPlayer = secondPlayer;
		fireStateChanged();
	}

	public OthelloPlayer getCurrentPlayer() {
		return this.currentPlayer;
	}

	public OthelloPlayer getOpponentPlayer() {
		return getCurrentPlayer().getPiece() == getFirstPlayer().getPiece() ? getSecondPlayer() : getFirstPlayer();
	}

	public void setCurrentPlayer(OthelloPlayer currentPlayer) {
		this.currentPlayer = currentPlayer;
		fireStateChanged();
	}

	public void setDifficulty(OthelloDifficulty difficulty) {
		this.difficulty = difficulty;
		fireStateChanged();
	}

	public void changeCurrentPlayer() {
		setCurrentPlayer(
				getCurrentPlayer().getPiece() == getFirstPlayer().getPiece() ? getSecondPlayer() : getFirstPlayer());
	}

	public Boolean isFirstPlayerCurrentPlayer() {
		return getCurrentPlayer().getPiece() == getFirstPlayer().getPiece();
	}

	public Boolean isSecondPlayerCurrentPlayer() {
		return getCurrentPlayer().getPiece() == getFirstPlayer().getPiece();
	}

	public OthelloDifficulty getDifficulty() {
		return difficulty;
	}

	public EventListenerList getListenerList() {
		return listenerList;
	}

	public ChangeEvent getChangeEvent() {
		return changeEvent;
	}

	public Boolean isValidMove(OthelloMove move, OthelloPlayer player) {
		if (move == null || player == null || getCurrentPlayer() == null) {
			return false;
		}
		final OthelloPiece[][] pieces = board.getPieces();
		OthelloPiece piece = player.getPiece();
		final Integer row = move.getRow();
		final Integer col = move.getColumn();
		if (row < 0 || row >= board.getRows() || col < 0 || col >= board.getColumns()) {
			return false;
		}
		if (pieces[row][col] != OthelloPiece.EMPTY) {
			return false;
		}
		final Integer[][] directions = { { 1, 1 }, // diagonal en bas à droite
				{ -1, -1 }, // diagonal en haut à gauche
				{ -1, 0 }, // vertical vers le haut
				{ -1, 1 }, // diagonal en haut à droite
				{ 0, -1 }, // horizontal vers la gauche
				{ 0, 1 }, // horizontal vers la droite
				{ 1, -1 }, // diagonal en bas à gauche
				{ 1, 0 }, // vertical vers le bas
		};
		Boolean isValid = false;
		final Integer boardRows = board.getRows();
		final Integer boardColumns = board.getColumns();
		OthelloPiece currentPiece = null;
		Integer rowStep = 0, colStep = 0;
		Integer currentRow = 0, currentCol = 0;
		Integer count = 0;
		for (Integer[] direction : directions) {
			rowStep = direction[0];
			colStep = direction[1];
			currentRow = row + rowStep;
			currentCol = col + colStep;
			count = 0;
			while (currentRow >= 0 && currentRow < boardRows && currentCol >= 0 && currentCol < boardColumns) {
				currentPiece = pieces[currentRow][currentCol];
				if (currentPiece == OthelloPiece.EMPTY) {
					break;
				}
				if (currentPiece == piece) {
					isValid = count > 0;
					break;
				}
				currentRow += rowStep;
				currentCol += colStep;
				count++;
			}
			if (isValid) {
				break;
			}
		}
		return isValid;
	}

	public boolean validateMove(OthelloMove move, OthelloPlayer player) {
		if (move == null || player == null || getCurrentPlayer() == null) {
			return false;
		}
		final OthelloPiece[][] pieces = board.getPieces();
		OthelloPiece piece = player.getPiece();
		final Integer row = move.getRow();
		final Integer col = move.getColumn();
		pieces[row][col] = piece;
		final Integer[][] directions = { { 1, 1 }, // diagonal en bas à droite
				{ -1, -1 }, // diagonal en haut à gauche
				{ -1, 0 }, // vertical vers le haut
				{ -1, 1 }, // diagonal en haut à droite
				{ 0, -1 }, // horizontal vers la gauche
				{ 0, 1 }, // horizontal vers la droite
				{ 1, -1 }, // diagonal en bas à gauche
				{ 1, 0 }, // vertical vers le bas
		};
		final Integer boardRows = board.getRows();
		final Integer boardColumns = board.getColumns();
		OthelloPiece currentPiece = null;
		Integer rowStep = 0, colStep = 0;
		Integer currentRow = 0, currentCol = 0;
		Integer count = 0;
		for (Integer[] direction : directions) {
			rowStep = direction[0];
			colStep = direction[1];
			currentRow = row + rowStep;
			currentCol = col + colStep;
			count = 0;
			while (currentRow >= 0 && currentRow < boardRows && currentCol >= 0 && currentCol < boardColumns) {
				currentPiece = pieces[currentRow][currentCol];
				if (currentPiece == OthelloPiece.EMPTY) {
					break;
				}
				if (currentPiece == piece) {
					if (count > 0) {
						OthelloMove updatedMove = OthelloMove.createMove(row, col);
						board.setBoard(updatedMove, piece);
						// Enregistrement du coup joué
			            OthelloPlayer playerEntry = null;
				        for (Map.Entry<OthelloPlayer, Stack<OthelloMove>> entry : mapPlayersMoves.entrySet()) {
				        	playerEntry = entry.getKey();
				            if (getCurrentPlayer().getPiece() == playerEntry.getPiece()) {
				            	break;
				            }
				        }
				        if (playerEntry != null) {
				        	mapPlayersMoves.get(playerEntry).push(updatedMove);
				        	System.out.println("Mouvement enregistré : " + updatedMove);
				        }
						// Toutes les autres pièces à modifier
						for (int i = 1; i <= count; i++) {
							updatedMove = OthelloMove.createMove(row + i * rowStep, col + i * colStep);
							this.placePiece(updatedMove, currentPiece);
						}
						firstPlayer.setNbPiece(this.countPieces(getFirstPlayer()));
						secondPlayer.setNbPiece(this.countPieces(getSecondPlayer()));
						fireStateChanged();
						return true;
					}
					break;
				}
				currentRow += rowStep;
				currentCol += colStep;
				count++;
			}
		}
		return false;
	}
	
	public AbstractMap.SimpleEntry<OthelloPlayer, OthelloMove> getRecentMove() {
        OthelloPlayer playerEntry = null;
        for (Map.Entry<OthelloPlayer, Stack<OthelloMove>> entry : mapPlayersMoves.entrySet()) {
        	playerEntry = entry.getKey();
            if (getCurrentPlayer().getPiece() == playerEntry.getPiece()) {
            	break;
            }
        }
        if (playerEntry != null) {
        	final Stack<OthelloMove> moveStack = mapPlayersMoves.get(playerEntry);
        	OthelloMove topElement = null;
        	try {
        		topElement = moveStack.peek();
        	} catch (EmptyStackException  e) {
        		topElement = null;
			}
        	return new AbstractMap.SimpleEntry<OthelloPlayer, OthelloMove>(playerEntry, topElement);
        }
        return null;
	}

	public List<OthelloMove> getValidMoves(OthelloPlayer player) {
		final List<OthelloMove> validMoves = new ArrayList<>();
		if (player == null)
			return validMoves;
		OthelloMove move = null;
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				move = OthelloMove.createMove(i, j);
				if (this.isValidMove(move, player)) {
					validMoves.add(move);
				}
				move = null;
			}
		}
		return validMoves;
	}

	public Boolean isGameOver() {
		return getValidMoves(firstPlayer).isEmpty() && getValidMoves(secondPlayer).isEmpty();
	}

	public int countPieces(OthelloPlayer player) {
		int count = 0;
		OthelloPiece[][] pieces = board.getPieces();
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[i].length; j++) {
				if (pieces[i][j] == player.getPiece()) {
					count++;
				}
			}
		}
		return count;
	}

	public OthelloPlayer getWinner() {
		final int firstPlayerCount = countPieces(getFirstPlayer());
		final int secondPlayerCount = countPieces(getSecondPlayer());
		return (firstPlayerCount > secondPlayerCount) ? getFirstPlayer()
				: ((secondPlayerCount > firstPlayerCount) ? getSecondPlayer() : null);
	}

	public void resetGame() {
		final OthelloGameMode gameMode = this.getGameMode() != null ? this.getGameMode()
				: OthelloGameMode.PLAYER_VS_PLAYER;
		final OthelloSearchAlgorithm searchAlgorithm = this.getSearchAlgorithm() != null ? this.getSearchAlgorithm()
				: OthelloSearchAlgorithm.MINMAX;
		final OthelloDifficulty difficulty = this.getDifficulty() != null ? this.getDifficulty()
				: OthelloDifficulty.EASY;
		final OthelloPiece pieceFirstPlayer = this.getFirstPlayer().getPiece() != null
				? this.getFirstPlayer().getPiece()
				: OthelloPiece.BLACK;
		final OthelloPiece pieceSecondPlayer = this.getSecondPlayer().getPiece() != null
				? this.getSecondPlayer().getPiece()
				: OthelloPiece.WHITE;
		this.gameInitialisation(gameMode, searchAlgorithm, difficulty, pieceFirstPlayer, pieceSecondPlayer);
		fireStateChanged();
	}

	public OthelloSearchAlgorithm getSearchAlgorithm() {
		return searchAlgorithm;
	}

	public void setSearchAlgorithm(OthelloSearchAlgorithm searchAlgorithm) {
		this.searchAlgorithm = searchAlgorithm;
	}

	public OthelloGameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(OthelloGameMode gameMode) {
		this.gameMode = gameMode;
	}

	public OthelloGame saveGame(String fileName) {
		final File file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
				ObjectOutputStream oos = new ObjectOutputStream(bos)) {
			OthelloGame game = this;
			oos.writeObject(game);
			return game;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public OthelloGame loadGame(String fileName) {
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName));
				ObjectInputStream ois = new ObjectInputStream(bis)) {
			OthelloGame game = (OthelloGame) ois.readObject();
			this.setGame(game);
			return game;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setGame(OthelloGame game) {
		this.setGameMode(game.getGameMode());
		this.setDifficulty(game.getDifficulty());
		this.setBoard(game.getBoard());
		this.setFirstPlayer(game.getFirstPlayer());
		this.setSecondPlayer(game.getSecondPlayer());
		this.setCurrentPlayer(game.getCurrentPlayer());
		fireStateChanged();
	}

	public void setListenerList(EventListenerList listenerList) {
		this.listenerList = listenerList;
	}

	public void setChangeEvent(ChangeEvent changeEvent) {
		this.changeEvent = changeEvent;
	}

	public void addChangeListener(ChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (listenerList == null) {
			listenerList = new EventListenerList();
		}
		listenerList.add(ChangeListener.class, listener);
	}

	public void removeChangeListener(ChangeListener listener) {
		if (listener == null || listenerList == null) {
			return;
		}
		listenerList.remove(ChangeListener.class, listener);
	}

	protected void fireStateChanged() {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);
				}
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}

	public OthelloGame getClone() {
		OthelloGame clone = null;
		try {
			clone = (OthelloGame) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		OthelloGame clone = (OthelloGame) super.clone();
		clone.setBoard(this.board.getClone());
		clone.setFirstPlayer(this.firstPlayer.getClone());
		clone.setSecondPlayer(this.secondPlayer.getClone());
		clone.setCurrentPlayer(this.currentPlayer.getClone());
		return clone;
	}

}
