package com.advancedbattleships.common.lang;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class Multicast {

	/**
	 * Creates a new set in which every element of the source set is cast to the
	 * target data type.<br />
	 * <br />
	 * This is enables casting the entire source set in a single line of code.<br />
	 * <br />
	 * Make sure the two data types are compatible before calling this method.
	 */
	@SuppressWarnings("unchecked")
	public static <S, T> Set<T> multicastSet(Set<S> srcSet) {
		return multicastSet(srcSet, (s) -> (T) s);
	}

	/**
	 * Creates a new set in which every element of the source set is cast to the
	 * target data type using the provided casting function.<br />
	 * <br />
	 * This is enables casting the entire source set in a single line of code.<br />
	 * <br />
	 * If the provided casting function reference is null, a NullPointerException
	 * occurs.
	 */
	public static <S, T> Set<T> multicastSet(Set<S> srcSet, Function<S, T> transform) {
		if (srcSet == null) {
			return null;
		}

		Set<T> ret = new HashSet<>();

		srcSet.forEach(s -> {
			ret.add(transform.apply(s));
		});

		return ret;
	}
}
