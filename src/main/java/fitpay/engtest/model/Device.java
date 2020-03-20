package fitpay.engtest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Device extends UserAsset {
    private String deviceIdentifier;

    public Device(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

}
