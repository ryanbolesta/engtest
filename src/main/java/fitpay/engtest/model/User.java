package fitpay.engtest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Map;

/**
 * User DTO that represents the response of the FitPay API individual user call
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class User {

    private Map<String, Link> links;
    private String id;

    public Map<String, Link> getLinks() {
        return links;
    }

    @JsonSetter("_links")
    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
