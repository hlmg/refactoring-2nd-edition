package ch1;

import ch1.data.Performance;
import ch1.data.Play;

public class PerformanceCalculator {

	protected final Performance performance;
	private final Play play;

	public PerformanceCalculator(Performance performance, Play play) {
		this.performance = performance;
		this.play = play;
	}

	public int amount() {
		throw new UnsupportedOperationException("서브 클래스에서 처리");
	}

	public int volumeCredits() {
		var result = 0;
		result += Math.max(performance.getAudience() - 30, 0);
		if ("comedy".equals(play.type())) {
			result += performance.getAudience() / 5;
		}
		return result;
	}

	public Play getPlay() {
		return play;
	}

}
