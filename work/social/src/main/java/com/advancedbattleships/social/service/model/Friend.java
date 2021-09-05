package com.advancedbattleships.social.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Friend {

	private String friendUserUniqueToken;

	private String status;

	private Boolean isOnline;

	private String nickName;

	private String logoName;

}
