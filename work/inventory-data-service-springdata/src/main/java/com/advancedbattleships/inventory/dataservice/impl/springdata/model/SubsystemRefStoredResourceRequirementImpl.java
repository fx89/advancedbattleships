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

import com.advancedbattleships.inventory.dataservice.model.StoredResourceType;
import com.advancedbattleships.inventory.dataservice.model.SubsystemRefStoredResourceRequirement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name="SUBSYSTEM_REF_STORED_RESOURCE_REQ")
@AllArgsConstructor
@NoArgsConstructor
public class SubsystemRefStoredResourceRequirementImpl implements SubsystemRefStoredResourceRequirement {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORED_RESOURCE_TYPE_ID")
	@Fetch(FetchMode.JOIN)
	private StoredResourceTypeImpl storedResourceType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBSYSTEM_REF_ID")
	@Fetch(FetchMode.JOIN)
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

	public SubsystemRefStoredResourceRequirementImpl(SubsystemRefStoredResourceRequirement src) {
		this.setRequiredAmountPerShot(src.getRequiredAmountPerShot());
		this.setStoredResource(src.getStoredResource());

		if (src instanceof SubsystemRefStoredResourceRequirementImpl) {
			this.id = ((SubsystemRefStoredResourceRequirementImpl) src).id;
			this.subsystemRef = ((SubsystemRefStoredResourceRequirementImpl) src).subsystemRef;
		}
	}
}
