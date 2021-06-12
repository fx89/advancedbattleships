package com.advancedbattleships.inventory.dataservice.impl.springdata.model;

import static com.advancedbattleships.common.lang.Multicast.multicastSet;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.advancedbattleships.inventory.dataservice.model.SubbsystemRefGeneratedResourceRequirement;
import com.advancedbattleships.inventory.dataservice.model.SubbsystemRefStoredResourceRequirement;
import com.advancedbattleships.inventory.dataservice.model.SubsystemRef;
import com.advancedbattleships.inventory.dataservice.model.SubsystemRefGeneratedResourceSpec;
import com.advancedbattleships.inventory.dataservice.model.SubsystemRefStorage;
import com.advancedbattleships.inventory.dataservice.model.SubsystemType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name="SUBSYSTEM_REF")
@AllArgsConstructor
@NoArgsConstructor
public class SubsystemRefImpl implements SubsystemRef {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME")
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TYPE_ID")
	private SubsystemTypeImpl type;

	@Column(name = "COST")
	private double cost;

	@OneToMany(mappedBy="subsystemRef")
	private Set<SubbsystemRefStoredResourceRequirementImpl> storedResourceRequirements;

	@OneToMany(mappedBy="subsystemRef")
	private Set<SubsystemRefStorageImpl> storage;

	@OneToMany(mappedBy="subsystemRef")
	private Set<SubbsystemRefGeneratedResourceRequirementImpl> generatedResourceRequirmenets;

	@OneToMany(mappedBy="subsystemRef")
	private Set<SubsystemRefGeneratedResourceSpecImpl> generatedResources;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public SubsystemType getType() {
		return type;
	}

	@Override
	public void setType(SubsystemType type) {
		this.type = new SubsystemTypeImpl(type);
	}

	@Override
	public double getCost() {
		return cost;
	}

	@Override
	public void setCost(double cost) {
		this.cost = cost;
	}

	@Override
	public Set<SubbsystemRefStoredResourceRequirement> getStoredResourceRequirements() {
		return multicastSet(storedResourceRequirements);
	}

	@Override
	public void setStoredResourceRequirements(Set<SubbsystemRefStoredResourceRequirement> storedResourceRequirements) {
		this.storedResourceRequirements
			= multicastSet(storedResourceRequirements, (src) -> new SubbsystemRefStoredResourceRequirementImpl(src));
	}

	@Override
	public Set<SubsystemRefStorage> getStorage() {
		return multicastSet(storage);
	}

	@Override
	public void setStorage(Set<SubsystemRefStorage> storage) {
		this.storage = multicastSet(storage, (src) -> new SubsystemRefStorageImpl(src));
	}

	@Override
	public Set<SubbsystemRefGeneratedResourceRequirement> getGeneratedResourceRequirements() {
		return multicastSet(generatedResourceRequirmenets);
	}

	@Override
	public void setGeneratedResourceRequirements(
			Set<SubbsystemRefGeneratedResourceRequirement> generatedResourceRequirements) {
		this.generatedResourceRequirmenets
			= multicastSet(generatedResourceRequirmenets, (src) -> new SubbsystemRefGeneratedResourceRequirementImpl(src));
	}

	@Override
	public Set<SubsystemRefGeneratedResourceSpec> getGeneratedResources() {
		return multicastSet(generatedResources);
	}

	@Override
	public void setGeneratedResources(Set<SubsystemRefGeneratedResourceSpec> generatedResources) {
		this.generatedResources
			= multicastSet(generatedResources, (src) -> new SubsystemRefGeneratedResourceSpecImpl(src));
	}

	public SubsystemRefImpl(SubsystemRef src) {
		this.setCost(src.getCost());
		this.setGeneratedResourceRequirements(src.getGeneratedResourceRequirements());
		this.setGeneratedResources(src.getGeneratedResources());
		this.setName(src.getName());
		this.setStorage(src.getStorage());
		this.setStoredResourceRequirements(src.getStoredResourceRequirements());
		this.setType(src.getType());

		if (src instanceof SubsystemRefImpl) {
			this.id = ((SubsystemRefImpl) src).id;
		}
	}
}
