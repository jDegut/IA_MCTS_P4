package game;

import java.util.Scanner;

public class Game {
	private static final Scanner scanner = new Scanner(System.in);

	private final String player1;
	private final String player2;
	private String currentPlayer;
	private final Board board;

	public Game(String player1) {
		this.player1 = player1;
		this.player2 = Pion.getOpponent(player1);
		this.currentPlayer = player1;
		this.board = new Board();
		start();
	}

	public void start() {
		System.out.println("Début de la partie");
		while (board.getWinner() == null && board.getPossibleActions().size() > 0) {
			System.out.println("A toi de jouer : " + currentPlayer);
			board.afficher();
			System.out.println("Quel position voulez-vous jouer ?");
			int action = scanner.nextInt();
			play(action);
			if (board.getWinner() != null) {
				board.afficher();
				System.out.println("Bravo " + Pion.getOpponent(currentPlayer) + " tu as gagné !");
				break;
			}
		}
		System.out.println("Fin de la partie");
	}

	private void play(int action) {
		board.addPion(action, currentPlayer);
		currentPlayer = Pion.getOpponent(currentPlayer);
	}

}
