//package com.presence.testpresence.config;
//
//import jdk.internal.loader.Resource;
//import org.apache.http.conn.HttpClientConnectionManager;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.ssl.SSLContextBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.ClientHttpRequestFactory;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//
//import javax.net.ssl.SSLContext;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.security.KeyManagementException;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.cert.CertificateException;
//
//@Configuration
//public class CustomRestTemplateConfiguration {
//
//    @Value("${trust.store}")
//    private Resource trustStore;
//
//    @Value("${trust.store.password}")
//    private String trustStorePassword;
//
//    @Bean
//    public RestTemplate restTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, MalformedURLException, IOException {
//
//        SSLContext sslContext = new SSLContextBuilder()
//                .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray()).build();
//        SSLConnectionSocketFactory sslConFactory = new SSLConnectionSocketFactory(sslContext);
//        HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
//                .setSSLSocketFactory(sslConFactory)
//                .build();
//        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
//        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
//        return new RestTemplate(requestFactory);
//    }
//}
