package org.andulir.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "andulir.thread")
@Data
@NoArgsConstructor
public class ThreadPoolProperty {

    private Integer coreSize;

    private Integer maxSize;

    private Integer keepAliveTime;
}
