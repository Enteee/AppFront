package com.appfront.server.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.appfront.server.AppFrontServer;

/**
 * JUnit test for {@link com.appfront.server.user.RESTUser}
 * 
 * @author ente
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class TestRESTUser {
    
    private MockMvc mockMvc;
    /**
     * @author ente
     */
    @Configuration
    @EnableWebMvc
    public static class TestConfiguration {
        
        /**
         * @return AppFront instance to test
         */
        @Bean
        public AppFrontServer appFrontServer() {
            return new AppFrontServer();
        }
    }
    @Autowired
    private WebApplicationContext wac;
    
    /**
     * Setup test environment.
     * 
     * @throws Exception
     */
    @Before
    public void setup() throws Exception {
        mockMvc = webAppContextSetup(wac).build();
    }
    
    /**
     * Test for {@link com.appfront.server.user.RESTUser#registerNewUser()}
     * 
     * @throws Exception
     */
    @Test
    public void registerNewUser() throws Exception {
        mockMvc.perform(put("/user/_new")).andDo(print())
        // HTTP 200 returned
                .andExpect(status().isOk());
    }
}
