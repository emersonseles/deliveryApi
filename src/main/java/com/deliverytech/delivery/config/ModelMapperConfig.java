package com.deliverytech.delivery.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // configurações do ModelMapper para mapeamento de objetos
        mapper.getConfiguration()
                /* define a estratégia de correspondência como STRICT
                 para garantir que apenas campos com nomes e tipos
                 correspondentes sejam mapeados
                */
                .setMatchingStrategy(MatchingStrategies.STRICT)
                /* define se a correspondência de campos está habilitada */
                .setFieldMatchingEnabled(true)
                /* define o nível de acesso aos campos */
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        return mapper;
    }
}