package fitpay.engtest.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class CompositeUser extends AbstractBaseModel {
    private String userId;
    private List<Device> devices;
    private List<CreditCard> creditCards;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompositeUser)) return false;
        CompositeUser that = (CompositeUser) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
