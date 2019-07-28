package com.orangelala.framework.model.cms;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
/**
 * 页面数据模型
 * @author chrilwe
 *
 */
@Document(collection = "cms_config")
@Data
@ToString
public class CmsConfig {

    @Id
    private String id;
    private String name;
    private CmsConfigModel model;
    
}
