package com.orangelala.service.ucenter.mapper.sql;

public class RegionSqlString {
	public static final String findRegionByLevel = "select id,name,pid,sname,level,citycode,yzcode,mername,Lng,Lat,pingyin from region where level=#{level}";
	public static final String findRegionBypid = "select id,name,pid,sname,level,citycode,yzcode,mername,Lng,Lat,pingyin from region where pid=#{pid}";
	public static final String findRegionByLevelAndLikeName = "select id,name,pid,sname,level,citycode,yzcode,mername,Lng,Lat,pingyin from region where level=#{levle} and name like #{name}";
	public static final String findRegionById = "select id,name,pid,sname,level,citycode,yzcode,mername,Lng,Lat,pingyin from region where id=#{id}";
}
