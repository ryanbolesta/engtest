package fitpay.engtest;

import fitpay.engtest.properties.FitPayAPIProperties;
import fitpay.engtest.service.FitPayAPIService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableConfigurationProperties(FitPayAPIProperties.class)
@EnableAsync
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * RestTemplate bean for making API requests
     */
    @Bean
    public RestTemplate fitPayRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    /**
     * Executor bean for performing asynchronous actions in the application
     */
    @Bean
    public Executor asyncExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    /**
     * Bean for the API access token. We retrieve it once on startup.
     * NOTE: There is not logic here to renew the token when it expires
     */
    @Bean
    public String fitPayAPIAccessToken(FitPayAPIService fitPayAPIService) throws Exception {
        return fitPayAPIService.getAccessToken();
    }

}
