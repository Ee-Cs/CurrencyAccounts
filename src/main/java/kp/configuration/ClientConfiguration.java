package kp.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import static kp.Constants.NATIONAL_BANK_URL;

/**
 * The client configuration.
 */
@Configuration
public class ClientConfiguration {

    /**
     * Creates the {@link RestClient} bean.
     *
     * @return the rest client
     */
    @Bean
    RestClient restClient() {

        return RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .baseUrl(NATIONAL_BANK_URL).build();
    }
}