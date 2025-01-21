package ch1;

import ch1.data.Invoice;
import ch1.data.Performance;
import ch1.data.Play;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Statement {

	Map<String, Play> plays;
	Invoice invoice;
	StatementData data;

	public Statement(Map<String, Play> plays, Invoice invoice) {
		this.plays = plays;
		this.invoice = invoice;
	}

	public String statement() {
		data = new StatementData(
			invoice.customer(),
			invoice.performances()
		);
		return renderPlainText();
	}

	private String renderPlainText() {
		var result = String.format("청구 내역 (고객명: %s)%n", data.customer());
		for (var perf : data.performances()) {
			// 청구 내역 출력
			result += String.format(" %s: %s (%s석)%n",
				playFor(perf).name(),
				usd(amountFor(perf)),
				perf.audience()
			);
		}
		result += String.format("총액: %s%n", usd(totalAmount()));
		result += String.format("적립 포인트: %s점%n", totalVolumeCredits());
		return result;
	}

	private int volumeCreditsFor(Performance performance) {
		var result = 0;
		result += Math.max(performance.audience() - 30, 0);
		if ("comedy".equals(playFor(performance).type())) {
			result += performance.audience() / 5;
		}
		return result;
	}

	private Play playFor(Performance performance) {
		return plays.get(performance.playID());
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

	private String usd(int number) {
		return NumberFormat.getCurrencyInstance(Locale.US).format(number / 100);
	}

	private int totalAmount() {
		var result = 0;
		for (var perf : data.performances()) {
			result += amountFor(perf);
		}
		return result;
	}

	private int totalVolumeCredits() {
		var result = 0;
		for (var perf : data.performances()) {
			result += volumeCreditsFor(perf);
		}
		return result;
	}

	private record StatementData(
		String customer,
		List<Performance> performances
	) {}

}
