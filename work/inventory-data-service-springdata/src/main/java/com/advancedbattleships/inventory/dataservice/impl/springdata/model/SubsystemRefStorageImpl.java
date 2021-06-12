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
import com.advancedbattleships.inventory.dataservice.model.SubsystemRefStorage;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name="SUBSYSTEM_REF_STORAGE")
@AllArgsConstructor
@NoArgsConstructor
public class SubsystemRefStorageImpl implements SubsystemRefStorage {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SUBSYSTEM_REF_ID")
	private SubsystemRefImpl subsystemRef;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STORED_RESOURCE_TYPE_ID")
	private StoredResourceTypeImpl storedResourceType;

	@Column(name = "INITIAL_AMT")
	private double initialAmount;

	@Override
	public StoredResourceType getStoredResource() {
		return storedResourceType;
	}

	@Override
	public void setStoredResource(StoredResourceType storedResource) {
		this.storedResourceType = new StoredResourceTypeImpl(storedResource);
	}

	@Override
	public double getInitialAmount() {
		return initialAmount;
	}

	@Override
	public void setInitialAmount(double initialAmount) {
		this.initialAmount = initialAmount;
	}

	public SubsystemRefStorageImpl(SubsystemRefStorage src) {
		this.setInitialAmount(src.getInitialAmount());
		this.setStoredResource(src.getStoredResource());

		if (src instanceof SubsystemRefStorageImpl) {
			this.id = ((SubsystemRefStorageImpl) src).id;
			this.subsystemRef = ((SubsystemRefStorageImpl) src).subsystemRef;
		}
	}
}
