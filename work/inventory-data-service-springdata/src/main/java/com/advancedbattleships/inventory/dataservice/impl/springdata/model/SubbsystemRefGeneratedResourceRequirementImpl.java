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

import com.advancedbattleships.inventory.dataservice.model.GeneratedResourceType;
import com.advancedbattleships.inventory.dataservice.model.SubbsystemRefGeneratedResourceRequirement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name="SUBSYSTEM_REF_GENERATED_RESOURCE_REQ")
@AllArgsConstructor
@NoArgsConstructor
public class SubbsystemRefGeneratedResourceRequirementImpl implements SubbsystemRefGeneratedResourceRequirement {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBSYSTEM_REF_ID")
	private SubsystemRefImpl subsystemRef;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GENERATED_RESOURCE_TYPE_ID")
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

	public SubbsystemRefGeneratedResourceRequirementImpl( SubbsystemRefGeneratedResourceRequirement src) {
		this.setRequiredAmount(src.getRequiredAmount());
		this.setResourceType(src.getResourceType());

		if (src instanceof SubbsystemRefGeneratedResourceRequirementImpl) {
			this.id = ((SubbsystemRefGeneratedResourceRequirementImpl) src).id;
			this.subsystemRef = ((SubbsystemRefGeneratedResourceRequirementImpl) src).subsystemRef;
		}
	}
}
