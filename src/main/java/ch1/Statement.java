package ch1;

import ch1.data.Invoice;
import ch1.data.Performance;
import ch1.data.Play;
import ch1.data.StatementData;

import java.text.NumberFormat;
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
		data = new StatementData();
		data.setCustomer(invoice.customer());
		data.setPerformances(invoice.performances().stream().map(this::enrichPerformance).toList());
		data.setTotalAmount(totalAmount(data));
		data.setTotalVolumeCredits(totalVolumeCredits(data));
		return renderPlainText();
	}

	private Performance enrichPerformance(Performance performance) {
		var result = performance.copy();
		result.setPlay(playFor(result));
		result.setAmount(amountFor(result));
		result.setVolumeCredits(volumeCreditsFor(result));
		return result;
	}

	private String renderPlainText() {
		var result = String.format("청구 내역 (고객명: %s)%n", data.getCustomer());
		for (var perf : data.getPerformances()) {
			// 청구 내역 출력
			result += String.format(" %s: %s (%s석)%n",
				perf.getPlay().name(),
				usd(perf.getAmount()),
				perf.getAudience()
			);
		}
		result += String.format("총액: %s%n", usd(totalAmount(data)));
		result += String.format("적립 포인트: %s점%n", totalVolumeCredits(data));
		return result;
	}

	private int volumeCreditsFor(Performance performance) {
		var result = 0;
		result += Math.max(performance.getAudience() - 30, 0);
		if ("comedy".equals(performance.getPlay().type())) {
			result += performance.getAudience() / 5;
		}
		return result;
	}

	private Play playFor(Performance performance) {
		return plays.get(performance.getPlayID());
	}

	private int amountFor(Performance performance) {
		var result = 0;
		switch (performance.getPlay().type()) {
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
				throw new IllegalArgumentException("알 수 없는 장르: " + performance.getPlay().type());
		}
		return result;
	}

	private String usd(int number) {
		return NumberFormat.getCurrencyInstance(Locale.US).format(number / 100);
	}

	private int totalAmount(StatementData data) {
		return data.getPerformances().stream()
			.mapToInt(Performance::getAmount)
			.sum();
	}

	private int totalVolumeCredits(StatementData data) {
		return data.getPerformances().stream()
			.mapToInt(Performance::getVolumeCredits)
			.sum();
	}

}
