package com.advancedbattleships.system.dataservice.impl.springdata.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "PARAMETERS")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Parameter implements Serializable {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "VALUE_CHAR")
	private String valueChar;

	@Column(name = "VALUE_NUMBER")
	private Long valueNumber;

	@Column(name = "VALUE_DECIMAL")
	private Double valueDecimal;
}
