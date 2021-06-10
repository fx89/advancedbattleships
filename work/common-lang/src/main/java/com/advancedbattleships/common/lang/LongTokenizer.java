package com.advancedbattleships.common.lang;

public interface LongTokenizer {
	/**
	 * Returns a hash which uniquely identifies the given LONG. Different
	 * implementations may employ different algorithms for different purposes.
	 * For instance,
	 * {@link com.advancedbattleships.common.lang.longtokenizer.AlphanumericLongTokenizer
	 * AlphanumericLongTokenizer} makes tokens which are shorter than UUIDs, but still
	 * capable of holding timestamps.
	 */
	public String tokenize(long source);
}
