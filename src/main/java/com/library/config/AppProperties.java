// 例: src/main/java/com/example/library/config/AppProperties.java

package com.library.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app") 
@Component
public class AppProperties {

    // application.properties / application-local.propertiesの
    // "app.jwtSecret" に対応
    private String jwtSecret;

    // "app.jwtExpirationM" に対応
    private long jwtExpirationMs;

    // Getter and Setter (Lombokを使用しない場合は手動で記述が必要です)
    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public long getJwtExpirationMs() {
        return jwtExpirationMs;
    }

    public void setJwtExpirationMs(long jwtExpirationMs) {
        this.jwtExpirationMs = jwtExpirationMs;
    }
}