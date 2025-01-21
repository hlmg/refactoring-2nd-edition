package ch1.data;

public record Performance(String playID, int audience) {

	public Performance copy() {
		return new Performance(playID, audience);
	}

}
