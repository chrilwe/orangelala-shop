package com.orangelala.framework.model.ucenter;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Region {
	private int id;
	private String name;
	private int pid;
	private String sname;
	private int level;
	private String citycode;
	private String yzcode;
	private String mername;
	private float Lng;
	private float Lat;
	private String pinyin;
}
