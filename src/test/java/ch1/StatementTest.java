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

}
