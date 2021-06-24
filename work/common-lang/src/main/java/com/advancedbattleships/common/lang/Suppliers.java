package com.advancedbattleships.common.lang;

import java.util.function.Supplier;

public class Suppliers {

	public static <T> T nullSafeSupplier(Supplier<T> supplier, T valueOnNullPointerException) {
		try {
			return supplier.get();
		} catch (NullPointerException exc) {
			return valueOnNullPointerException;
		}
	}
}
