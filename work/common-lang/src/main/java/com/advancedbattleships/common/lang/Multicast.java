package com.advancedbattleships.common.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
		return (Set<T>) multicastCollection(srcSet, (s) -> new HashSet<T>(s), transform);
	}

	/**
	 * Creates a new list in which every element of the source list is cast to the
	 * target data type.<br />
	 * <br />
	 * This is enables casting the entire source list in a single line of code.<br />
	 * <br />
	 * Make sure the two data types are compatible before calling this method.
	 */
	@SuppressWarnings("unchecked")
	public static <S, T> List<T> multicastList(List<S> srcList) {
		return multicastList(srcList, (s) -> (T)s);
	}

	/**
	 * Creates a new list in which every element of the source list is cast to the
	 * target data type using the provided casting function.<br />
	 * <br />
	 * This is enables casting the entire source list in a single line of code.<br />
	 * <br />
	 * If the provided casting function reference is null, a NullPointerException
	 * occurs.
	 */
	public static <S, T> List<T> multicastList(List<S> srcList, Function<S, T> transform) {
		return (List<T>) multicastCollection(srcList, (s) -> new ArrayList<T>(s), transform);
	}

	/**
	 * Creates a new collection in which every element of the source list is cast to the
	 * target data type using the provided casting function.<br />
	 * <br />
	 * This is enables casting the entire source collection in a single line of code.<br />
	 * <br />
	 * If the provided casting function reference is null, a NullPointerException will
	 * occur.<br />
	 * <br />
	 * The provided initialization function takes in the size of the old collection and
	 * returns a new collection of the proper type and of the same size. If the provided
	 * initialization function reference is null, a NullPointerException will occur.
	 */
	public static <S, T> Collection<T> multicastCollection(Collection<S> srcCollection, Function<Integer, Collection<T>> createFunc, Function<S, T> transform) {
		if (srcCollection == null) {
			return null;
		}

		Collection<T> ret = createFunc.apply(srcCollection.size());		

		srcCollection.forEach(s -> {
			ret.add(transform.apply(s));
		});
		
		return ret;
	}
}
