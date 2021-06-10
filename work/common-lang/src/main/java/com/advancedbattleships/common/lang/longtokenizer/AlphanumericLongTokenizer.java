package com.advancedbattleships.common.lang.longtokenizer;

import com.advancedbattleships.common.lang.LongTokenizer;

/**
 * Produces 10 character long hashes formed only from alphanumeric characters.
 * These hashes can then be stored in any database, regardless of collation.
 * This is useful for uniquely identifying objects across environments.
 */
public class AlphanumericLongTokenizer implements LongTokenizer {
	private static final int TOKEN_SIZE = 10;
	
	private static final char[] VALID_CHARS = {
		'`', '~', '1', '!',  '2', '@',  '3',  '#', '4', '$', '5', '%', '6', '^', '7', '&', '8', '*', '9', '(', '0', ')',
		'-', '_', '=', '+',  'q', 'Q',  'w',  'W', 'e', 'E', 'r', 'R', 't', 'T', 'y', 'Y', 'u', 'U', 'i', 'I', 'o', 'O',
		'p', 'P', '[', '{',  ']', '}',  'a',  'A', 's', 'S', 'd', 'D', 'f', 'F', 'g', 'G', 'h', 'H', 'j', 'J', 'k', 'K',
		'l', 'L', ';', ':', '\'', '"', '\\',  '|', 'z', 'Z', 'x', 'X', 'c', 'C', 'v', 'V', 'b', 'B', 'n', 'N', 'm', 'M',
		',', '<', '.', '>',  '/', '?',  ' '
	};

	private static final long VALID_CHARS_COUNT = VALID_CHARS.length;

	@Override
	public String tokenize(long source) {
		final char[] token = new char[TOKEN_SIZE];
		int currentDigit = 0;

		long remainder = 0L;

		while (source > 0) {
			remainder = source % VALID_CHARS_COUNT;
			source /= VALID_CHARS_COUNT;
			token[currentDigit] = VALID_CHARS[(int) remainder];
			currentDigit++;
		}

		while (currentDigit < TOKEN_SIZE) {
			token[currentDigit] = VALID_CHARS[0];
			currentDigit++;
		}

		return new String(token);
	}

}
