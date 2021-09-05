package com.advancedbattleships.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessagingProperties {
	private Integer pollingIntervalMS;
	private Integer maxNotificationsOnScreen;
}
