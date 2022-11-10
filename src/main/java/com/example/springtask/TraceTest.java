package com.example.springtask;

import com.amazonaws.services.s3.AmazonS3;
import com.example.springtask.Service.FileService;
import com.example.springtask.Service.KafkaService;
import com.example.springtask.configuration.OtelConfig;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.*;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
//import io.opentelemetry.instrumentation.annotations.WithSpan;
//import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.OpenTelemetrySdkBuilder;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class TraceTest implements ApplicationRunner {

    @Autowired
    OpenTelemetry openTelemetry;

    @Autowired
    SdkTracerProvider sdkTracerProvider;
    @Autowired
    FileService fileService;
    @Autowired
    KafkaService kafkaService;

    @Autowired
    KafkaTemplate kafkaTemplate;
    private final Log logger = LogFactory.getLog(TraceTest.class);


    @Override
//    @WithSpan
    public void run(ApplicationArguments arg0) throws InterruptedException, IOException {
        logger.info("pk2");
            List<String> traceIdList = arg0.getOptionValues("traceId");
            String traceId = traceIdList.get(0);
        logger.info("pk2");
            List<String> spanIdList = arg0.getOptionValues("spanId");
            String spanId = spanIdList.get(0);
        logger.info("pk2");
            List<String> accessKeyList = arg0.getOptionValues("accessKey");
            String accesskey = accessKeyList.get(0);
        logger.info("pk2");
            List<String> secretkeyList = arg0.getOptionValues("secretKey");
            String secretkey = secretkeyList.get(0);
        logger.info("pk2");

//            List<String> traceStateList = Collections.singletonList(arg0.getOptionValues("traceState").toString());
//            String traceState = traceStateList.get(0);
//            List<String> traceFlagList = Collections.singletonList(arg0.getOptionValues("traceFlag").toString());
//            String traceFlag = traceFlagList.get(0);
        logger.info("secretkey + accesskey");
//            logger.info(secretkey + accesskey);
        logger.info("s3+++");



        SpanContext remoteContext = SpanContext.createFromRemoteParent(
                traceId,
                spanId,
                TraceFlags.getSampled(),
                TraceState.getDefault());

        Context.root().with(Span.wrap(remoteContext)).makeCurrent();

        Span span = openTelemetry.getTracer("test").spanBuilder("spanbuilder")
                .setParent(Context.current().with(Span.wrap(remoteContext))).startSpan();
        Scope ss = span.makeCurrent();


        fileService.uploadToS3(accesskey, secretkey,"exchangestorage", "test_file", "application/pdf");
//        kafkaService.sendMessageToTopic("tenantid","fileInformations","testTopic");
        kafkaTemplate.send("testTopic","test message with trace");
        kafkaTemplate.flush();

            logger.info(remoteContext.isValid());
//            logger.info(traceId+" +"+spanId);


//            Span span = openTelemetry.getTracer("tttttt").spanBuilder("spanbuilder").startSpan();



        logger.info(Span.current().getSpanContext().getTraceId());
        logger.info(Span.current().getSpanContext().getSpanId());
        logger.info("Test trace6");
        span.end();
        sdkTracerProvider.forceFlush();
    }
}
