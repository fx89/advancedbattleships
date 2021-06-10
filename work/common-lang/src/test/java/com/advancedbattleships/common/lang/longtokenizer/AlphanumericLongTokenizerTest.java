package com.advancedbattleships.common.lang.longtokenizer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

import com.advancedbattleships.common.lang.LongTokenizer;

@SpringBootTest
public class AlphanumericLongTokenizerTest {

	@Test
	void contextLoads() {
		LongTokenizer tokenizer = new AlphanumericLongTokenizer();
		String token = tokenizer.tokenize(1L);
		assertThat(token).isEqualTo("~`````````");
	}

}
