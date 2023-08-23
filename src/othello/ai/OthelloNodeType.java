package othello.ai;

/**
 * Enumération représentant les types de noeuds pour les algorithmes de la théorie des jeux sur le jeu Othello.
 * 
 * <ul>
 *   <li>LEAF : Représente un noeud feuille dans l'arbre de recherche de coup(s).</li>
 *   <li>INTERNAL_NODE : Représente un noeud interne dans l'arbre de coup(s).</li>
 * </ul>
 * 
 */
public enum OthelloNodeType {
	LEAF, INTERNAL_NODE
}
