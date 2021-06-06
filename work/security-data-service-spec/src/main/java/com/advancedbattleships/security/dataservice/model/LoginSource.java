package com.advancedbattleships.security.dataservice.model;

public enum LoginSource {
	GOOGLE(1), GITHUB(2), FACEBOOK(3), YAHOO(5), MICROSOFT(4);

	private int value;

	private LoginSource(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		if (value == GOOGLE.getValue()) {
			return "GOOGLE";
		}

		if (value == GITHUB.getValue()) {
			return "GITHUB";
		}

		if (value == YAHOO.getValue()) {
			return "YAHOO";
		}

		if (value == FACEBOOK.getValue()) {
			return "FACEBOOK";
		}

		if (value == MICROSOFT.getValue()) {
			return "MICROSOFT";
		}

		return "UNKNOWN";
	}
}
