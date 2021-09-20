package src.scenarios.tictactoe;

import src.network.Circuit;
import src.network.Task;
import src.network.TrainingTypes.*;

public class TicGame extends AdversarialMultirun {
    private int[][] board;
    private int playerMove = 1;
    public Circuit player1;
    public Circuit player2;

    /* Game board:
     x   0 1 2
    y  0
       1
       2

    */

    public TicGame(Circuit player1, Circuit player2) {
        super(9, 9, 2);
        board = new int[3][3];
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Attempts to make a move for the current player (Alternates the current player)
     * @param x X-coordinate
     * @param y Y-coordinate
     * @return If move was legal
     */
    public boolean makeMove(int x, int y) {
        if(x > 2 || y > 2 || x < 0 || y < 0) { // Out of bounds
            return false;
        }
        if(board[x][y] != 0) { // Already filled square
            return false;
        }

        board[x][y] = playerMove;
        playerMove = 3 - playerMove; // Alternate player
        return true;
    }

    /**
     * @return -1 for continuing game, 0 for tie, 1 for p1, 2 for p2 
     */
    public int checkWin() {
        // Horizontal win
        for(int[] x : board) {
            int acc = 0;
            for(int y : x) {
                acc += y;
            }
            if(acc / 3 == 1) return 1;
            if(acc / 3 == 2) return 2;
        }

        // Vertical win
        for(int y = 0; y < 3; y++) {
            int acc = 0;
            for(int[] x : board) {
                acc += x[y];
            }
            if(acc / 3 == 1) return 1;
            if(acc / 3 == 2) return 2;
        }

        // Diagonal win
        for(int xy = 0; xy < 3; xy++) {
            int acc = 0;
            int invertAcc = 0;
            for(int i = 0; i < 3; i++) {
                acc += board[xy][xy];
                invertAcc += board[xy][2-xy];
            }
            if(acc / 3 == 1) return 1;
            if(acc / 3 == 2) return 2;
            if(invertAcc / 3 == 1) return 1;
            if(invertAcc / 3 == 2) return 2;
        }

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
        for(int[] x : board) {
            System.out.print('\n');
            for(int y : x) {
                System.out.print("" + '[' + y + ']');
            }
        }
        System.out.print('\n');
    }

    public double[] createInputs() {
        double[] inputs = new double[9];
        int acc = 0;
        for(int[] x : board) {
            for(int y : x) {
                inputs[acc] = y;
                acc++;
            }
        }
        return inputs;
    }

    /**
     * Returns -1 to continue game, 0 for no outputs, 0.2 for multiple outputs, and 0.25 for an invalid input
     * @param output Array containing the circuit output
     * @return -1 to continue, a valid fitness to quit
     */
    public double processOutput(double[] output) {
        double max = output[0];
        int maxLocation = 0;
        double max2 = 0;

        for(int x = 0; x < output.length; x++) {
            if(output[x] > max) {
                max2 = max;
                max = output[x];
            }
        }
        if(max < 0.5) return 0; // Penalize no outputs (Max score = 0)
        if(max - max2 <= 0.4) return (max - max2) / 2; // Penalize multiple outputs (If difference is less than/equals 0.4) (Max score = 0.2)
        int x = maxLocation / 3;
        int y = maxLocation % 3;
        if(!makeMove(x, y)) return 0.3;// Penalize invalid input

        return -1;
    }

    @Override
    protected double[] getFitnessFromCircuits(Circuit[] circuits) {
        Task current;
        double[] scores = {-1,-1};
        while(checkWin() == -1 && scores[0] == -1 && scores[2] == -1) {
            current = this.startCircuitTask(circuits[playerMove - 1], createInputs(), "Tic Tac Toe player " + (playerMove + 1));
            scores[playerMove - 1] = processOutput(current.getResults());
        }
        if(scores[0] == -1) scores[0] = 0;
        if(scores[1] == -1) scores[1] = 0;
        return scores;
    }
}
