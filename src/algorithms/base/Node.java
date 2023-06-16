package algorithms.base;

import game.Board;
import game.Pion;

import java.util.HashMap;
import java.util.Map;

public class Node {

	private final Node parent;
	private final Board board;
	private final String player;
	private final boolean isTerminal;

	public Node(Node parent, Board board, String player) {
		this.parent = parent;
		this.board = board;
		this.player = player;
		this.isTerminal = board.getWinner() != null || board.getPossibleActions().size() == 0;
	}

	private Map<Integer, Node> generateChildren() {
		Map<Integer, Node> children = new HashMap<>();
		for(Integer i : board.getPossibleActions()) {
			Board copy = board.copy();
			copy.addPion(i, player);
			children.put(i, new Node(this, copy, Pion.getOpponent(player)));
		}
		return children;
	}

	public Map<Integer, Node> getChildren() {
		return isTerminal ? null : generateChildren();
	}

	public int getScore(String player) {
		return this.board.evaluate(player);
	}

	public boolean isTerminal() {
		return isTerminal;
	}
}