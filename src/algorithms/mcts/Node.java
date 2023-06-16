package algorithms.mcts;

import game.Board;
import game.Pion;

import java.util.*;

public class Node {

	private final Node parent;
	private final List<Node> children;
	private final Board board;
	private String player;
	private int score;
	private int action;
	private int visits;

	public Node(Node parent, Board board, String player) {
		this.parent = parent;
		this.children = new ArrayList<>();
		this.board = board;
		this.player = player;
		this.score = 0;
		this.visits = 0;
	}

	public String getPlayer() {
		return player;
	}

	public Node addChild(int action) {
		Board copy = board.copy();
		copy.addPion(action, player);
		Node child = new Node(this, copy, Pion.getOpponent(player));
		child.setAction(action);
		children.add(child);
		return child;
	}

	public void update(int delta) {
		this.visits++;
		this.score += delta;
	}

	public Node getParent() {
		return parent;
	}

	public List<Node> getChildren() {
		return children;
	}

	public int getScore() {
		return score;
	}

	public boolean isTerminal() {
		return board.getWinner() != null || board.getPossibleActions().size() == 0;
	}

	public boolean isFullyExpanded() {
		return children.size() == board.getPossibleActions().size();
	}

	public int getAction() {
		return action;
	}

	public int getUntriedAction() {
		List<Integer> possibleActions = board.getPossibleActions();
		for(Node child : children) {
			possibleActions.remove((Integer) child.getAction());
		}
		return possibleActions.get((int) (Math.random() * possibleActions.size()));
	}

	public int getRandomAction() {
		List<Integer> possibleActions = board.getPossibleActions();
		return possibleActions.get(new Random().nextInt(possibleActions.size()));
	}

	public void apply(int action) {
		board.addPion(action, player);
		this.player = Pion.getOpponent(player);
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getWinner() {
		return board.getWinner();
	}

	public int getVisits() {
		return visits;
	}

	public Node copy() {
		return new Node(parent, board.copy(), player);
	}
}
