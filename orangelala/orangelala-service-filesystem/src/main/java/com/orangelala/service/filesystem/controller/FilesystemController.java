package com.orangelala.service.filesystem.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.orangelala.framework.api.IFileSysytemController;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.filesysytem.response.ImageUploadResponse;
/**
 * 文件管理
 * @author chrilwe
 *
 */
@RestController
@RequestMapping("/filesystem")
public class FilesystemController implements IFileSysytemController {
	
	//上传图片
	@Override
	@PostMapping("/uploadImage")
	public ImageUploadResponse uploadImage(MultipartFile image) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//上传区域文件
	@Override
	@PostMapping("/uploadRegionFile")
	public BaseResponse uploadRegionFile(MultipartFile regionFile) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//下载区域文件
	@Override
	@PostMapping("/downloadRegionFile")
	public BaseResponse downloadRegionFile() {
		// TODO Auto-generated method stub
		return null;
	}

}
