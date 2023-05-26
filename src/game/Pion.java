package game;

public class Pion {

	public static final String[] PLAYER_LIST = new String[]	{"O", "X"};

	public String color;

	public Pion(String color) {
		this.color = color;
	}

	public static String getOpponent(String player) {
		return player.equals(PLAYER_LIST[0]) ? PLAYER_LIST[1] : PLAYER_LIST[0];
	}

}
