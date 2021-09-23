package src.scenarios.tictactoe;

import src.network.*;

public class TicGame {
    private int[][] board;
    private int playerMove = 1;
	private String name;
	private static int gamesMade = 0;

    public TicGame(String name) {
        board = new int[3][3];
		this.name = name;
		gamesMade++;
    }

    /**
     * Attempts to make a move for the current player (Alternates the current player)
     * @param x X-coordinate
     * @param y Y-coordinate
	 * @param playerMove Player that moved
     * @return If move was legal
     */
    public boolean makeMove(int x, int y, int playerMove) {
        if(x > 2 || y > 2 || x < 0 || y < 0) { // Out of bounds
            return false;
        }
        if(board[x][y] != 0) { // Already filled square
            return false;
        }

		this.playerMove = playerMove;
        board[x][y] = playerMove;
        return true;
    }

    /**
     * @return -1 for continuing game, 0 for tie, 1 for p1, 2 for p2 
     */
    private int checkWin() {
        // Horizontal win
        for(int[] x : board) {
			int num = 0;
            if(x[0] == playerMove && x[0] == x[1] && x[1] == x[2]) return playerMove;
        }

        // Vertical win
        for(int y = 0; y < 3; y++) {
            if(board[0][y] == playerMove && board[0][y] == board[1][y] && board[1][y] == board[2][y]) return playerMove;
        }

        // Diagonal win
    	if(board[0][0] == playerMove && board[0][0] == board[1][1] && board[1][1] == board[2][2]) return playerMove;
		if(board[0][2] == playerMove && board[0][2] == board[1][1] && board[1][1] == board[2][0]) return playerMove;

        // Is game still being played?
        boolean zeroFlag = false;
        watch:
        for(int[] x : board) {
            for(int y : x) {
                if(y == 0) {
                    zeroFlag = true;
                    break watch;
                }
            }
        }

        if(zeroFlag) return -1;
        else return 0;
    }

    public void printBoard() {
        System.out.print('\n');
		System.out.println(name);
        for(int[] x : board) {
            System.out.print('\n');
            for(int y : x) {
                System.out.print("" + '[' + y + ']');
            }
        }
        
    }

    public double[] createInputs(int player) {
        double[] inputs = new double[9];
        int acc = 0;
        for(int[] x : board) {
            for(int y : x) {
				double out = 0; // Low (enemy)
				if(y == 0) out = 0.5; // Neutral
				else if(y == player) out = 1; // High (player)
                inputs[acc] = out;
                acc++;
            }
        }
        return inputs;
    }

	public GameState getWinner() {
		int winner = checkWin();
		GameState s = GameState.ERROR;
		switch(winner) {
			case 1:
				s = GameState.P1WIN;
				break;
			case 2:
				s = GameState.P2WIN;
				break;
			case 0:
				s = GameState.TIE;
				break;
			case -1:
				s = GameState.CONTINUE;
				break;
			default:
				break;
		}
		return s;
	}
}
