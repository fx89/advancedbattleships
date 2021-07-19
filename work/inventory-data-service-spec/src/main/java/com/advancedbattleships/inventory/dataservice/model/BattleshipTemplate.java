package com.advancedbattleships.inventory.dataservice.model;

import java.io.Serializable;

public interface BattleshipTemplate extends Serializable {

	String getUniqueToken();

	void setUniqueToken(String uniqueToken);

	String getUserUniqueToken();

	void setUserUniqueToken(String userUniqueToken);

	boolean isOfficialTemplate();

	void setOfficialTemplate(boolean isOfficialTemplate);

	boolean isPublic();

	void setPublic(boolean isPublic);

	boolean isVisibleInLists();

	void setVisibleInLists(boolean isVisibleInLists);

	String getName();

	void setName(String name);

	Point2I getHullSize();

	void setHullSize(Point2I hullSize);

	void setHullSize(int width, int height);

	boolean[][] getHull();

	void setHull(boolean[][] hull);

	Double getCost();

	void setCost(Double cost);

	Double getEnergy();

	void setEnergy(Double energy);

	Double getFirepower();

	void setFirepower(Double firepower);
}
