package com.example.springtask.configuration;

import com.example.model.FileInformations;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.kafkaclients.KafkaTelemetry;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Autowired
    OpenTelemetry openTelemetry;

    @Bean(name = "producerInstrumentation")
    public DefaultKafkaProducerFactoryCustomizer producerInstrumentation() {
        KafkaTelemetry kafkaTelemetry = KafkaTelemetry.create(openTelemetry);
        return producerFactory -> producerFactory().addPostProcessor(kafkaTelemetry::wrap);
    }


    @Bean
    public DefaultKafkaProducerFactory<String, FileInformations> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "broker:29092");
//                "localhost:9092");
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                FileInformations.class);
        configProps.put(
                ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,
                "120000");
        return new DefaultKafkaProducerFactory<>(configProps);
    }


    @Bean(name = "kafkaTemplate")
    public KafkaTemplate<String, FileInformations> kafkaTemplate() {
        producerInstrumentation().customize(producerFactory());
        return new KafkaTemplate<>(producerFactory());
    }

    /*@Bean(name = "kafkaFileTemplate")
    public KafkaTemplate<String, byte[]> kafkaFileTemplate() {
        return new KafkaTemplate<>(producerFileFactory());
    }*/



}

