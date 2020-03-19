package fitpay.engtest.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class Device extends UserAsset {
    private String deviceIdentifier;

    public Device(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;
        Device device = (Device) o;
        return deviceIdentifier.equals(device.deviceIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceIdentifier);
    }
}
