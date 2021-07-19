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
	 * Creates a new collection in which every element of the source collection is cast to the
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
	public static <S, T> Collection<T> multicastCollection(Collection<S> srcCollection, Function<Integer, Iterable<T>> createFunc, Function<S, T> transform) {
		return (Collection<T>) multicastIterable(
					srcCollection,
					(coll) -> ((Collection<S>)coll).size(),
					createFunc,
					transform
				);
	}

	/**
	 * Returns a new iterable containing all the elements of the provided srcIterable
	 * cast as the new data type. This allows casting iterables in a single line of code.
	 */
	@SuppressWarnings("unchecked")
	public static<S, T> Iterable<T> multicastIterable(Iterable<S> srcIterable) {
		return multicastIterable(
					srcIterable,
					(src) -> 1000,
					(size) -> new ArrayList<>(),
					(s) -> (T)s
				);
	}

	/**
	 * Creates a new iterable in which every element of the source iterable is cast to the
	 * target data type using the provided casting function.<br />
	 * <br />
	 * This is enables casting the entire source iterable in a single line of code.<br />
	 * <br />
	 * If the provided casting function reference is null, a NullPointerException will
	 * occur.<br />
	 * <br />
	 * The provided initialization function takes in the size calculated by the provided
	 * getSizeFunc() and returns a new iterable of the proper type and of the same size.
	 * If the provided initialization function reference is null, a NullPointerException
	 * will occur.
	 */
	public static <S, T> Iterable<T> multicastIterable(
		Iterable<S> srcIterable,
		Function<Iterable<S>, Integer> getSizeFunc,
		Function<Integer, Iterable<T>> createFunc,
		Function<S, T> transform
	) {
		if (srcIterable == null) {
			return null;
		}

		Collection<T> ret = (Collection<T>) createFunc.apply(getSizeFunc.apply(srcIterable));

		srcIterable.forEach(s -> {
			ret.add(transform.apply(s));
		});
		
		return ret;
	}
}
