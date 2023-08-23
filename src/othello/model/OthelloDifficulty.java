package othello.model;

/**
 * L'énumération OthelloDifficulty représente les niveaux de difficulté pour le
 * jeu Othello. Chaque niveau de difficulté est associé à un nom et une
 * profondeur de recherche. Les niveaux de difficulté disponibles sont : Facile,
 * Moyen, Difficile, Extrême.
 */
public enum OthelloDifficulty {

	EASY("Façile", 1), MEDIUM("Moyen", 2), HARD("Difficile", 3), EXTREM("Extrême", 4);

	private String name;
	private Integer depth;

	/**
	 * Constructeur de l'énumération OthelloDifficulty.
	 *
	 * @param name  Le nom associé au niveau de difficulté.
	 * @param depth La profondeur de recherche associée au niveau de difficulté.
	 */
	private OthelloDifficulty(String name, Integer depth) {
		this.name = name;
		this.depth = depth;
	}
	
    /**
     * Retourne le nom associé au niveau de difficulté.
     *
     * @return Le nom du niveau de difficulté.
     */
	public String getName() {
		return name;
	}
	
    /**
     * Retourne la profondeur de recherche associée au niveau de difficulté.
     *
     * @return La profondeur de recherche du niveau de difficulté.
     */
	public Integer getDepth() {
		return depth;
	}
	
    /**
     * Modifie le nom associé au niveau de difficulté.
     *
     * @param name Le nouveau nom du niveau de difficulté.
     */
	public void setName(String name) {
		this.name = name;
	}
	
    /**
     * Modifie la profondeur de recherche associée au niveau de difficulté.
     *
     * @param depth La nouvelle profondeur de recherche du niveau de difficulté.
     */
	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	
    /**
     * Recherche un niveau de difficulté par son nom.
     *
     * @param name Le nom du niveau de difficulté à rechercher.
     * @return Le niveau de difficulté correspondant au nom, ou null si aucun niveau de difficulté n'est trouvé.
     */
	public static OthelloDifficulty findByName(String name) {
		for (OthelloDifficulty difficulty : OthelloDifficulty.values()) {
			if (difficulty.getName().equals(name)) {
				return difficulty;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
