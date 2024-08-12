package com.zb.wiki.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class Oauth2Config {
    @Bean
    public RestClient restClient() {
      return RestClient.builder().build();
    }
}
