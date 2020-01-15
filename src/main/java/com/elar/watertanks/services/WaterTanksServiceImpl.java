package com.elar.watertanks.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.elar.watertanks.api.WaterTanksController.TankNotFoundException;
import com.elar.watertanks.model.WaterTank;

@Service
public class WaterTanksServiceImpl implements WaterTanksService{
	
	private List<WaterTank> waterTanks;	

	public List<WaterTank> getWaterTanks() {
		return waterTanks;
	}

	public WaterTanksServiceImpl(List<WaterTank> waterTanks) {
		super();
		this.waterTanks = waterTanks;
	}

	@Override
	public BigDecimal queryMaxCapacity(Integer tankId) {
		WaterTank waterTank = waterTanks.get(tankId);
		return waterTank.getMaxCapacity();
	}

	@Override
	public BigDecimal queryCurrentCapacity(Integer tankId) {
		WaterTank waterTank = waterTanks.get(tankId);
		return waterTank.getCurrentCapacity();
	}

	@Override
	public boolean addWater(Integer tankId, BigDecimal volumeInLiters) {
		if(volumeInLiters.compareTo(BigDecimal.ZERO) < 0) {
			return false;
		}
		WaterTank waterTank = waterTanks.get(tankId);
		
		BigDecimal maxCapacity = waterTank.getMaxCapacity();
		synchronized (waterTank.getCurrentCapacity()) {
			BigDecimal currentCapacity = waterTank.getCurrentCapacity();

			if(currentCapacity != null && maxCapacity != null &&
					(currentCapacity.add(volumeInLiters)).compareTo(maxCapacity) > 0) {
				
				return false;
			}
			else {
				waterTank.setCurrentCapacity(currentCapacity.add(volumeInLiters));
				return true;
			}
		}
		
	}

	public void populateInitialData(List<WaterTank> waterTanks) {
		this.waterTanks = waterTanks;
	}


	public void validateTankId(Integer tankId) {
		if(tankId < 0 || tankId > getWaterTanks().size()) {
			throw new TankNotFoundException("Tank not found. Tank id: " + tankId);
		}
	}
	
	@SuppressWarnings("serial")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public class TankNotFoundException extends RuntimeException {
		public TankNotFoundException(String exception) {
			super(exception);
		}
	}
}
