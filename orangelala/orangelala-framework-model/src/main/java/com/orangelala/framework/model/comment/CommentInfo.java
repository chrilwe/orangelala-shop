package com.orangelala.framework.model.comment;
/**
 * 用户评价信息
 * @author chrilwe
 *
 */

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CommentInfo {
	private String id;
	private String itemId;
	private String userId;
	//评价文字信息
	private String message;
	//评价等级：低于3颗星差评，3颗星一般，高于3颗星满意 
	private long grade;
	//评价创建时间
	private Date createTime;
	//是否匿名评价
	private boolean anonymous;
}
