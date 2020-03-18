package fitpay.engtest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Abstract class for user assets. A user asset is considered an attribute of a
 * user with a 'state' attribute, such as credit cards and devices
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class UserAsset implements Serializable {
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
