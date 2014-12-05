package kassensturz.configuration;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Hystrix is used to separate the ThreadPools when calling external services.
 *
 * When more than a given number of failures (default: 20) occur in a time frame (default: 10s), then a defined fallback
 * method is used.
 * *
 * For more information see https://github.com/Netflix/Hystrix/wiki
 */
@Configuration
public class HystrixConfiguration {

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return new ServletRegistrationBean(new HystrixMetricsStreamServlet(), "/hystrix.stream");
    }

}
