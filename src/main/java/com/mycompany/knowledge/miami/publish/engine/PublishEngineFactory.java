package com.mycompany.knowledge.miami.publish.engine;

import com.mycompany.knwoledge.miami.publish.engine.gongan.JenaGonganPublishEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PublishEngineFactory {
    @Value("${engine.type}")
    String engineType;
    @Value(("${jena.modelName}"))
    String modelName;
    @Value(("${jena.fusekiURI}"))
    String fusekiURI;
    @Bean
    PublishEngine createEngine() {
        switch (engineType) {
            case "jena2gongan" : return new JenaGonganPublishEngine(fusekiURI, modelName);
            default:throw new RuntimeException(String.format("Now do not support engine type %s, except jena2gongan", engineType));
        }
    }
}
