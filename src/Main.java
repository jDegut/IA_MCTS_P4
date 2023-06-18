import algorithms.base.AlphaBeta;
import algorithms.base.BaseAlgo;
import algorithms.base.MiniMax;
import algorithms.mcts.MCTS;
import algorithms.mcts.Node;
import game.Board;
import game.Game;
import game.Pion;

import java.util.Random;

public class Main {
	public static void main(String[] args) {
//		ratioRandomBase(new AlphaBeta("O"), 100, 5, "O");
		ratioRandomMCTS(100, 1000, "O");
//		ratioBaseMCTS(new AlphaBeta("X"), 1, 5, 100000,"O");
//		Game g = new Game("O");
	}

	public static void ratioRandomBase(BaseAlgo algo, int nbIter, int maxDepth, String player) {
		int win = 0;
		for (int i = 0; i < nbIter; i++) {
			Board board = new Board();
			while (board.getWinner() == null && board.getPossibleActions().size() > 0) {
				int a = board.getPossibleActions().get(new Random().nextInt(board.getPossibleActions().size()));
				board.addPion(a, Pion.getOpponent(player));
				if (board.getWinner() != null)
					break;
				int b = algo.start(new algorithms.base.Node(null, board, player), maxDepth);
				board.addPion(b, player);
			}
			System.out.println("Etat final " + i + " : ");
			board.afficher();
			if (board.getWinner() != null && board.getWinner().equals(player))
				win++;
		}
		System.out.println("Ratio contre un bot aléatoire : " + win + "/" + nbIter + " (" + (win * 100 / nbIter) + "%)");
	}

	public static void ratioRandomMCTS(int nbIter, int maxIter, String player) {
		int win = 0;
		MCTS algo = new MCTS(player);
		for(int i = 0; i < nbIter; i++) {
			Board board = new Board();
			while(board.getWinner() == null && board.getPossibleActions().size() > 0) {
				int b = board.getPossibleActions().get(new Random().nextInt(board.getPossibleActions().size()));
				board.addPion(b, Pion.getOpponent(player));
				if(board.getWinner() != null)
					break;
				int a = algo.uctSearch(new Node(null, board, player), maxIter);
				board.addPion(a, player);
			}
			System.out.println("Etat final " + i + " : ");
			board.afficher();
			if (board.getWinner() != null && board.getWinner().equals(player))
				win++;
		}
		System.out.println("Ratio contre un bot aléatoire : " + win + "/" + nbIter + " (" + (win * 100 / nbIter) + "%)");
	}

	public static void ratioBaseMCTS(BaseAlgo algo1, int nbIter, int maxDepth, int maxIter, String player) {
		int win = 0;
		MCTS algo2 = new MCTS(player);
		for(int i = 0; i < nbIter; i++) {
			Board board = new Board();
			while(board.getWinner() == null && board.getPossibleActions().size() > 0) {
				int a = algo1.start(new algorithms.base.Node(null, board, Pion.getOpponent(player)), maxDepth);
				board.addPion(a, Pion.getOpponent(player));
				board.afficher();
				if(board.getWinner() != null)
					break;
				int b = algo2.uctSearch(new Node(null, board, player), maxIter);
				board.addPion(b, player);
				board.afficher();
			}
			System.out.println("Etat final " + i + " : ");
			board.afficher();
			if (board.getWinner() != null && board.getWinner().equals(player))
				win++;
		}
		System.out.println("Ratio contre l'algo " + algo1.getClass().getSimpleName() + " : " + win + "/" + nbIter + " (" + (win * 100 / nbIter) + "%)");
	}
}