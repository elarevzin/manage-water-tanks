package com.elar.watertanks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.elar.watertanks.model.WaterTank;
import com.elar.watertanks.services.WaterTanksService;

@Service
public class InitializeWaterTanksRunner implements CommandLineRunner{
	
	@Value("${numberOfTanks}")
	private int numberOfTanks;
	
	@Value("${maxTankCapacity}")
	private BigDecimal maxTankCapacity;
	
	@Value("${knownLeakPerMinute}")
	private BigDecimal knownLeakPerMinute;
	
	private static final BigDecimal SECONDS_IN_MINUTE = new BigDecimal(60);


	
	@Autowired
	WaterTanksService waterTanksService;
	

	@Override
	public void run(String... args) throws Exception {
		List<WaterTank> waterTanks = new ArrayList<WaterTank>(numberOfTanks);
		for(int i=0; i<numberOfTanks; i++) {
			WaterTank waterTank = new WaterTank(i, maxTankCapacity, BigDecimal.ZERO);
			waterTanks.add(waterTank);
		}
		
		waterTanksService.populateInitialData(waterTanks);

		Runnable leakWater = new Runnable() {
		    public void run() {
				BigDecimal leakPerSecond = knownLeakPerMinute.divide(SECONDS_IN_MINUTE, 4, BigDecimal.ROUND_DOWN);
		        for(int i=0; i<waterTanks.size(); i++) {
					WaterTank waterTank = waterTanks.get(i);
					synchronized (waterTank.getCurrentCapacity()){
						BigDecimal currentCapacity = waterTank.getCurrentCapacity();
				        //System.out.println("tank id: " + waterTank.getId() + " capacity: " + waterTank.getCurrentCapacity().toString());

						if(currentCapacity != null && currentCapacity.compareTo(BigDecimal.ZERO) > 0) {
							waterTank.setCurrentCapacity(waterTank.getCurrentCapacity().subtract(leakPerSecond));
					        //System.out.println("tank id: " + waterTank.getId() + " capacity: " + waterTank.getCurrentCapacity().toString());
						}
					}
				}
		    }
		};

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(leakWater, 0, 1, TimeUnit.SECONDS);
	}

}
