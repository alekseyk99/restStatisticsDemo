package com.alekseyk99.springboot.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.alekseyk99.springboot.WebApplication;
import com.alekseyk99.springboot.dto.Statistic;
import com.alekseyk99.springboot.dto.Transaction;
import com.alekseyk99.springboot.service.CustomFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebApplication.class })
@WebAppConfiguration
public class ControllerTest {

    private MockMvc mockMvc;
    
    private ObjectMapper objectMapper;

    public ControllerTest() {
        this.objectMapper = new ObjectMapper();
    }
    
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CustomFilter filter;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilters(filter).build();
    }
    
	@Test
	public void testGetStatistic() throws Exception {
	    
	    MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/statistics"))
	            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
	    
	    Statistic statistic = objectMapper.readValue(result.getResponse().getContentAsString(), Statistic.class);
	    Assert.assertEquals("Initial value", 0, statistic.getCount());
	}

	
    @Test
    public void testPostTransaction() throws Exception {
        
        Transaction transaction = new Transaction(10, 20);
        MvcResult result = this.mockMvc.perform(
                    MockMvcRequestBuilders.post("/transactions")
                    .content(objectMapper.writeValueAsString(transaction))
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        
    }

}

