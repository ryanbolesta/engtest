package fitpay.engtest.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.Map;

/**
 * User DTO that represents the response of the FitPay API individual user call
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class User extends AbstractBaseModel {

    private String userId;

    @JsonSetter("_links")
    @EqualsAndHashCode.Exclude
    private Map<String, Link> links;



}
