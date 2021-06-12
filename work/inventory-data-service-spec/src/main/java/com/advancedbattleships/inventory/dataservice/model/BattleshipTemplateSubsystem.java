package com.advancedbattleships.inventory.dataservice.model;

import java.io.Serializable;

public interface BattleshipTemplateSubsystem extends Serializable {

	String getUniqueToken();

	void setUniqueToken(String uniqueToken);

	BattleshipTemplate getBattleshipTemplate();

	void setBattleshipTemplate(BattleshipTemplate battleshipTemplate);

	SubsystemRef getSubsystemRef();

	void setSubsystemRef(SubsystemRef subsystemRef);

	Point2I getPosition();

	void setPosition(Point2I position);
}
