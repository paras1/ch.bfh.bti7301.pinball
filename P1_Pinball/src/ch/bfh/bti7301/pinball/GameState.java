package ch.bfh.bti7301.pinball;

public class GameState {

	private static GameState instance = null;

	private boolean gameInProgress;

	private int ballNumber;
	private int totalBalls = 3;

	private static long score;

	public static GameState getInstance() {
		if (instance == null) {
			instance = new GameState();
		}
		return instance;
	}

	public void startNewGame() {
		score = 0;
		ballNumber = 1;
		gameInProgress = true;
	}

	public void doNextBall() {
		if (ballNumber < totalBalls) {
			++ballNumber;
		} else {
			gameInProgress = false;
		}
	}

	public void addScore(long points) {
		score += points;
	}

	// Getters and Setters

	public boolean isGameInProgress() {
		return gameInProgress;
	}

	public void setGameInProgress(boolean gameInProgress) {
		this.gameInProgress = gameInProgress;
	}

	public int getBallNumber() {
		return ballNumber;
	}

	public void setBallNumber(int ballNumber) {
		this.ballNumber = ballNumber;
	}

	public int getTotalBalls() {
		return totalBalls;
	}

	public void setTotalBalls(int totalBalls) {
		this.totalBalls = totalBalls;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

}
