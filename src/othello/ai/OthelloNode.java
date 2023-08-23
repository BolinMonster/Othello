package othello.ai;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import othello.OthelloGame;
import othello.ai.OthelloNode;
import othello.model.OthelloBoard;
import othello.model.OthelloMove;
import othello.model.OthelloPlayer;

/**
 * Classe représentant un noeud dans un arbre de jeu d'Othello pour la théorie des jeux.
 * 
 * Cette classe est utilisée pour représenter un noeud dans un arbre de jeu d'Othello utilisé par les algorithmes de la théorie des jeux .
 * Elle contient des informations sur le type de joueur, le type de noeud, l'heuristique, le facteur de branchement, les mouvements possibles et l'état du plateau à ce noeud.
 * Elle offre également des méthodes pour ajouter ou supprimer des enfants, obtenir les informations sur le noeud, ainsi que pour copier en profondeur des tableaux de nombres et générer une représentation sous forme de chaîne de caractères.
*/
public class OthelloNode implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static Integer nodeCount = 0;
	private Integer nodeNum;
	// Etiquette
	private OthelloPlayerType playerType;
	// Statut
	private OthelloNodeType nodeType;
	// heuristque
	private Double heuristic;
	// branchingFactor = le nombre de branches d'un noeud mais pas utiliser car children = list
	private Integer branchFactor;
	// Fils
	private List<OthelloNode> children;
	// l'état du jeu à ce noeud
	private OthelloGame game;
	// le coup joué sur ce noeud
    private AbstractMap.SimpleEntry<OthelloPlayer, OthelloMove> playerMoveEntry;
    
    // Algorithme MinMax Alpha Beta
    private Double alpha;
    private Double beta;
	
	/**
     * Constructeur par défaut.
     * 
     * Initialise les valeurs par défaut pour les attributs du noeud.
     */
	public OthelloNode() {
		this.nodeNum = OthelloNode.nodeCount++;
		this.playerType = null;
		this.nodeType = OthelloNodeType.LEAF;
		this.branchFactor = 0;
		this.heuristic = 0.0;
		this.children = new ArrayList<OthelloNode>();
		this.game = null;
		this.playerMoveEntry = null;
		this.alpha = null;
		this.beta = null;
	}
	
	/**
	 * Constructeur avec paramètres.
	 * 
	 * @param playerType Le type de joueur pour ce noeud.
	 * @param nodeType Le type de noeud.
	 * @param heuristic L'heuristique pour ce noeud.
	 * @param bf Le facteur de branchement pour ce noeud.
	 */
	/*
	public OthelloNode(Integer a, OthelloPlayerType playerType, OthelloNodeType nodeType, double heuristic, int bf) {
		this.playerType = playerType;
		this.nodeType = nodeType;
		this.heuristic = heuristic;
		this.branchFactor = bf;
		this.children = new ArrayList<OthelloNode>();
	}
	*/

	public Double getAlpha() {
		return alpha;
	}

	public Double getBeta() {
		return beta;
	}

	public void setAlpha(Double alpha) {
		this.alpha = alpha;
	}

	public void setBeta(Double beta) {
		this.beta = beta;
	}

	/**
	 * Renvoie le nombre total de noeuds créés.
	 * 
	 * @return Le nombre total de noeuds créés.
	 */
	public static Integer getNodeCount() {
		return nodeCount;
	}
	
	/**
	 * Renvoie le numéro de ce noeud.
	 * 
	 * @return Le numéro de ce noeud.
	 */
	public Integer getNodeNum() {
		return nodeNum;
	}
	
	/**
	 * Renvoie le type de joueur pour ce noeud.
	 * 
	 * @return Le type de joueur pour ce noeud.
	 */
	public OthelloPlayerType getPlayerType() {
		return playerType;
	}
	
	/**
	 * Renvoie le type de noeud.
	 * 
	 * @return Le type de noeud.
	 */
	public OthelloNodeType getNodeType() {
		return nodeType;
	}
	
	/**
	 * Renvoie l'heuristique pour ce noeud.
	 * 
	 * @return L'heuristique pour ce noeud.
	 */
	public Double getHeuristic() {
		return heuristic;
	}
	
	 /**
     * Renvoie le facteur de branchement de ce noeud.
     * 
     * @return Le facteur de branchement de ce noeud.
     */
	public Integer getBranchFactor() {
		return branchFactor;
	}
	
	/**
	 * Renvoie une copie de la liste des enfants de ce noeud.
	 * 
	 * @return Une copie de la liste des enfants de ce noeud.
	 */
	public List<OthelloNode> getChildren() {
		return new ArrayList<OthelloNode>(children);
	}
	
	/**
	 * Récupère l'état du jeu à ce noeud.
	 * 
	 * @return L'état du jeu à ce noeud.
	 */
	public OthelloGame getGame() {
		return game;
	}
	
	/**
	 * Définit le nombre total de noeuds créés.
	 * 
	 * @param nodeCount Le nombre total de noeuds créés à définir.
	 */
	public static void setNodeCount(Integer nodeCount) {
		OthelloNode.nodeCount = nodeCount;
	}
	
	/**
	 * Définit le numéro de ce noeud.
	 * 
	 * @param nodeNum Le numéro de ce noeud à définir.
	 */
	public void setNodeNum(Integer nodeNum) {
		this.nodeNum = nodeNum;
	}
	
	/**
	 * Définit le type de joueur pour ce noeud.
	 * 
	 * @param playerType Le type de joueur à définir pour ce noeud.
	 */
	public void setPlayerType(OthelloPlayerType playerType) {
		this.playerType = playerType;
	}
	
	/**
	 * Définit le type de noeud.
	 * 
	 * @param nodeType Le type de noeud à définir.
	 */
	public void setNodeType(OthelloNodeType nodeType) {
		this.nodeType = nodeType;
	}
	
	/**
	 * Définit l'heuristique pour ce noeud.
	 * 
	 * @param heuristic L'heuristique à définir pour ce noeud.
	 */
	public void setHeuristic(Double heuristic) {
		this.heuristic = heuristic;
	}
	
	/**
     * Définit le facteur de branchement de ce noeud.
     * 
     * @param branchFactor Le facteur de branchement à définir.
     */
	public void setBranchFactor(Integer branchFactor) {
		this.branchFactor = branchFactor != null ? branchFactor : this.branchFactor;
	}
	
	/**
	 * Définit la liste des enfants de ce noeud.
	 * 
	 * @param children La liste des enfants à définir pour ce noeud.
	 */
	public void setChildren(List<OthelloNode> children) {
		this.children = children;
		setBranchFactor(children.size());
	}
	
	/**
     * Ajoute un noeud enfant à la liste des enfants de ce noeud.
     * 
     * @param node Le noeud à ajouter comme enfant.
     * @return true si l'ajout a réussi, sinon false.
     */
	public boolean addChildren(OthelloNode node) {
		return children.add(node);
	}
	
	/**
     * Supprime un noeud enfant de la liste des enfants de ce noeud.
     * 
     * @param node Le noeud à supprimer comme enfant.
     * @return true si la suppression a réussi, sinon false.
     */
	public boolean removeChildren(OthelloNode node) {
		return children.remove(node);
	}
	
	/**
	 * Définit l'état du jeu à ce noeud.
	 * 
	 * @param board L'état du jeu à définir.
	 */
	public void setGame(OthelloGame game) {
		this.game = game;
	}
	
	public AbstractMap.SimpleEntry<OthelloPlayer, OthelloMove> getPlayerMoveEntry() {
		return playerMoveEntry;
	}

	public void setPlayerMoveEntry(AbstractMap.SimpleEntry<OthelloPlayer, OthelloMove> playerMoveEntry) {
		this.playerMoveEntry = playerMoveEntry;
	}
	
	public OthelloPlayer getPlayerEntry() {
		return playerMoveEntry != null ? playerMoveEntry.getKey() : null;
	}
	
	public OthelloMove getMoveEntry() {
		return playerMoveEntry != null ? playerMoveEntry.getValue() : null;
	}
	
	/**
	 * Effectue une copie en profondeur d'un tableau de nombres.
	 * 
	 * @param number Le tableau de nombres à copier.
	 * @return Une copie en profondeur du tableau de nombres.
	 */
	private Double[] deepCopyNumber(Double[] number) {
		final int n = number.length;
		Double[] copy = new Double[n];
		for (int i = 0; i < n; i++) {
			copy[i] = number[i];
		}
		return copy;
	}

	public String getMinifiedData() {
		final StringBuilder sb = new StringBuilder();
		sb.append("[num:")
		        .append(getNodeNum())
		        .append(", type: ")
		        .append(getPlayerType().name())
		        .append(", joueur:")
		        .append(getPlayerMoveEntry().getKey().getName())
		        .append(", h:")
		        .append(getHeuristic())
		        .append("]");
		return sb.toString();
	}
	
	@Override
	public String toString() {
	    final StringBuilder sb = new StringBuilder();
	    sb.append("Num de noeud : ").append(getNodeNum()).append("\n");
	    sb.append("Type de noeud : ").append(getNodeType().name()).append("\n");
	    sb.append("Type de noeud du joueur : ").append(getPlayerType().name()).append("\n");
	    sb.append("Heuristique : ").append(getHeuristic().toString()).append("\n");
	    if (alpha != null) {
		    sb.append("Alpha α : ").append(getAlpha().toString()).append("\n");
	    }
	    if (beta != null) {
		    sb.append("Bêta β : ").append(getBeta().toString()).append("\n");
	    }
	    sb.append("Facteur de branchement : ").append(branchFactor.toString()).append("\n"); 
    	final OthelloPlayer player = getPlayerEntry();
    	final OthelloMove move = getMoveEntry();
    	if (player != null) {
    	    sb.append("Joueur : ").append(player.getName()).append("\n");
    	}
    	if (move != null) {
    	    sb
    	    	.append("Ligne : ")
    	    		.append(move.getRow())
    	    	.append(", Colonne : ")
    	    	.append(move.getColumn())
    	    .append("\n");
    	}
	    /*if (!children.isEmpty()) {
	        sb.append("Enfants : \n");
	        for (OthelloNode child : children) {
	            sb.append("\t" + child.toString());
	        }
	    }*/
	    return sb.toString();
	}
}
