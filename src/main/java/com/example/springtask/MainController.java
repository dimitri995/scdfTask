//package com.example.springtask;
//
//
//import io.opentelemetry.api.OpenTelemetry;
//import io.opentelemetry.api.trace.Span;
//import io.opentelemetry.api.trace.Tracer;
//import io.opentelemetry.context.Scope;
//import io.opentelemetry.instrumentation.annotations.WithSpan;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import reactor.core.publisher.Mono;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//@Controller
//@Slf4j
//public class MainController {
//
//    @Autowired
//    OpenTelemetry openTelemetry;
//
//
//    @GetMapping("/trace/{traceId}")
//    public void getTrace() {
//        Tracer tracer = openTelemetry.getTracer("spring-cloud");
//
//        Span span = tracer.spanBuilder("span").startSpan();
////        Context.current().makeCurrent();
//        Scope ss = span.makeCurrent();
//
//
//        log.info(Span.current().getSpanContext().getTraceId());
//        log.info(String.valueOf(Span.current().getSpanContext()));
//        span.end();
//    }
//
//}
