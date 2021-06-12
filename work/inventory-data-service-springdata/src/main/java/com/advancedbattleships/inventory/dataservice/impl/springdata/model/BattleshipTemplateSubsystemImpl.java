package com.advancedbattleships.inventory.dataservice.impl.springdata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplate;
import com.advancedbattleships.inventory.dataservice.model.BattleshipTemplateSubsystem;
import com.advancedbattleships.inventory.dataservice.model.Point2I;
import com.advancedbattleships.inventory.dataservice.model.SubsystemRef;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name="BATTLESHIP_TEMPLATE_SUBSYSTEM")
@AllArgsConstructor
@NoArgsConstructor
public class BattleshipTemplateSubsystemImpl implements BattleshipTemplateSubsystem {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "UNIQUE_TOKEN")
	String uniqueToken;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BATTLESHIP_TEMPLATE_ID")
	BattleshipTemplateImpl battleshipTemplate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SUBSYSTEM_REF_ID")
	SubsystemRefImpl subsystemRef;

	@Column(name = "POS_X")
	int posX;

	@Column(name = "POS_Y")
	int posY;

	@Override
	public String getUniqueToken() {
		return uniqueToken;
	}

	@Override
	public void setUniqueToken(String uniqueToken) {
		this.uniqueToken = uniqueToken;
	}

	@Override
	public BattleshipTemplate getBattleshipTemplate() {
		return battleshipTemplate;
	}

	@Override
	public void setBattleshipTemplate(BattleshipTemplate battleshipTemplate) {
		this.battleshipTemplate = new BattleshipTemplateImpl(battleshipTemplate);
	}

	@Override
	public SubsystemRef getSubsystemRef() {
		return subsystemRef;
	}

	@Override
	public void setSubsystemRef(SubsystemRef subsystemRef) {
		this.subsystemRef = new SubsystemRefImpl(subsystemRef);
	}

	@Override
	public Point2I getPosition() {
		return new Point2I(posX, posY);
	}

	@Override
	public void setPosition(Point2I position) {
		this.posX = position.x;
		this.posY = position.y;
	}

	public BattleshipTemplateSubsystemImpl(BattleshipTemplateSubsystem src) {
		this.setBattleshipTemplate(src.getBattleshipTemplate());
		this.setPosition(src.getPosition());
		this.setSubsystemRef(src.getSubsystemRef());
		this.setUniqueToken(src.getUniqueToken());

		if (src instanceof BattleshipTemplateSubsystemImpl) {
			this.id = ((BattleshipTemplateSubsystemImpl) src).id;
		}
	}
}
