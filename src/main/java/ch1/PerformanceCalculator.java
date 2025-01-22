package ch1;

import ch1.data.Performance;
import ch1.data.Play;

public class PerformanceCalculator {

	private final Performance performance;
	private final Play play;

	public PerformanceCalculator(Performance performance, Play play) {
		this.performance = performance;
		this.play = play;
	}

	public Play getPlay() {
		return play;
	}

}
