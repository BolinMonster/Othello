package othello.ai;

import java.util.List;

import othello.OthelloGame;
import othello.model.OthelloBoard;
import othello.model.OthelloColor;
import othello.model.OthelloMove;
import othello.model.OthelloPiece;

/**
 * Classe représentant les fonctionnalités de la théorie des jeux sur le de jeu Othello.
*/
public class OthelloAI {
	
	/**
	 * Crée un arbre de recherche pour l'algorithme d'IA du jeu Othello.
	 *  
	 * @param game Le jeu Othello actuel.
	 * @param depth La profondeur de l'arbre de recherche.
	 * @return Le noeud racine de l'arbre de recherche.
	*/
	public static OthelloNode createTree(OthelloGame game, int depth) {
		// final long startTime = System.nanoTime();
		// final Runtime runtime = Runtime.getRuntime();
		final OthelloNode node = new OthelloNode();
		node.setGame(game);
		final List<OthelloMove> validMoves = game.getValidMoves(game.getCurrentPlayer());
		node.setBranchFactor(validMoves.size());
		
		// Inversion du sens et de MIN ET MAX pour afficher le recent noeud
		if (game.getCurrentPlayer().getPiece() == game.getFirstPlayer().getPiece()) {
			node.setPlayerType(OthelloPlayerType.MIN);
		} else if (game.getCurrentPlayer().getPiece() == game.getSecondPlayer().getPiece()) {
			node.setPlayerType(OthelloPlayerType.MAX);
		}
		
		if (validMoves.size() == 0 || depth == 0) {
			node.setNodeType(OthelloNodeType.LEAF);
			node.setBranchFactor(0);
		} else {
		    node.setNodeType(OthelloNodeType.INTERNAL_NODE);
		}
		
		// Inversion du sens et de MIN ET MAX pour afficher le recent noeud
        game.changeCurrentPlayer();
		node.setPlayerMoveEntry(game.getRecentMove());
		game.changeCurrentPlayer();
        
		System.out.println("n " + node.getNodeNum() + " recent: " + game.getRecentMove());
		if (depth > 0 && node.getBranchFactor() > 0) {
			// node.addChildren(new OthelloNode());
			for (int i = 0; i < node.getBranchFactor(); i++) {
				final List<OthelloMove> tmpValidMoves = game.getValidMoves(game.getCurrentPlayer());
				final OthelloGame tmpGame = game.getClone();
				// System.out.println(node.getMinifiedData());
		        // System.out.println("n " + node.getNodeNum() + " - Liste des mouvements : " + tmpValidMoves);
				// System.out.println("n " + node.getNodeNum() + " - i " + i  + " = " + tmpValidMoves.get(i));
				final OthelloMove currentMove = tmpValidMoves.get(i);
		        tmpGame.validateMove(currentMove, tmpGame.getCurrentPlayer());
		        tmpGame.changeCurrentPlayer();
			    node.addChildren(createTree(tmpGame, depth - 1));
			}
		}
		// final long endTime = System.nanoTime();
		// final long duration = (endTime - startTime) / 1_000_000;
		// final long memoryUsage = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
		// System.out.println("Durée d'exécution de createTree : " + duration + " ms.");
		// System.out.println("Utilisation de mémoire de createTree : " + memoryUsage + " Mo.");
		return node;
	}
	
	/**
	 * Affiche les heuristiques d'un noeud de l'arbre de recherche de l'algorithme d'IA du jeu Othello.
	 * @param node Le noeud dont les heuristiques doivent être affichées.
	 * @param parentNodeNum Le numéro de noeud du parent du noeud actuel.
	 * @return void
	*/
	public static void printHeuristics(OthelloNode node, Integer parentNodeNum) {
		System.out.println("Num de noeud : " + node.getNodeNum() + ", Numéro de noeud parent : " + parentNodeNum);
		System.out.println(node.toString());
		for (OthelloNode child : node.getChildren()) {
			printHeuristics(child, node.getNodeNum());
		}
	}
	
	/**
	 * Évalue la position actuelle pour une heuristique sur le jeu d'Othello en fonction des paramètres d'évaluation activés.
	 * 
	 * @param game Le jeu d'Othello à évaluer.
	 * @param isMaterialEnabled Indique si l'évaluation basée sur le matériel est activée.
	 * @param isMobilityEnabled Indique si l'évaluation basée sur la mobilité est activée.
	 * @param isPositionStrengthEnabled Indique si l'évaluation basée sur la force de position est activée.
	 * @return La valeur d'évaluation de la position du jeu d'Othello en fonction des paramètres activés.
	 */
	public static int evaluate(OthelloGame game, OthelloSearchAlgorithm searchAlgorithm) {
		if (!searchAlgorithm.isMaterialEnabled() && !searchAlgorithm.isMobilityEnabled() && !searchAlgorithm.isPositionStrengthEnabled()) {
			return computeByMaterial(game);
		}
		int score = 0;
		if (searchAlgorithm.isMaterialEnabled()) {
			score += OthelloAI.computeByMaterial(game);
		}
		if (searchAlgorithm.isMobilityEnabled()) {
			score += OthelloAI.computeByMobility(game);
		}
		if (searchAlgorithm.isPositionStrengthEnabled()) {
			score += OthelloAI.computeByPositionStrength(game);
		}
		return score;
	}
	
