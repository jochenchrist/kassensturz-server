package kassensturz.factory;

import org.springframework.web.client.RestTemplate;

/**
 * Very simple factory
 */
public class RestTemplateFactory {

    private static RestTemplate restTemplate = new RestTemplate();

    public static RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
