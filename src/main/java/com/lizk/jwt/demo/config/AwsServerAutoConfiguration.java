package com.lizk.jwt.demo.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 文件名
 * Created at 2020/3/10
 * Created by lizongke
 * Copyright (C) 2020 SAIC VOLKSWAGEN, All rights reserved.
 */
@Configuration
public class AwsServerAutoConfiguration {

    @Bean
    public AmazonS3 amazonS3Client() {
        AmazonS3 amazonS3Client = null;
        try {
            BasicAWSCredentials credentials = new BasicAWSCredentials("4182B2F7E60EDA1E1030",
                    "W0E2ODZDNkJEOTM4RkZGQzFEQkFFOEU1Rjc5NkVEMjE4QzI4QzgxNkZd");
            AWSStaticCredentialsProvider awsCredent = new AWSStaticCredentialsProvider(credentials);
            ClientConfiguration clientConfig = new ClientConfiguration();
            if ("https".equalsIgnoreCase("http")) {
                clientConfig.setProtocol(Protocol.HTTPS);
            } else {
                clientConfig.setProtocol(Protocol.HTTP);
            }
            AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                    "172.20.20.3:81", "172.20.20.3:81");
            clientConfig.setSignerOverride("S3SignerType");
            amazonS3Client = AmazonS3ClientBuilder.standard().withCredentials(awsCredent)
                    .withClientConfiguration(clientConfig).withEndpointConfiguration(endpointConfiguration).build();
            //AmazonS3  s3Client =new AmazonS3Client(credentials, clientConfig);
            //s3Client.setEndpoint(s3ServerProperties.getRegion());
        } catch (Exception ex) {
           System.out.println(ex.getMessage());
        }
        return amazonS3Client;
    }

}
