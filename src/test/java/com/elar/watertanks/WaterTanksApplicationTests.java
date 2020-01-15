package com.elar.watertanks;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import javax.swing.text.AbstractDocument.Content;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.*;
import org.springframework.test.web.servlet.result.*;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class WaterTanksApplicationTests {	
	
	public static final BigDecimal MAX_CAPACITY = new BigDecimal(50);

	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void getMaxCapacityTest(){
		try {
			MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8083/watertanks/api/queryMaxCapacity?tankId=0")
					.content("").contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
			assertEquals(MAX_CAPACITY.toString(), result.getResponse().getContentAsString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void addWaterOverflowTest(){
		try {
			
			String uri = "http://localhost:8083/watertanks/api/addWater?tankId=0&volume=";
			BigDecimal volume = MAX_CAPACITY.add(new BigDecimal(2));
			uri = uri + volume.toString();
			MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri)
					.content("").contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
			assertEquals("false", result.getResponse().getContentAsString());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void addWaterNegativeTest(){
		try {
			
			String uri = "http://localhost:8083/watertanks/api/addWater?tankId=0&volume=-5";
			MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri)
					.content("").contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
			assertEquals("false", result.getResponse().getContentAsString());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void waterLeakTest(){
		try {

			BigDecimal currentCapacity = getCurrentCapacity();

			BigDecimal volumeToAdd = MAX_CAPACITY.subtract(currentCapacity);
			
			String uri = "http://localhost:8083/watertanks/api/addWater?tankId=0&volume=";
			
			uri = uri + volumeToAdd;
			MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri)
					.content("").contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
			assertEquals("true", result.getResponse().getContentAsString());
			
			Thread.sleep(1000);
			
			uri = "http://localhost:8083/watertanks/api/addWater?tankId=0&volume=0.016";
			result = mockMvc.perform(MockMvcRequestBuilders.post(uri)
					.content("").contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
			assertEquals("true", result.getResponse().getContentAsString());
			
			Thread.sleep(1000);
			
			uri = "http://localhost:8083/watertanks/api/addWater?tankId=0&volume=0.2";
			result = mockMvc.perform(MockMvcRequestBuilders.post(uri)
					.content("").contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
			assertEquals("false", result.getResponse().getContentAsString());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private BigDecimal getCurrentCapacity() throws Exception {
		MvcResult currentCapacityResult = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8083/watertanks/api/queryCurrentCapacity?tankId=0")
				.content("").contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		
		String currentCapacityStr = currentCapacityResult.getResponse().getContentAsString();
		return new BigDecimal(currentCapacityStr);
	}
	
	@Test
	public void addWaterMultithreadingTest() throws Exception{

			AddWaterThread threads[] = new AddWaterThread[10];
			for (int i = 0; i < threads.length; i++) {
				threads[i] = new AddWaterThread();
				threads[i].start();	
			}
			
			BigDecimal currentCapacity = getCurrentCapacity();
			assertTrue(currentCapacity.compareTo(MAX_CAPACITY) < 0 || currentCapacity.compareTo(MAX_CAPACITY) == 0);
			
			for (int i = 0; i < threads.length; i++) {
				threads[i].shutdown();
			}
	}
	
	public class AddWaterThread extends Thread{
		private volatile boolean shutdown = false;
		String uri = "http://localhost:8083/watertanks/api/addWater?tankId=0&volume=49";

	    public void run() {
	    	while (!shutdown) {
				try {
					mockMvc.perform(MockMvcRequestBuilders.post(uri)
								.content("").contentType(MediaType.APPLICATION_JSON))
						.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
				} catch (Exception e) {
					// TODO : logging
					e.printStackTrace();
				}
	    	}
			
	    }
	    
	    public void shutdown() {
	        shutdown = true;
	    }
	}
	 

}
