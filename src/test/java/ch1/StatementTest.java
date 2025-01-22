package ch1;

import ch1.data.Invoice;
import ch1.data.Play;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class StatementTest {

	static Map<String, Play> plays;
	static List<Invoice> invoices;

	@BeforeAll
	static void beforeAll() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		File playData = new File("src/test/resources/plays.json");
		plays = objectMapper.readValue(playData, new TypeReference<>() {});
		File invoiceData = new File("src/test/resources/invoices.json");
		invoices = objectMapper.readValue(invoiceData, new TypeReference<>() {});
	}

	@Test
	void statement() {
		Statement statement = new Statement(plays, invoices.get(0));
		String result = statement.statement();
		Assertions.assertThat(result).isEqualTo("""
			청구 내역 (고객명: BigCo)
			 Hamlet: $650.00 (55석)
			 As You Like It: $580.00 (35석)
			 Othello: $500.00 (40석)
			총액: $1,730.00
			적립 포인트: 47점
			""");
	}

	@Test
	void htmlStatement() {
		Statement statement = new Statement(plays, invoices.get(0));
		String result = statement.htmlStatement();
		Assertions.assertThat(result).isEqualTo("""
			<h1>청구 내역 (고객명: BigCo)</h1>
			<table>
			<tr><th>연극</th><th>좌석 수</th><th>금액</th></tr>  <tr><td>Hamlet</td><td>(55석)</td><td>$650.00</td></tr>
			  <tr><td>As You Like It</td><td>(35석)</td><td>$580.00</td></tr>
			  <tr><td>Othello</td><td>(40석)</td><td>$500.00</td></tr>
			</table>
			<p>총액: <em>$1,730.00</em></p>
			<p>적립 포인트: <em>47</em>점</p>
			""");
	}

}
