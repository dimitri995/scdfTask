package com.example.springtask.configuration;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OtelConfig {

    @Bean
    public OpenTelemetry openTelemetry() {

//        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
//                .registerMetricReader(PeriodicMetricReader.builder(OtlpHttpMetricExporter.builder().build()).build())
//                .setResource(resource)
//                .build();

        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider())
//                .setMeterProvider(sdkMeterProvider)
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .buildAndRegisterGlobal();

        return openTelemetry;
    }

    @Bean
    public SdkTracerProvider sdkTracerProvider() {

        Resource resource = Resource.getDefault()
                .merge(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, "taskService")));

        SpanExporter spanExporter = OtlpHttpSpanExporter.builder()
                .setEndpoint("http://otel-collector:4318/v1/traces") //TODO Replace <URL> to the Collector URL
//                .setEndpoint("http://localhost:4318/v1/traces") //TODO Replace <URL> to the Collector URL
                .build();

        LoggingSpanExporter loggingSpanExporter = new LoggingSpanExporter();

        return SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(loggingSpanExporter).build())
                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
                .setResource(resource)
                .build();
    }
}
