package com.orangelala.framework.common.filesysytem.response;

import com.orangelala.framework.common.base.BaseResponse;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ImageUploadResponse extends BaseResponse {

	public ImageUploadResponse(int code, String msg) {
		super(code, msg);
		// TODO Auto-generated constructor stub
	}
	private String imageUrl;
}
