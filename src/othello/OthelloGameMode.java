package othello;

public enum OthelloGameMode {
	
	PLAYER_VS_PLAYER("Joueur contre Joueur"),
	PLAYER_VS_MACHINE("Joueur contre Machine");
	
	private static final long serialVersionUID = 1L;
	
	private String gameModeName;
	
	private OthelloGameMode(String gameModeName) {
		this.gameModeName = gameModeName;
	}
	
	public String getGameModeName() {
		return this.gameModeName;
	}
	
	public static OthelloGameMode findByGameModeName(String gameModeName) {
	    for (OthelloGameMode mode : OthelloGameMode.values()) {
	        if (mode.getGameModeName().equals(gameModeName)) {
	            return mode;
	        }
	    }
	    return null;
	}

	@Override
	public String toString() {
        return "OthelloGameMode [gameModeName=" + getGameModeName() + "]";
	}
}
