package src.scenarios.tictactoe;

public enum GameState{
	ERROR(-1),
	CONTINUE(0),
	P1WIN(1),
	P2WIN(2),
	TIE(3);

	public final int value;

	GameState(int value) {
		this.value = value;
	}
}