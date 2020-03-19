package fitpay.engtest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CreditCard extends UserAsset {
    private String creditCardId;

    public CreditCard(String creditCardId) {
        this.creditCardId = creditCardId;
    }

}
