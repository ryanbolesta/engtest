package fitpay.engtest.model;

import java.util.Objects;

public class Device extends UserAsset {
    private String deviceIdentifier;

    public Device() {}

    public Device(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
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
