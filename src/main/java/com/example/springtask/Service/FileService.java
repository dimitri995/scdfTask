package com.example.springtask.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Tag;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.opentelemetry.instrumentation.awssdk.v1_11.AwsSdkTelemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    @Autowired
    OpenTelemetry openTelemetry;

    public AmazonS3 s3Client(String accesskey, String secretKey){
        AWSCredentials credentials = new BasicAWSCredentials(
                accesskey,
                secretKey
        );
        return AmazonS3ClientBuilder
                .standard()
                .withRequestHandlers(AwsSdkTelemetry.builder(openTelemetry)
                        .setCaptureExperimentalSpanAttributes(true)
                        .build()
                        .newRequestHandler())
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("arn:aws:s3:eu-central-1:289305837948:accesspoint/files", Regions.EU_CENTRAL_1.toString()))
                .build();
    }

    public void uploadToS3(String accessKey, String secretKey,String bucketName, String filename, String contentType) throws IOException {
        AmazonS3 s3Client= s3Client(accessKey, secretKey);
        if(!s3Client.doesBucketExistV2(bucketName)) {
            s3Client.createBucket(bucketName);
        }

//        InputStream targetStream = new ByteArrayInputStream(fileInBytes);
        URL url = new URL("https://play-lh.googleusercontent.com/1-hPxafOxdYpYZEOKzNIkSP43HXCNftVJVttoo4ucl7rsMASXW3Xr6GlXURCubE1tA=w3840-h2160-rw");
        InputStream in = new BufferedInputStream(url.openStream());
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        byte[] buf = new byte[1024];
//        int n = 0;
//        while (-1!=(n=in.read(buf)))
//        {
//            out.write(buf, 0, n);
//        }
//        out.close();
//        in.close();
//        byte[] response = out.toByteArray();
//        String path = "D:\\Users\\dimitri.hebrard\\projet\\BL.EXCHANGE\\tracabilite\\AdminService\\src\\main\\resources\\file\\custom.pdf";
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(url.getFile().length());

        PutObjectRequest putRequest = new PutObjectRequest(bucketName,filename, in,metadata);

        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("lifecycle", "true"));
        putRequest.setTagging(new ObjectTagging(tags));

        s3Client.putObject(putRequest);
    }


    @WithSpan(value = "this is span withspan")
    public void saveFile(@SpanAttribute String filename) {
//        Tracer tracer =
//                openTelemetry.getTracer("instrumentation-library-name", "1.0.0");
        Span span = Span.current();
//        Span span = tracer.spanBuilder("save file local").startSpan();

//        Scope ss = span.makeCurrent();
        span.setAttribute("size", "95441");
        System.out.print("p1");
        System.out.print("p2");
        System.out.print("p3");
        try {
//           System.out.println(1/0);
        } catch (Exception e) {
            span.recordException(e);
            throw new RuntimeException(e);
        }finally {
            span.end();
        }

    }
}
