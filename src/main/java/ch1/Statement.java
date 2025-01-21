package ch1;

import ch1.data.Invoice;
import ch1.data.Performance;
import ch1.data.Play;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Statement {

	Map<String, Play> plays;
	Invoice invoice;

	public Statement(Map<String, Play> plays, Invoice invoice) {
		this.plays = plays;
		this.invoice = invoice;
	}

	public String statement() {
		var totalAmount = 0;
		var volumeCredits = 0;
		var result = String.format("청구 내역 (고객명: %s)%n", invoice.customer());
		var format = NumberFormat.getCurrencyInstance(Locale.US);
		for (var perf : invoice.performances()) {
			volumeCredits += volumeCreditsFor(perf);

			// 청구 내역 출력
			result += String.format(" %s: %s (%s석)%n",
				playFor(perf).name(),
				format.format(amountFor(perf) / 100),
				perf.audience()
			);
			totalAmount += amountFor(perf);
		}
		result += String.format("총액: %s%n", format.format(totalAmount / 100));
		result += String.format("적립 포인트: %s점%n", volumeCredits);
		return result;
	}

	private int volumeCreditsFor(Performance performance) {
		var volumeCredits = 0;
		volumeCredits += Math.max(performance.audience() - 30, 0);
		if ("comedy".equals(playFor(performance).type())) {
			volumeCredits += performance.audience() / 5;
		}
		return volumeCredits;
	}

	private Play playFor(Performance perf) {
		return plays.get(perf.playID());
	}

	private int amountFor(Performance performance) {
		var result = 0;
		switch (playFor(performance).type()) {
			case "tragedy":
				result = 40000;
				if (performance.audience() > 30) {
					result += 1000 * (performance.audience() - 30);
				}
				break;
			case "comedy":
				result = 30000;
				if (performance.audience() > 20) {
					result += 10000 + 500 * (performance.audience() - 20);
				}
				result += 300 * performance.audience();
				break;
			default:
				throw new IllegalArgumentException("알 수 없는 장르: " + playFor(performance).type());
		}
		return result;
	}

}
