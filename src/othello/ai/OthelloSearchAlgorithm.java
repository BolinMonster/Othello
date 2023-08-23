package othello.ai;

/**
 * Enumération représentant les algorithmes de recherche utilisés par la théorie des jeux sur le jeu Othello.
 * 
 * <ul>
 *   <li>MINMAX : Représente l'algorithme Minimax.</li>
 *   <li>NEGAMAX : Représente l'algorithme Negamax.</li>
 *   <li>ALPHABETA : Représente l'algorithme Alpha-Beta.</li>
 * </ul>
 * 
 */
public enum OthelloSearchAlgorithm {
	
	MINMAX("Minimax", false, false, false),
	ALPHABETA_MINIMAX("AlphaBeta", false, false, false);
	
	private String name;
	private boolean isMaterialEnabled, isMobilityEnabled, isPositionStrengthEnabled;
	
	private OthelloSearchAlgorithm(String name, boolean isMaterialEnabled, boolean isMobilityEnabled, boolean isPositionStrengthEnabled) {
		this.name = name;
		this.isMaterialEnabled = isMaterialEnabled;
		this.isMobilityEnabled = isMobilityEnabled;
		this.isPositionStrengthEnabled = isPositionStrengthEnabled;
	}
	
	/**
     * Renvoie le nom de l'algorithme.
     * 
     * @return Le nom de l'algorithme.
     */
	public String getName() {
		return name;
	}
	
    public boolean isMaterialEnabled() {
		return isMaterialEnabled;
	}

	public boolean isMobilityEnabled() {
		return isMobilityEnabled;
	}

	public boolean isPositionStrengthEnabled() {
		return isPositionStrengthEnabled;
	}
	
    /**
     * Définit le nom de l'algorithme.
     * 
     * @param name Le nouveau nom de l'algorithme.
     */
	public void setName(String name) {
		this.name = name;
	}
	
	public void setMaterialEnabled(boolean isMaterialEnabled) {
		this.isMaterialEnabled = isMaterialEnabled;
	}

	public void setMobilityEnabled(boolean isMobilityEnabled) {
		this.isMobilityEnabled = isMobilityEnabled;
	}

	public void setPositionStrengthEnabled(boolean isPositionStrengthEnabled) {
		this.isPositionStrengthEnabled = isPositionStrengthEnabled;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
