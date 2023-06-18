package algorithms.mcts;

public class MCTS {

	private static final double C = Math.sqrt(2);

	private final String player;
	private int maxDepth;

	public MCTS(String player) {
		this.player = player;
		this.maxDepth = 0;
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
			if(temp.getWinner() != null)
				scoreTot += temp.getWinner().equals(player) ? 1 : -1;
		}
		return scoreTot;
	}

	public void backUp(Node n, int delta) {
		int depth = 0;
		while(n != null) {
			n.update(delta);
			n = n.getParent();
			depth++;
		}
		if(depth > maxDepth)
			maxDepth = depth;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void resetMaxDepth() {
		maxDepth = 0;
	}
}
