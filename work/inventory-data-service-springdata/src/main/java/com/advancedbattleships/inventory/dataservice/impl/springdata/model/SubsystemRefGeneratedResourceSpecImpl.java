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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.advancedbattleships.inventory.dataservice.model.GeneratedResourceType;
import com.advancedbattleships.inventory.dataservice.model.SubsystemRefGeneratedResourceSpec;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "SUBSYSTEM_REF_GENERATED_RESOURCE_SPEC")
@AllArgsConstructor
@NoArgsConstructor
public class SubsystemRefGeneratedResourceSpecImpl implements SubsystemRefGeneratedResourceSpec {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SUBSYSTEM_REF_ID")
	@Fetch(FetchMode.JOIN)
	private SubsystemRefImpl subsystemRef;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GENERATED_RESOURCE_TYPE_ID")
	@Fetch(FetchMode.JOIN)
	private GeneratedResourceTypeImpl resourceType;

	@Column(name = "AMOUNT")
	private double amount;

	@Override
	public GeneratedResourceType getResourceType() {
		return resourceType;
	}

	@Override
	public void setResourceType(GeneratedResourceType resourceType) {
		this.resourceType = new GeneratedResourceTypeImpl(resourceType);
	}

	@Override
	public double getAmount() {
		return amount;
	}

	@Override
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public SubsystemRefGeneratedResourceSpecImpl(SubsystemRefGeneratedResourceSpec src) {
		this.setAmount(src.getAmount());
		this.setResourceType(new GeneratedResourceTypeImpl(src.getResourceType()));

		if (src instanceof SubsystemRefGeneratedResourceSpecImpl) {
			this.id = ((SubsystemRefGeneratedResourceSpecImpl) src).id;
			this.subsystemRef = ((SubsystemRefGeneratedResourceSpecImpl) src).subsystemRef;
		}
	}
}
