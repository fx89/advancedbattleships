package com.advancedbattleships.common.lang.longtokenizer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.advancedbattleships.common.lang.LongTokenizer;

public class AlphanumericLongTokenizerTest {

	@Test
	void contextLoads() {
		LongTokenizer tokenizer = new AlphanumericLongTokenizer();
		String token = tokenizer.tokenize(1L);
		assertThat(token).isEqualTo("~`````````");
	}

}
