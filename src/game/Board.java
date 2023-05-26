package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Board {
	private static final int BOARD_SIZE = 7;
	private static final int BOARD_HEIGHT = 6;

	private final Pion[][] board;

	public Board() {
		this.board = new Pion[BOARD_HEIGHT][BOARD_SIZE];
	}

	private boolean isEmpty(int i, int j) {
		return this.board[i][j] == null;
	}

	public List<Integer> getPossibleActions() {
		List<Integer> indexes = new ArrayList<>();
		for(int i = 0; i < BOARD_SIZE; i++) {
			if(board[0][i] == null) indexes.add(i);
		}
		return indexes;
	}

	public int getDeeperIndex(int j) {
		int i = -1;
		while (i+1 < 6 && this.isEmpty(i+1, j)) {
			i++;
		}
		return i;
	}

	public void addPion(int j, String player) {
		int i = this.getDeeperIndex(j);
		if(i == -1) {
			System.out.println("Colonne pleine");
			return;
		}
		if(this.isEmpty(i, j)) {
			this.board[i][j] = new Pion(player);
		}
	}

	public void afficher() {
		for (Pion[] pions : board) {
			System.out.print("|");
			for (Pion pion : pions) {
				if (pion == null) {
					System.out.print(" ");
				} else {
					System.out.print(pion.color);
				}
				System.out.print("|");
			}
			System.out.println();
		}
		System.out.println("---------------");
	}

	public String checkWin() {
		boolean win;
		for(String p : Pion.PLAYER_LIST) {
			win = checkHorizontal(p);
			if (win) return p;
			win = checkVertical(p);
			if (win) return p;
			win = checkDiagonal(p);
			if (win) return p;
		}
		return null;
	}

	private boolean checkHorizontal(String player) {
		for(int i = BOARD_HEIGHT - 1; i >= 0; i--) {
			for(int j = 0; j < BOARD_SIZE - 3; j++) {
				if(board[i][j] == null || board[i][j+1] == null
						|| board[i][j+2] == null || board[i][j+3] == null)
					continue;
				if(board[i][j].color.equals(player) &&
						board[i][j+1].color.equals(player) &&
						board[i][j+2].color.equals(player) &&
						board[i][j+3].color.equals(player))
					return true;
			}
		}
		return false;
	}

	private boolean checkVertical(String player) {
		for(int j = 0; j < BOARD_SIZE; j++) {
			for(int i = BOARD_HEIGHT - 1; i >= 3; i--) {
				if(board[i][j] == null || board[i-1][j] == null
						|| board[i-2][j] == null || board[i-3][j] == null) {
					continue;
				}
				if(board[i][j].color.equals(player) &&
						board[i-1][j].color.equals(player) &&
						board[i-2][j].color.equals(player) &&
						board[i-3][j].color.equals(player))
					return true;
			}
		}
		return false;
	}

	private boolean checkDiagonal(String player) {
		// check for diagonal /
		for(int i = 3; i < BOARD_HEIGHT; i++) {
			for(int j = 0; j < BOARD_SIZE - 3; j++) {
				if(board[i][j] == null || board[i-1][j+1] == null
						|| board[i-2][j+2] == null || board[i-3][j+3] == null)
					continue;
				if(board[i][j].color.equals(player) &&
						board[i-1][j+1].color.equals(player) &&
						board[i-2][j+2].color.equals(player) &&
						board[i-3][j+3].color.equals(player))
					return true;
			}
		}

		// check for diagonal \
		for(int i = 3; i < BOARD_HEIGHT; i++) {
			for(int j = 3; j < BOARD_SIZE; j++) {
				if(board[i][j] == null || board[i-1][j-1] == null
						|| board[i-2][j-2] == null || board[i-3][j-3] == null)
					continue;
				if(board[i][j].color.equals(player) &&
						board[i-1][j-1].color.equals(player) &&
						board[i-2][j-2].color.equals(player) &&
						board[i-3][j-3].color.equals(player))
					return true;
			}
		}
		return false;
	}

	public int evaluate(String player) {
		int score = 0;
		String opponent = Pion.getOpponent(player);
		score += evaluateHorizontal(player) + evaluateVertical(player) + evaluateDiagonal(player);
		score -= (evaluateHorizontal(opponent) + evaluateVertical(opponent) + evaluateDiagonal(opponent));
		return score;
	}

	private int evaluateHorizontal(String player) {
		int score = 0;
		Pion[] pions = new Pion[4];
		for(int i = BOARD_HEIGHT - 1; i >= 0; i--) {
			for(int j = 0; j < BOARD_SIZE - 3; j++) {
				for(int k = 0; k < 4; k++) {
					pions[k] = board[i][j+k];
				}
				score += evaluateTab(pions, player);
			}
		}
		return score;
	}

	private int evaluateVertical(String player) {
		int score = 0;
		Pion[] pions = new Pion[4];
		for(int j = 0; j < BOARD_SIZE; j++) {
			for(int i = BOARD_HEIGHT - 1; i >= 3; i--) {
				for(int k = 0; k < 4; k++) {
					pions[k] = board[i-k][j];
				}
				score += evaluateTab(pions, player);
			}
		}
		return score;
	}

	private int evaluateDiagonal(String player) {
		int score = 0;
		Pion[] pions = new Pion[4];
		// check for diagonal /
		for(int i = 3; i < BOARD_HEIGHT; i++) {
			for(int j = 0; j < BOARD_SIZE - 3; j++) {
				for(int k = 0; k < 4; k++) {
					pions[k] = board[i-k][j+k];
				}
				score += evaluateTab(pions, player);
			}
		}

		// check for diagonal \
		for(int i = 3; i < BOARD_HEIGHT; i++) {
			for(int j = 3; j < BOARD_SIZE; j++) {
				for(int k = 0; k < 4; k++) {
					pions[k] = board[i-k][j-k];
				}
				score += evaluateTab(pions, player);
			}
		}
		return score;
	}

	// On vérifie le tableau de 4 pions de manière efficace pour évaluer (d'après la stratégie du prof)
	private int evaluateTab(Pion[] pions, String player) {
		if(Arrays.stream(pions)
				.allMatch(Objects::isNull))
			return 0;
		if(Arrays.stream(pions).filter(Objects::nonNull)
				.filter(p -> p.color.equals(player))
				.count() == 4) {
			return 1000;
		}
		if(Arrays.stream(pions).filter(Objects::nonNull)
				.filter(p -> p.color.equals(player))
				.count() == 3
				&& Arrays.stream(pions).filter(Objects::isNull)
				.count() == 1)
			return 50;
		if(Arrays.stream(pions).filter(Objects::nonNull)
				.filter(p -> p.color.equals(player))
				.count() == 2
				&& Arrays.stream(pions).filter(Objects::isNull)
				.count() == 2)
			return 5;
		if(Arrays.stream(pions).filter(Objects::nonNull)
				.filter(p -> p.color.equals(player))
				.count() == 1
				&& Arrays.stream(pions).filter(Objects::isNull)
				.count() == 3)
			return 1;
		return 0;
	}

	public Board copy() {
		Board clone = new Board();
		for(int i = 0; i < 6; i++) {
			System.arraycopy(board[i], 0, clone.board[i], 0, 7);
		}
		return clone;
	}
}
