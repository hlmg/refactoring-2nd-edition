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

    public Statement(Map<String, Play> plays, Invoice invoice) {
        this.plays = plays;
        this.invoice = invoice;
    }

    public String statement() {
        StatementRenderer statementRenderer = new StatementRenderer(createStatementData());
        return statementRenderer.renderPlainText();
    }

    public String htmlStatement() {
        StatementRenderer statementRenderer = new StatementRenderer(createStatementData());
        return statementRenderer.renderHtml();
    }

    private StatementData createStatementData() {
        StatementData data = new StatementData();
        data.setCustomer(invoice.customer());
        data.setPerformances(invoice.performances().stream().map(this::enrichPerformance).toList());
        data.setTotalAmount(totalAmount(data));
        data.setTotalVolumeCredits(totalVolumeCredits(data));
        return data;
    }

    private Performance enrichPerformance(Performance performance) {
        var calculator = createPerformanceCalculator(performance, playFor(performance));
        var result = performance.copy();
        result.setPlay(calculator.getPlay());
        result.setAmount(calculator.amount());
        result.setVolumeCredits(calculator.volumeCredits());
        return result;
    }

    private PerformanceCalculator createPerformanceCalculator(Performance performance, Play play) {
        return switch (play.type()) {
            case "tragedy" -> new TragedyCalculator(performance, play);
            case "comedy" -> new ComedyCalculator(performance, play);
            default -> throw new IllegalArgumentException("알 수 없는 장르: " + play.type());
        };
    }

    private Play playFor(Performance performance) {
        return plays.get(performance.getPlayID());
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

    private static class StatementRenderer {

        StatementData statementData;

        public StatementRenderer(StatementData statementData) {
            this.statementData = statementData;
        }

        public String renderPlainText() {
            var result = String.format("청구 내역 (고객명: %s)%n", statementData.getCustomer());
            for (var perf : statementData.getPerformances()) {
                // 청구 내역 출력
                result += String.format(" %s: %s (%s석)%n",
                    perf.getPlay().name(),
                    usd(perf.getAmount()),
                    perf.getAudience()
                );
            }
            result += String.format("총액: %s%n", usd(statementData.getTotalAmount()));
            result += String.format("적립 포인트: %s점%n", statementData.getTotalVolumeCredits());
            return result;
        }

        public String renderHtml() {
            var result = String.format("<h1>청구 내역 (고객명: %s)</h1>%n", statementData.getCustomer());
            result += String.format("<table>%n");
            result += String.format("<tr><th>연극</th><th>좌석 수</th><th>금액</th></tr>");
            for (Performance performance : statementData.getPerformances()) {
                result += String.format("  <tr><td>%s</td><td>(%s석)</td>", performance.getPlay().name(),
                    performance.getAudience());
                result += String.format("<td>%s</td></tr>%n", usd(performance.getAmount()));
            }
            result += String.format("</table>%n");
            result += String.format("<p>총액: <em>%s</em></p>%n", usd(statementData.getTotalAmount()));
            result += String.format("<p>적립 포인트: <em>%s</em>점</p>%n", statementData.getTotalVolumeCredits());
            return result;
        }

        private String usd(int number) {
            return NumberFormat.getCurrencyInstance(Locale.US).format(number / 100);
        }

    }

}
