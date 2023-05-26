package algorithms.base;

import java.util.Map;

public class AlphaBeta implements BaseAlgo {

	private String player;

	public AlphaBeta(String player) {
		this.player = player;
	}

	@Override
	public int start(Node root, int maxDepth) {
		return max(root, maxDepth, -Integer.MAX_VALUE, Integer.MAX_VALUE)[1];
	}

	public int[] max(Node n, int maxDepth, int alpha, int beta) {
		if(n.isTerminal() || maxDepth == 0)
			return new int[] {
					n.getScore(player),
					-1
			};
		int u = -Integer.MAX_VALUE;
		int a = -1;
		Map<Integer, Node> children = n.getChildren();
		for(Integer af : children.keySet()) {
			int eval = min(children.get(af), maxDepth - 1, alpha, beta)[0];
			if(eval > u) {
				u = eval;
				a = af;
			}
			if(u >= beta)
				return new int[] {
						u, a
				};
			alpha = Math.max(alpha, u);
		}
		return new int[] {
				u, a
		};
	}

	public int[] min(Node n, int maxDepth, int alpha, int beta) {
		if(n.isTerminal() || maxDepth == 0)
			return new int[] {
					n.getScore(player),
					-1
			};
		int u = Integer.MAX_VALUE;
		int a = -1;
		Map<Integer, Node> children = n.getChildren();
		for(Integer af : children.keySet()) {
			int eval = max(children.get(af), maxDepth - 1, alpha, beta)[0];
			if(eval < u) {
				u = eval;
				a = af;
			}
			if(u <= alpha)
				return new int[] {
						u, a
				};
			beta = Math.min(beta, u);
		}
		return new int[] {
				u, a
		};
	}
}
