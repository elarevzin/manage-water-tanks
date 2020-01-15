package com.elar.watertanks.api;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.elar.watertanks.services.WaterTanksService;

@RestController
@RequestMapping("watertanks/api")
public class WaterTanksController {
	
	
	
	@Autowired
	private WaterTanksService waterTanksService;
	
	@GetMapping("/queryMaxCapacity")
	@ResponseStatus(HttpStatus.OK)
	public BigDecimal queryMaxCapacity(@RequestParam(value = "tankId", required = true) Integer tankId) {
		return this.waterTanksService.queryMaxCapacity(tankId);
	}
	@GetMapping("/queryCurrentCapacity")
	@ResponseStatus(HttpStatus.OK)
	public BigDecimal queryCurrentCapacity(@RequestParam(value = "tankId", required = true) Integer tankId) {
		return this.waterTanksService.queryCurrentCapacity(tankId);
	}
	
	@PostMapping("/addWater")
	@ResponseStatus(HttpStatus.OK)
	public boolean addWater(@RequestParam(value = "tankId", required = true) Integer tankId, @RequestParam(value = "volume", required = true) BigDecimal volumeInLiters) {
		return this.waterTanksService.addWater(tankId, volumeInLiters);
	}

}
