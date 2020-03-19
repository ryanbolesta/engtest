package fitpay.engtest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class AbstractBaseModel implements Serializable {
}
