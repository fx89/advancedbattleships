package com.advancedbattleships.inventory.dataservice.impl.springdata.model;

import static com.advancedbattleships.common.lang.Transcoder.booleanArrayToByteArray;
import static com.advancedbattleships.common.lang.Transcoder.byteArrayToBooleanArray;

import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplate;
import com.advancedbattleships.inventory.dataservice.model.Point2I;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name="BATTLESHIP_TEMPLATE")
@AllArgsConstructor
@NoArgsConstructor
public class BattleshipTemplateImpl implements BattleshipTemplate {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="UNIQUE_TOKEN")
	private String uniqueToken;

	@Column(name="USER_UNIQUE_TOKEN")
	private String userUniqueToken;
	
	@Column(name="IS_OFFICIAL_TEMPLATE")
	private boolean isOfficialTemplate;

	@Column(name="IS_PUBLIC")
	private boolean isPublic;

	@Column(name="IS_VISIBLE_IN_LISTS")
	private boolean isVisibleInLists;

	@Column(name="NAME")
	private String name;

	@Column(name="HULL_WIDTH")
	private int hullWidth;
	
	@Column(name="HULL_HEIGHT")
	private int hullHeight;

	@Column(name="HULL_ARRAY")
	private String hashedHullArray;

	@Column(name="COST")
	private Double cost;

	@Column(name="ENERGY")
	private Double energy;

	@Column(name="FIREPOWER")
	private Double firepower;

	@Override
	public String getUniqueToken() {
		return uniqueToken;
	}

	@Override
	public void setUniqueToken(String uniqueToken) {
		this.uniqueToken = uniqueToken;
	}

	@Override
	public String getUserUniqueToken() {
		return userUniqueToken;
	}

	@Override
	public void setUserUniqueToken(String userUniqueToken) {
		this.userUniqueToken = userUniqueToken;
	}

	@Override
	public boolean isOfficialTemplate() {
		return isOfficialTemplate;
	}

	@Override
	public void setOfficialTemplate(boolean isOfficialTemplate) {
		this.isOfficialTemplate = isOfficialTemplate;		
	}

	@Override
	public boolean isPublic() {
		return isPublic;
	}

	@Override
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	@Override
	public boolean isVisibleInLists() {
		return isVisibleInLists;
	}

	@Override
	public void setVisibleInLists(boolean isVisibleInLists) {
		this.isVisibleInLists = isVisibleInLists;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Point2I getHullSize() {
		return new Point2I(hullWidth, hullHeight);
	}

	@Override
	public void setHullSize(Point2I hullSize) {
		if (hullSize != null) {
			this.hullWidth = hullSize.x;
			this.hullHeight = hullSize.y;
		}
	}

	@Override
	public void setHullSize(int width, int height) {
		this.hullWidth = width;
		this.hullHeight = height;
	}

	@Transient
	private boolean[][] hullArray = null;

	public void initEmptyHullArray() {
		this.setHull(new boolean[hullHeight][hullWidth]);
	}

	@Override
	public boolean[][] getHull() {
		// If there's no hashed hull array, return NULL
		if (hashedHullArray == null) {
			return null;
		}

		// If the hull array has never been decoded, then it needs to be decoded
		if (hullArray == null) {
			// Decode the Base64 hash
			byte[] bytes = Base64.getDecoder().decode(hashedHullArray.getBytes());

			// Get the booleans array from the decoded bytes
			boolean[] bools = byteArrayToBooleanArray(bytes);

			// Un-flatten the booleans array
			hullArray = new boolean[hullHeight][hullWidth];
			int i = 0;
			for (int h = 0 ; h < hullHeight ; h++) {
				for (int w = 0 ; w < hullWidth ; w++) {
					hullArray[h][w] = bools[i];
					i++;
				}
			}
		}

		// In any case, return the array
		return hullArray;
	}

	@Override
	public void setHull(boolean[][] hull) {
		// Flatten the hull array
		boolean[] flatHullArray = new boolean[hullWidth * hullHeight];
		int i = 0;
		for (int h = 0 ; h < hullHeight ; h++) {
			for (int w = 0 ; w < hullWidth ; w++) {
				flatHullArray[i] = hull[h][w];
				i++;
			}
		}

		// Convert the flat hull array into a byte array
		byte[] hullBytes = booleanArrayToByteArray(flatHullArray);

		// Create a Base64 hash of the byte array
		hashedHullArray = new String(Base64.getEncoder().encode(hullBytes));

		// Expunge the previously cached hull array (if any)
		hullArray = null;
	}

	@Override
	public Double getCost() {
		return cost;
	}

	@Override
	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Override
	public Double getEnergy() {
		return energy;
	}

	@Override
	public void setEnergy(Double energy) {
		this.energy = energy;
	}

	@Override
	public Double getFirepower() {
		return firepower;
	}

	@Override
	public void setFirepower(Double firepower) {
		this.firepower = firepower;
	}

	public Long getId() {
		return id;
	}

	public BattleshipTemplateImpl(BattleshipTemplate src) {
		this.setHullSize(src.getHullSize());
		this.setHull(src.getHull());
		this.setName(src.getName());
		this.setOfficialTemplate(src.isOfficialTemplate());
		this.setPublic(src.isPublic());
		this.setUniqueToken(src.getUniqueToken());
		this.setUserUniqueToken(src.getUserUniqueToken());
		this.setVisibleInLists(src.isVisibleInLists());
		this.setCost(src.getCost());
		this.setEnergy(src.getEnergy());
		this.setFirepower(src.getFirepower());

		if (src instanceof BattleshipTemplateImpl) {
			this.id = ((BattleshipTemplateImpl) src).id;
		}
	}
}
