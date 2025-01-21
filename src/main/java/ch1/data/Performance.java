package ch1.data;

public class Performance {

	private String playID;
	private int audience;
	private Play play;

	public Performance() {
	}

	public Performance(String playID, int audience) {
		this.playID = playID;
		this.audience = audience;
	}

	public Performance copy() {
		return new Performance(playID, audience);
	}

	public String getPlayID() {
		return playID;
	}

	public int getAudience() {
		return audience;
	}

	public Play getPlay() {
		return play;
	}

	public void setPlay(Play play) {
		this.play = play;
	}

}
