package me.atm.foodie.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 上传头像
 *
 * @author Altman
 * @date 2019/12/03
 **/
@Data
@Component
@ConfigurationProperties(prefix = "system.face")
public class UploadFaceFileProperties {
    private String url;
    private String domain;
}
