package fitpay.engtest.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * Holds properties needed for connecting to the FitPay API.
 */
@ConstructorBinding
@ConfigurationProperties(prefix="fitpay.api")
@Validated
public class FitPayAPIProperties {

    @NotBlank
    private final String clientId;

    @NotBlank
    private final String secret;

    @NotBlank
    private final String baseUrl;

    @NotBlank
    private final String tokenUrl;

    public FitPayAPIProperties(@NotBlank String clientId, @NotBlank String secret, @NotBlank String baseUrl, @NotBlank String tokenUrl) {
        this.clientId = clientId;
        this.secret = secret;
        this.baseUrl = baseUrl;
        this.tokenUrl = tokenUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getSecret() {
        return secret;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

}
