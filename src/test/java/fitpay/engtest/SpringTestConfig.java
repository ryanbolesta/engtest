package fitpay.engtest;

import fitpay.engtest.model.Token;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Configuration
@Import(Application.class)
public class SpringTestConfig {

    @Bean
    @Primary
    public Token fitPayAPIAccessToken(){
        return new Token("access token");
    }

}
