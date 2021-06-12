package com.advancedbattleships.inventory.dataservice.impl.springdata.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.advancedbattleships.inventory.dataservice.model.GeneratedResourceType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "GENERATED_RESOURCE_TYPE")
@AllArgsConstructor
@NoArgsConstructor
public class GeneratedResourceTypeImpl implements GeneratedResourceType {

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

	public GeneratedResourceTypeImpl(GeneratedResourceType src) {
		this.name = src.getName();

		if (src instanceof GeneratedResourceTypeImpl) {
			this.id = ((GeneratedResourceTypeImpl) src).id;
		}
	}
}
