package fitpay.engtest.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class CreditCard extends UserAsset {
    private String creditCardId;

    public CreditCard(String creditCardId) {
        this.creditCardId = creditCardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditCard)) return false;
        CreditCard that = (CreditCard) o;
        return creditCardId.equals(that.creditCardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creditCardId);
    }
}
