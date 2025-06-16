package com.example.crud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {    @Override
    public void addFormatters(FormatterRegistry registry) {
        // Register date formatter
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true); // This will use ISO date format (yyyy-MM-dd)
        registrar.registerFormatters(registry);
        
        // Add custom formatter for Integer to handle decimal input
        registry.addFormatterForFieldType(Integer.class, new org.springframework.format.number.NumberStyleFormatter("###") {
            @Override
            public Integer parse(String text, java.util.Locale locale) {
                if (text != null && text.contains(".")) {
                    // Convert decimal string to integer
                    double doubleValue = Double.parseDouble(text);
                    return (int) doubleValue;
                }
                try {
                    return ((Number) super.parse(text, locale)).intValue();
                } catch (java.text.ParseException e) {
                    throw new RuntimeException("Failed to parse integer: " + text, e);
                }
            }
        });
    }
}
