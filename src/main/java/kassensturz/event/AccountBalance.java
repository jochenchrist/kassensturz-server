package kassensturz.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountBalance {
    public BigDecimal amount;
    public String type;
    public Instant timestamp;
}
