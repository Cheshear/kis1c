package com.example.authorization;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class SpringMongoConfig {
    public @Bean
    MongoTemplate mongoTemplate() throws Exception {

        MongoTemplate mongoTemplate =
                new MongoTemplate(new MongoClient("localhost"),"local");
        return mongoTemplate;

    }
}