	/**
	 * Calcule la valeur de l'évaluation basée sur le matériel dans le jeu d'Othello.
	 *
	 * @param game Le jeu d'Othello pour lequel calculer l'évaluation basée sur le matériel.
	 * @return La valeur de l'évaluation basée sur le matériel du jeu d'Othello.
	 */
	public static int computeByMaterial(OthelloGame game) {
		OthelloPiece[][] pieces = game.getBoard().getPieces();
		int blackPieces = 0;
		int whitePieces = 0;
		for (OthelloPiece[] row : pieces) {
			for (OthelloPiece piece : row) {
				if (piece == OthelloPiece.BLACK) {
					blackPieces++;
				} else if (piece == OthelloPiece.WHITE) {
					whitePieces++;
				}
			}
		}
		return blackPieces - whitePieces;
	}

	/**
	 * La mobilité est une mesure de la quantité de mouvements disponibles pour
	 * chaque joueur. En général, un joueur avec plus de mouvements disponibles a un
	 * avantage par rapport à un joueur avec moins de mouvements disponibles. Pour
	 * calculer cette mesure, vous pouvez simplement compter le nombre de mouvements
	 * disponibles pour chaque joueur et soustraire le nombre de mouvements
	 * disponibles pour l'autre joueur.
	 * 
	 * Calcule la valeur de l'évaluation basée sur la mobilité dans un jeu d'Othello.
	 * 
	 * @param game Le jeu d'Othello pour lequel calculer l'évaluation basée sur la mobilité.
	 * @return La valeur de l'évaluation basée sur la mobilité du jeu d'Othello.
	 */
	public static Integer computeByMobility(OthelloGame game) {
		List<OthelloMove> moves = game.getValidMoves(game.getCurrentPlayer());
		List<OthelloMove> opponentMoves = game.getValidMoves(game.getOpponentPlayer());
		return moves.size() - opponentMoves.size();
	}

