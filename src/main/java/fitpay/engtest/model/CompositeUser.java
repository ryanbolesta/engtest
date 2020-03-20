package fitpay.engtest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CompositeUser extends AbstractBaseModel {
    @NonNull
    private String userId;
    private List<Device> devices;
    private List<CreditCard> creditCards;
}
