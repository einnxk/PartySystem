package de.einnik.party.http;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * Class to provide an Http Client
 */
public class HttpClientProvider {

    /**
     * Http Client, with Custom Build
     */
    public static final HttpClient CLIENT =
            HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(7))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
}