	/*
	 * Justification de la matrice de valuation : Un pion placé dans un coin est
	 * imprenable et constitue donc une solide base de départ pour la conquête des
	 * bords. Les cases bordant le coin sont à éviter car elles donnent à
	 * l’adversaire la possibilité de prendre le coin. Les cases centrales augmente
	 * les possibilités de jeu. Les cases du bords sont également des points d’appui
	 * solides.
	 * 
	 * Calcule la valeur de l'évaluation basée sur la force de position dans un jeu d'Othello.
	 * 
	 * @param game Le jeu d'Othello pour lequel calculer l'évaluation basée sur la force de position.
	 * @return La valeur de l'évaluation basée sur la force de position du jeu d'Othello.
	 */
	public static Integer computeByPositionStrength(OthelloGame game) {
		int[][] valuationMatrix = { 
				{ 500, -150, 30, 10, 10, 30, -150, 500 }, 
				{ -150, -250, 0, 0, 0, 0, -250, -150 },
				{ 30, 0, 1, 2, 2, 1, 0, 30 }, 
				{ 10, 0, 2, 16, 16, 2, 0, 10 }, 
				{ 10, 0, 2, 16, 16, 2, 0, 10 },
				{ 30, 0, 1, 2, 2, 1, 0, 30 }, 
				{ -150, -250, 0, 0, 0, 0, -250, -150 },
				{ 500, -150, 30, 10, 10, 30, -150, 500 } 
		};
		final OthelloBoard board = game.getBoard();
		final int rows = board.getRows();
		final int columns = board.getColumns();
		int positionStrength = 0;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				OthelloPiece piece = board.getPiece(OthelloMove.createMove(row, col));
				if (piece != OthelloPiece.EMPTY) {
					if (piece.getColor() == OthelloColor.BLACK) {
						positionStrength += valuationMatrix[row][col];
					} else if (piece.getColor() == OthelloColor.WHITE) {
						positionStrength += -valuationMatrix[row][col];
					}
				}
			}
		}
		return positionStrength;
	}
	
	/**
	 * Implémentation de l'algorithme Minimax pour l'évaluation heuristique des noeuds dans l'arbre de recherche de l'algorithme d'IA du jeu Othello.
	 * @param node Le noeud à évaluer.
	 * @param depth La profondeur de recherche restante.
	 * @return La valeur heuristique du noeud évalué.
	*/
	public static double minimax(OthelloNode node, int depth) {
		if (depth == 0 || node.getNodeType() == OthelloNodeType.LEAF) {
			final OthelloGame game = node.getGame();
			final Double heuristic = (double) evaluate(game, game.getSearchAlgorithm());
			node.setHeuristic(heuristic);
			return heuristic;
		}
		double value;
		if (node.getPlayerType() == OthelloPlayerType.MAX) {
			value = Double.NEGATIVE_INFINITY;
			for (OthelloNode children : node.getChildren()) {
				value = Math.max(value, minimax(children, depth - 1));
			}
			node.setHeuristic(value);
		} else {
			value = Double.POSITIVE_INFINITY;
			for (OthelloNode children : node.getChildren()) {
				value = Math.min(value, minimax(children, depth - 1));
			}
			node.setHeuristic(value);
		}
		System.out.println(node.getMinifiedData());
		return value;
	}
	
	/**
	 * Implémentation de l'algorithme NegaMax pour l'évaluation heuristique des noeuds dans l'arbre de recherche de l'algorithme d'IA du jeu Othello.
	 * @param node Le noeud à évaluer.
	 * @param depth La profondeur de recherche restante.
	 * @return La valeur heuristique du noeud évalué.
	*/
	public static double negamax(OthelloNode node, int depth) {
		if (depth == 0 || node.getNodeType() == OthelloNodeType.LEAF) {
			final OthelloGame game = node.getGame();
			final Double heuristic = (double) evaluate(game, game.getSearchAlgorithm());
			node.setHeuristic(heuristic);
			return heuristic;
		}
		double value = Double.NEGATIVE_INFINITY;
		for (OthelloNode children : node.getChildren()) {
			value = Math.max(value, -negamax(children, depth - 1));
		}
		return value;
	}
	
	/**
	 * Implémentation de l'algorithme Alpha-Bêta Minimax pour l'évaluation heuristique des noeuds dans l'arbre de recherche de l'algorithme d'IA du jeu Othello.
	 * @param node Le noeud à évaluer.
	 * @param depth La profondeur de recherche restante.
	 * @param alpha La valeur alpha du noeud MAX
	 * @param beta La valeur beta du noeud MIN
	 * @return La valeur heuristique du noeud évalué.
	*/
	public static double minimaxAlphaBeta(OthelloNode node, int depth, double alpha, double beta) {
		if (depth == 0 || node.getNodeType() == OthelloNodeType.LEAF) {
			final OthelloGame game = node.getGame();
			final Double heuristic = (double) evaluate(game, game.getSearchAlgorithm());
			node.setHeuristic(heuristic);
			return heuristic;
		}
		if (node.getPlayerType() == OthelloPlayerType.MAX) {
			double maxValue = Double.NEGATIVE_INFINITY;
			for (OthelloNode children : node.getChildren()) {
				double value = minimaxAlphaBeta(children, depth - 1, alpha, beta);
				maxValue = Math.max(maxValue, value);
	            alpha = Math.max(alpha, maxValue);
				node.setAlpha(alpha);
				if (beta <= alpha) {
					break;
				}
			}
			node.setHeuristic(maxValue);
			System.out.println(node.getMinifiedData());
			return maxValue;
		} else {
			double minValue = Double.POSITIVE_INFINITY;
			for (OthelloNode children : node.getChildren()) {
				double value = minimaxAlphaBeta(children, depth - 1, alpha, beta);
				minValue = Math.min(minValue, value);
	            beta = Math.min(beta, value);
	            node.setBeta(beta);
				if (beta <= alpha) {
					break;
				}
			}
			node.setHeuristic(minValue);
			System.out.println(node.getMinifiedData());
			return minValue;
		}
		/*if (node.getPlayer() == OthelloPlayerType.MAX) {
			int i = 0;
			List<OthelloNode> childrens = node.getChildren();
			int k = childrens.size();
			while (alpha < beta && i <= k) {
				alpha = Math.max(alpha, minimaxAlphaBeta(childrens.get(i), depth - 1, alpha, beta));
				i++;
			}
			return alpha;
		} else {
			int i = 0;
			List<OthelloNode> childrens = node.getChildren();
			int k = childrens.size();
			double minValue = Double.POSITIVE_INFINITY;
			while (alpha < beta && i <= k) {
				double value = minimaxAlphaBeta(childrens.get(i), depth - 1, alpha, beta);
				beta = Math.min(minValue, value);
				i++;
			}
			return beta;
		}*/
		
	}

}
