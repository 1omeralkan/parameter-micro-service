package com.omeralkan.parameter.config;

import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OperationCustomizer customGlobalHeaders() {
        return (operation, handlerMethod) -> {
            Parameter acceptLanguage = new Parameter()
                    .in("header")
                    .schema(new StringSchema()._default("tr")) // Varsayılan olarak 'tr' dolu gelsin
                    .name("Accept-Language")
                    .description("Dil Seçeneği (İngilizce için 'en', Türkçe için boş bırakın veya 'tr' yazın)")
                    .required(false);

            operation.addParametersItem(acceptLanguage);
            return operation;
        };
    }
}