package com.orangelala.framework.model.item.category;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 商品分类
 * @author chrilwe
 *
 */
@Data
@ToString
public class ItemCategory {
	private Long id;

    private Long parentId;

    private String name;

    private Integer status;

    private Integer sortOrder;

    private long isParent;

    private Date created;

    private Date updated;
}
