package src.scenarios.tictactoe;

import src.network.*;

public class TicTask extends Task {
	private int currentMax;

	public TicTask(Circuit[] circuits, Trainer myTrainer) {
		super(circuits, myTrainer);
	}

	protected double[] calcFitness() {
		double[] game1scores = playGame(circuits[0], circuits[1]);
		double[] game2scores = playGame(circuits[1], circuits[0]);
		// Reverse game2 scores
		double temp = game2scores[0];
		game2scores[0] = game2scores[1];
		game2scores[1] = temp;
		double[] avgScores = {(game1scores[0] + game2scores[0]) / 2, (game1scores[1] + game2scores[1]) / 2};
		return avgScores;
	}

	private double[] playGame(Circuit c1, Circuit c2) {
		TicGame game = new TicGame(c1.toString() + " v " + c2.toString());
		int player = 1;
		int moves = 1;
		double[] gamescores = new double[2];
		watch:
		while(game.getWinner() == GameState.CONTINUE) {
			Circuit current = circuits[player - 1];
			double[] output = current.process(game.createInputs(player));
			// Penalize 
			double penalty = penalizeOutput(output, player, game);
			GameState gs = game.getWinner();
			if(penalty == 0 && gs == GameState.CONTINUE) {
				player = 3 - player; // Alternate
			} else if(gs == GameState.P1WIN) {
				gamescores[0] = 2.0;
				gamescores[1] = 1 + moves / 9;
				break watch;
			} else if(gs == GameState.P2WIN) {
				gamescores[1] = 2.0;
				gamescores[0] = 1 + moves / 9;
				break watch;
			} else if(gs == GameState.TIE) {
				gamescores[0] = 1 + moves / 9;
				gamescores[1] = 1 + moves / 9;
				break watch;
			} else { // Terminate game
				if(player == 1) {
					gamescores[0] = 1 - penalty;
					gamescores[1] = 1 + moves / 9;
				} else {
					gamescores[0] = 1 + moves / 9;
					gamescores[1] = 1 - penalty;
				}
				break watch;
			}
			moves++;
		}
		return gamescores;
	}

	/**
	 * Checks if circuit has make an interpretable move, then makes that move if possible
	 * @returns 0 on no penalties, range from 0-1 for severity of penalty

	*/
	private double penalizeOutput(double[] output, int player, TicGame game) {
		double max = output[0];
		double max2 = output[0];
		int acc = 0;
		int maxPos = 0;
		for(double out : output) {
			if(out > max) {
				max = out;
				maxPos = acc;
			}
			if(out < max && out > max2) max2 = out;
			acc++;
		}
		if(max < 0.1) { // No outputs: 1 - max
			return 1 - max;
		}
		int x = maxPos / 3;
		int y = maxPos % 3;

		if(!game.makeMove(x, y, player)) { // Invalid input: Penalty of 0.4
			return 0.4;
		} else { // No penalties
			return 0;
		}
	}
}