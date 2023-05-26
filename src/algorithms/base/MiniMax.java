package algorithms.base;

import java.util.Map;

public class MiniMax implements BaseAlgo {

	private String player;

	public MiniMax(String player) {
		this.player = player;
	}

	@Override
	public int start(Node root, int maxDepth) {
		return max(root, maxDepth)[1];
	}

	public int[] max(Node n, int maxDepth) {
		if(n.isTerminal() || maxDepth == 0)
			return new int[] {
					n.getScore(player),
					-1
			};
		int u = -Integer.MAX_VALUE;
		int a = -1;
		Map<Integer, Node> children = n.getChildren();
		for(Integer af : children.keySet()) {
			int eval = min(children.get(af), maxDepth - 1)[0];
			if(eval > u) {
				u = eval;
				a = af;
			}
		}
		return new int[] {
				u, a
		};
	}

	public int[] min(Node n, int maxDepth) {
		if(n.isTerminal() || maxDepth == 0)
			return new int[] {
					n.getScore(player),
					-1
			};
		int u = Integer.MAX_VALUE;
		int a = -1;
		Map<Integer, Node> children = n.getChildren();
		for(Integer af : children.keySet()) {
			int eval = max(children.get(af), maxDepth - 1)[0];
			if(eval < u) {
				u = eval;
				a = af;
			}
		}
		return new int[] {
				u, a
		};
	}
}