package fitpay.engtest.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Token extends AbstractBaseModel {

    @JsonSetter("access_token")
    private String accessToken;

    public Token(String accessToken) {
        this.accessToken = accessToken;
    }

}
