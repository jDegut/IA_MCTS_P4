import algorithms.base.AlphaBeta;
import algorithms.base.BaseAlgo;
import algorithms.base.MiniMax;
import algorithms.mcts.MCTS;
import algorithms.mcts.Node;
import game.Board;
import game.Game;
import game.Pion;

import java.util.Date;
import java.util.Random;

public class Main {
	public static void main(String[] args) {
		Date start = new Date();
//		ratioRandomBase(new MiniMax("O"), 100, 5, "O");
//		ratioRandomBase(new AlphaBeta("O"), 100, 6, "O");
		ratioRandomMCTS(100, 10000, "O");
//		ratioBaseMCTS(new AlphaBeta("O"), new MCTS("X"), 100, 5, 10000,"O");
//		Game g = new Game("O");
		Date end = new Date();
		System.out.println("Temps d'exécution : " + (end.getTime() - start.getTime()) + "ms");
	}

	public static void ratioRandomBase(BaseAlgo algo, int nbIter, int maxDepth, String player) {
		int win = 0;
		int nbNodesTotal = 0;
		for (int i = 0; i < nbIter; i++) {
			Board board = new Board();
			while (board.getWinner() == null && board.getPossibleActions().size() > 0) {
				int b = algo.start(new algorithms.base.Node(null, board, player), maxDepth);
				board.addPion(b, player);
				if (board.getWinner() != null)
					break;
				int a = board.getPossibleActions().get(new Random().nextInt(board.getPossibleActions().size()));
				board.addPion(a, Pion.getOpponent(player));
			}
			System.out.println("Etat final " + i + " : ");
			board.afficher();
			if (board.getWinner() != null && board.getWinner().equals(player))
				win++;
			nbNodesTotal += algo.getNbNodesTotal();
			algo.setNbNodesTotal(0);
		}
		System.out.println("Nombre de noeuds visités en moyenne : " + nbNodesTotal / nbIter);
		System.out.println("Ratio contre un bot aléatoire : " + win + "/" + nbIter + " (" + (win * 100 / nbIter) + "%)");
	}

	public static void ratioRandomMCTS(int nbIter, int maxIter, String player) {
		int win = 0;
		MCTS algo = new MCTS(player);
		int lvl = 0;
		int maxDepth = 0;
		for(int i = 0; i < nbIter; i++) {
			Board board = new Board();
			while(board.getWinner() == null && board.getPossibleActions().size() > 0) {
				int a = algo.uctSearch(new Node(null, board, player), maxIter);
				board.addPion(a, player);
				if(board.getWinner() != null)
					break;
				int b = board.getPossibleActions().get(new Random().nextInt(board.getPossibleActions().size()));
				board.addPion(b, Pion.getOpponent(player));
			}
			System.out.println("Etat final " + i + " : ");
			board.afficher();
			if (board.getWinner() != null && board.getWinner().equals(player))
				win++;
			lvl += algo.getMaxDepth();
			maxDepth = Math.max(maxDepth, algo.getMaxDepth());
			algo.resetMaxDepth();
		}
		System.out.println("Nombre de niveaux moyen : " + lvl / nbIter);
		System.out.println("Profondeur maximale : " + maxDepth);
		System.out.println("Ratio contre un bot aléatoire : " + win + "/" + nbIter + " (" + (win * 100 / nbIter) + "%)");
	}

	public static void ratioBaseMCTS(BaseAlgo algo1, MCTS algo2, int nbIter, int maxDepth, int maxIter, String player) {
		int win = 0;
		for(int i = 0; i < nbIter; i++) {
			Board board = new Board();
			while(board.getWinner() == null && board.getPossibleActions().size() > 0) {
				int a = algo1.start(new algorithms.base.Node(null, board, player), maxDepth);
				board.addPion(a, player);
				if(board.getWinner() != null)
					break;
				int b = algo2.uctSearch(new Node(null, board, Pion.getOpponent(player)), maxIter);
				board.addPion(b, Pion.getOpponent(player));

			}
			System.out.println("Etat final " + i + " : ");
			board.afficher();
			if (board.getWinner() != null && board.getWinner().equals(player))
				win++;
		}
		System.out.println("Ratio contre l'algo " + algo1.getClass().getSimpleName() + " : " + win + "/" + nbIter + " (" + (win * 100 / nbIter) + "%)");
	}
}