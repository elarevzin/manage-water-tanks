package com.elar.watertanks.services;

import java.math.BigDecimal;
import java.util.List;

import com.elar.watertanks.model.WaterTank;

public interface WaterTanksService {
	
	public BigDecimal queryMaxCapacity(Integer tankId) ;

	public BigDecimal queryCurrentCapacity(Integer tankId) ;
	
	public boolean addWater(Integer tankId, BigDecimal volumeInLiters);
	
	public void populateInitialData(List<WaterTank> waterTanks);
	
	public void validateTankId(Integer tankId) ;


}
