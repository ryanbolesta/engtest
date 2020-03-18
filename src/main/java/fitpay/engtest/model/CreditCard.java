package fitpay.engtest.model;

import java.util.Objects;

public class CreditCard extends UserAsset {
    private String creditCardId;

    public CreditCard() { }

    public CreditCard(String creditCardId) {
        this.creditCardId = creditCardId;
    }

    public String getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(String creditCardId) {
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
