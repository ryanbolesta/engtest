package fitpay.engtest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Link extends AbstractBaseModel {

    @NonNull
    private String href;
    private boolean templated;

    public Link(String href) {
        this.href = href;
    }

}
