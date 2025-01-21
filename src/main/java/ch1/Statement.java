package ch1;

import ch1.data.Invoice;
import ch1.data.Performance;
import ch1.data.Play;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Statement {

	public String statement(Invoice invoice, Map<String, Play> plays) {
		var totalAmount = 0;
		var volumeCredits = 0;
		var result = String.format("청구 내역 (고객명: %s)%n", invoice.customer());
		var format = NumberFormat.getCurrencyInstance(Locale.US);
		for (var perf : invoice.performances()) {
			var play = plays.get(perf.playID());
			var thisAmount = amountFor(perf, play);

			// 포인트 적립
			volumeCredits += Math.max(perf.audience() - 30, 0);

			// 희극 관객 5명마다 추가 포인트 제공
			if ("comedy".equals(play.type())) {
				volumeCredits += perf.audience() / 5;
			}

			// 청구 내역 출력
			result += String.format(" %s: %s (%s석)%n",
				play.name(),
				format.format(thisAmount / 100),
				perf.audience()
			);
			totalAmount += thisAmount;
		}
		result += String.format("총액: %s%n", format.format(totalAmount / 100));
		result += String.format("적립 포인트: %s점%n", volumeCredits);
		return result;
	}

	private int amountFor(Performance perf, Play play) {
		var result = 0;
		switch (play.type()) {
			case "tragedy":
				result = 40000;
				if (perf.audience() > 30) {
					result += 1000 * (perf.audience() - 30);
				}
				break;
			case "comedy":
				result = 30000;
				if (perf.audience() > 20) {
					result += 10000 + 500 * (perf.audience() - 20);
				}
				result += 300 * perf.audience();
				break;
			default:
				throw new IllegalArgumentException("알 수 없는 장르: " + play.type());
		}
		return result;
	}

}
