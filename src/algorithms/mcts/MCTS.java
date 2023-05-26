package algorithms.mcts;

import algorithms.mcts.Node;
import game.Pion;

public class MCTS {

	private static final double C = Math.sqrt(2);

	private final String player;

	public MCTS(String player) {
		this.player = player;
	}

	public int uctSearch(Node root, int maxIter) {
		for(int i = 0; i < maxIter; i++) {
			Node n = treePolicy(root);
			int delta = defaultPolicy(n);
			backUp(n, delta);
		}
		return bestChild(root, 0).getAction();
	}

	public Node treePolicy(Node n) {
		while(!n.isTerminal()) {
			if(!n.isFullyExpanded())
				return expand(n);
			else
				n = bestChild(n, C);
		}
		return n;
	}

	public Node expand(Node n) {
		int a = n.getUntriedAction();
		return n.addChild(a);
	}

	public Node bestChild(Node n, double c) {
		Node bestChild = null;
		double bestValue = Double.NEGATIVE_INFINITY;
		for(Node child : n.getChildren()) {
			double uctValue = (double) child.getScore() / child.getVisits() + c * Math.sqrt(2 * Math.log(n.getVisits()) / child.getVisits());
			if(uctValue > bestValue) {
				bestChild = child;
				bestValue = uctValue;
			}
		}
		return bestChild;
	}

	public int defaultPolicy(Node n) {
		int scoreTot = 0;
		for(int i = 0; i < 10; i++) {
			Node temp = n.copy();
			while (!temp.isTerminal()) {
				temp.apply(temp.getRandomAction());
			}
			if(temp.getWinner() == null)
				return 0;
			scoreTot += temp.getWinner().equals(Pion.getOpponent(player)) ? 1 : -100;
		}
		return scoreTot;
	}

	public void backUp(Node n, int delta) {
		while(n != null) {
			n.update(delta, player);
			n = n.getParent();
		}
	}
}
