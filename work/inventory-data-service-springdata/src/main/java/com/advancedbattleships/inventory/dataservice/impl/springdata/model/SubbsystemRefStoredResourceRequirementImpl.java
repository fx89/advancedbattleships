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

import com.advancedbattleships.inventory.dataservice.model.StoredResourceType;
import com.advancedbattleships.inventory.dataservice.model.SubbsystemRefStoredResourceRequirement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name="SUBSYSTEM_REF_STORED_RESOURCE_REQ")
@AllArgsConstructor
@NoArgsConstructor
public class SubbsystemRefStoredResourceRequirementImpl implements SubbsystemRefStoredResourceRequirement {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORED_RESOURCE_TYPE_ID")
	private StoredResourceTypeImpl storedResourceType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBSYSTEM_REF_ID")
	private SubsystemRefImpl subsystemRef;

	@Column(name = "REQUIRED_AMT_PER_SHOT")
	private double requiredAmountPerShot;

	@Override
	public StoredResourceType getStoredResource() {
		return storedResourceType;
	}

	@Override
	public void setStoredResource(StoredResourceType storedResource) {
		this.storedResourceType = new StoredResourceTypeImpl(storedResource);
	}

	@Override
	public double getRequiredAmountPerShot() {
		return requiredAmountPerShot;
	}

	@Override
	public void setRequiredAmountPerShot(double requiredAmountPerShot) {
		this.requiredAmountPerShot = requiredAmountPerShot;
	}

	public SubbsystemRefStoredResourceRequirementImpl(SubbsystemRefStoredResourceRequirement src) {
		this.setRequiredAmountPerShot(src.getRequiredAmountPerShot());
		this.setStoredResource(src.getStoredResource());

		if (src instanceof SubbsystemRefStoredResourceRequirementImpl) {
			this.id = ((SubbsystemRefStoredResourceRequirementImpl) src).id;
			this.subsystemRef = ((SubbsystemRefStoredResourceRequirementImpl) src).subsystemRef;
		}
	}
}
