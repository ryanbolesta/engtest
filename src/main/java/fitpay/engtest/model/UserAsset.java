package fitpay.engtest.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class for user assets. A user asset is considered an attribute of a
 * user with a 'state' attribute, such as credit cards and devices
 */
@Getter
@Setter
public abstract class UserAsset extends AbstractBaseModel {
    private String state;
}
