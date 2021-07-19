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
import com.advancedbattleships.inventory.dataservice.model.SubsystemRefGeneratedResourceRequirement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name="SUBSYSTEM_REF_GENERATED_RESOURCE_REQ")
@AllArgsConstructor
@NoArgsConstructor
public class SubsystemRefGeneratedResourceRequirementImpl implements SubsystemRefGeneratedResourceRequirement {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBSYSTEM_REF_ID")
	@Fetch(FetchMode.JOIN)
	private SubsystemRefImpl subsystemRef;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GENERATED_RESOURCE_TYPE_ID")
	@Fetch(FetchMode.JOIN)
	private GeneratedResourceTypeImpl generatedResourceType;

	@Column(name = "REQUIRED_AMT")
	private double requiredAmount;
	
	@Override
	public GeneratedResourceType getResourceType() {
		return generatedResourceType;
	}

	@Override
	public void setResourceType(GeneratedResourceType resourceType) {
		this.generatedResourceType = new GeneratedResourceTypeImpl(resourceType);
	}

	@Override
	public double getRequiredAmount() {
		return requiredAmount;
	}

	@Override
	public void setRequiredAmount(double requiredAmount) {
		this.requiredAmount = requiredAmount;
	}

	public SubsystemRefGeneratedResourceRequirementImpl( SubsystemRefGeneratedResourceRequirement src) {
		this.setRequiredAmount(src.getRequiredAmount());
		this.setResourceType(src.getResourceType());

		if (src instanceof SubsystemRefGeneratedResourceRequirementImpl) {
			this.id = ((SubsystemRefGeneratedResourceRequirementImpl) src).id;
			this.subsystemRef = ((SubsystemRefGeneratedResourceRequirementImpl) src).subsystemRef;
		}
	}
}
