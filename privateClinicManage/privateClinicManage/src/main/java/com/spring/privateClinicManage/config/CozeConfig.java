package com.spring.privateClinicManage.config;

import io.github.flyinox.coze4j.CozeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class CozeConfig {

    @Autowired
    private Environment env;

    @Bean
    public CozeClient cozeClient() {
        return new CozeClient(env.getProperty("COZE_API_TOKEN"), env.getProperty("COZE_COM_BASE_URL"));
    }

}
