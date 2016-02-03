package com.appfront.server;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * The main class.
 * 
 * @author ente
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableElasticsearchRepositories(basePackages = "com/appfront/server/repositories")
public class AppFrontServer {
    
    /**
     * Initialize the ElasticSearch operating object
     * 
     * @return the operating object
     */
    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(nodeBuilder().local(true).node().client());
    }
    
    /**
     * Main().
     * 
     * @param args
     *            command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(AppFrontServer.class, args);
    }
}
