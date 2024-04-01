package org.andulir.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置类，与配置文件对应
 */
@Component
@ConfigurationProperties(prefix = "andulir")
@Data
@NoArgsConstructor
public class AndulirProperty {
    private String scanPackage;
}
