package com.orangelala.service.shop.mapper.sql;

public class Sql {
	public static final String insertActivity = "insert into orangelala_activity(id,shop_id,title,start_time,end_time,grade,isStartWithOther,status) values(#{id},#{shopId},#{title},#{startTime},#{endTime},#{grade},#{isStartWithOther},#{status})";
	public static final String insertActivityDetail = "insert into orangelala_activity_detail(id,activity_id,pay_money,price,status) values(#{id},#{activityId},#{payMoney},#{price},#{status})";
	public static final String updateActivityStatus = "update orangelala_activity set status=#{status} where id=#{activityId}";
	public static final String updateActivityDetailStatus = "update orangelala_activity_detail set status=#{status} where activity_id=#{activityId}";
	public static final String findActivityById = "select id,shop_id as shopId,title,start_time as startTime,end_time as endTime,grade,isStartWithOther,status from orangelala_activity where id=#{activityId}";
	public static final String findUsefullActivity = "select id,shop_id as shopId,title,start_time as startTime,end_time as endTime,grade,isStartWithOther,status from oragelala_activity where shop_id=#{shopId} and status=#{status} and end_time>now() and (grade in (select max(grade) from orangelala_activity where shop_id=#{shopId}) or isStartWithOther=#{isStartWithOther})";
	public static final String findActivityDetailAscByPaymoney = "select id,activity_id as activityId,pay_money as payMoney,price,status from orangelala_activity_detail where activity_id=#{activityId} order by pay_money asc";
}
