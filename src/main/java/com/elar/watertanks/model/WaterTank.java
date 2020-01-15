package com.elar.watertanks.model;

import java.math.BigDecimal;

public class WaterTank {
	private Integer id;
	private BigDecimal maxCapacity;
	private BigDecimal currentCapacity;
	
	public BigDecimal getMaxCapacity() {
		return maxCapacity;
	}
	public void setMaxCapacity(BigDecimal maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
	public BigDecimal getCurrentCapacity() {
		return currentCapacity;
	}
	public void setCurrentCapacity(BigDecimal currentCapacity) {
		this.currentCapacity = currentCapacity;
	}
	public int getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public WaterTank(Integer id, BigDecimal maxCapacity, BigDecimal currentCapacity) {
		super();
		this.id = id;
		this.maxCapacity = maxCapacity;
		this.currentCapacity = currentCapacity;
	}
	
	
}
