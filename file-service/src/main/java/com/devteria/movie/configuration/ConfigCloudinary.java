package com.devteria.movie.configuration;
import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfigCloudinary {
    @Bean
    public Cloudinary configKey() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "deuq8fzmg");
        config.put("api_key", "288389138564278");
        config.put("api_secret", "pRG1bwi79rBiPUQ-fGacDItTwRg");
        return new Cloudinary(config);
    }
}
