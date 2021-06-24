package com.advancedbattleships.security.service.utility;

import java.time.LocalDateTime;

public class NicknameGenerator {

	private static final String[] SECONDS_NICKS = {
		"Alpha-",
		"Bravo-",
		"Delta-",
		"Echo-",
		"Foxtrot-",
		"Kilo-",
		"Lima-",
		"November-",
		"Oscar-",
		"Sierra-",
		"Zulu-",
	};

	private static final String [] MINUTES_NICKS = {
		"Quebec-",
		"Hotel-",
		"Tango-",
		"India-",
		"Whiskey-",
		"Xray-",
		"Golf-",
	};

	private static final String [] HOURS_NICKS = {
		"Mike-",
		"Victor-",
		"Yankee-",
		"Uniform-",
		"Juliet-",
		"Romeo-",
		"Papa-",
		"Charlie-",
	};
	

	public static synchronized String generateNickName() {
		LocalDateTime now = LocalDateTime.now();

		return getItem(now.getSecond(), SECONDS_NICKS)
			 + getItem(now.getMinute(), MINUTES_NICKS)
			 + getItem(now.getHour()  , HOURS_NICKS)
			 + now.getDayOfYear() + (int)Math.floor(Math.random() * 999) + now.getYear() + (int)Math.floor(Math.random() * 999) + now.getSecond() + (int)Math.floor(Math.random() * 999) + now.getHour()
		;
	}

	private static String getItem(int itemIndex, String[] itemsList) {
		if (itemIndex == 0) {
			return itemsList[0];
		}

		return itemsList[itemIndex % itemsList.length];
	}
}
