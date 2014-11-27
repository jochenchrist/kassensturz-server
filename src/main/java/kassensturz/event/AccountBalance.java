package kassensturz.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountBalance {
    public BigDecimal amount;
    public String type;
}
