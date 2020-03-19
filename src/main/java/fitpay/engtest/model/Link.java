package fitpay.engtest.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Link extends AbstractBaseModel {

    private String href;
    private boolean templated;

    public Link(String href) {
        this.href = href;
    }

}
