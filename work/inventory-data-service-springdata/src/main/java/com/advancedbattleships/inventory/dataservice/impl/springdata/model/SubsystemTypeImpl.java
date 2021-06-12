package com.advancedbattleships.inventory.dataservice.impl.springdata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.advancedbattleships.inventory.dataservice.model.SubsystemType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "SUBSYSTEM_TYPE")
@AllArgsConstructor
@NoArgsConstructor
public class SubsystemTypeImpl implements SubsystemType {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public SubsystemTypeImpl(SubsystemType src) {
		this.setName(src.getName());

		if (src instanceof SubsystemTypeImpl) {
			this.id = ((SubsystemTypeImpl) src).id;
		}
	}
}
