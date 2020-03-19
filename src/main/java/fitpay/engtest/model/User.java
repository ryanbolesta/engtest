package fitpay.engtest.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * User DTO that represents the response of the FitPay API individual user call
 */
@NoArgsConstructor
@Getter
@Setter
public class User extends AbstractBaseModel {

    @JsonSetter("_links")
    private Map<String, Link> links;
    private String id;

}
