package ch1;

import ch1.data.Invoice;
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
			Play play = plays.get(perf.playID());
			var thisAmount = 0;
			switch (play.type()) {
				case "tragedy":
					thisAmount = 40000;
					if (perf.audience() > 30) {
						thisAmount += 1000 * (perf.audience() - 30);
					}
					break;
				case "comedy":
					thisAmount = 30000;
					if (perf.audience() > 20) {
						thisAmount += 10000 + 500 * (perf.audience() - 20);
					}
					thisAmount += 300 * perf.audience();
					break;
				default:
					throw new IllegalArgumentException("알 수 없는 장르: " + play.type());
			}

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

}
