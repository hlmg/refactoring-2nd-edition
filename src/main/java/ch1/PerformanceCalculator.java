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

	public int amount() {
		var result = 0;
		switch (play.type()) {
			case "tragedy":
				result = 40000;
				if (performance.getAudience() > 30) {
					result += 1000 * (performance.getAudience() - 30);
				}
				break;
			case "comedy":
				result = 30000;
				if (performance.getAudience() > 20) {
					result += 10000 + 500 * (performance.getAudience() - 20);
				}
				result += 300 * performance.getAudience();
				break;
			default:
				throw new IllegalArgumentException("알 수 없는 장르: " + play.type());
		}
		return result;
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
