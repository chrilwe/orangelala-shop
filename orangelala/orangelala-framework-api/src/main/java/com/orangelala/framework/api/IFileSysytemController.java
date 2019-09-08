package com.orangelala.framework.api;
/**
 * 文件管理系统
 * @author chrilwe
 *
 */

import org.springframework.web.multipart.MultipartFile;

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.filesysytem.response.ImageUploadResponse;

public interface IFileSysytemController {
	//上传图片到fdfs
	public ImageUploadResponse uploadImage(MultipartFile image);
	
	//上传省份EXCEL文件
	public BaseResponse uploadRegionFile(MultipartFile regionFile);
	
	//导出省份EXCEL文件
	public BaseResponse downloadRegionFile();
}
