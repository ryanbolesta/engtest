package fitpay.engtest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Configuration
@Import(Application.class)
public class SpringTestConfig {

    @Bean
    @Primary
    public String fitPayAPIAccessToken(){
        return "access token";
    }

}
