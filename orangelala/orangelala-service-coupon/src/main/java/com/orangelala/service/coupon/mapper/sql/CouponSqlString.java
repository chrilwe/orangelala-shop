package com.orangelala.service.coupon.mapper.sql;

public class CouponSqlString {
	public static final String addCoupon = "insert into orangelala_coupon(id,name,type,status,money,sales_money,start_time,end_time,create_time,getTimes,coupon_num,shop_id,isUsedWithOther,version) values(#{id},#{name},#{type},#{status},#{money},#{salesMoney},#{startTime},#{endTime},#{createTime},#{getTimes},#{couponNum},#{shopId},#{isUsedWithOther},#{version})";
	public static final String addCouponUser = "insert into orangelala_coupon_user(id,coupon_id,user_id,status,create_time,update_time) values(#{id},#{couponId},#{userId},#{status},#{createTime},#{updateTime})";
	public static final String findCouponByCidAndSid = "select id,name,type,status,money,sales_money as salesMoney,start_time as startTime,end_time as endTime,create_time as createTime,getTimes,coupon_num as couponNum,shop_id as shopId,isUsedWithOther,version from orangelala_coupon where id=#{couponId} and shop_id=#{shopId} and status=#{status} and end_time<now()";
	public static final String updateCouponStatus = "update orangelala_coupon set status=#{status} where shopId=#{shopId} and couponId=#{couponId}";
	//查询优惠券状态正常的领取次数小于最大领取次数并且优惠券数量大于0的优惠券
	public static final String findCouponByCIdAndStatus = "select a.id,a.name,a.type,a.status,a.money,a.sales_money as salesMoney,a.start_time as startTime,a.end_time as endTime,a.create_time as createTime,a.getTimes,a.coupon_num as couponNum,a.shop_id as shopId,a.isUsedWithOther,a.version,count(b.coupon_id) from orangelala_coupon a left join orangelala_coupon_user b on a.id=b.coupon_id and b.user_id=#{userId} and a.status=#{status} and a.id=#{couponId} group by a.id having count(b.coupon_id)>a.getTimes and a.end_time>now() and a.coupon_num>0";
	//查询当前用户未过期未使用的店铺优惠券和全品类优惠券
	public static final String findUsefullCouponList = "select id,name,type,status,money,sales_money as salesMoney,start_time as startTime,end_time as endTime,create_time as createTime,getTimes,coupon_num as couponNum,shop_id as shopId,isUsedWithOther,version from orangelala_coupon a where a.id in(select coupon_id from orangelala_coupon_user b where b.user_id=#{userId} and b.status=#{status} group by b.coupon_id) and (shop_id=#{shopId} or shop_id is null) and end_time > now()";
	//findById
	public static final String findCouponById = "select id,name,type,status,money,sales_money as salesMoney,start_time as startTime,end_time as endTime,create_time as createTime,getTimes,coupon_num as couponNum,shop_id as shopId,isUsedWithOther,version from orangelala_coupon where id=#{couponId}";
	public static final String findCouponUserByCouponIdAndUserId = "select id,coupon_id as couponId,user_id as userId,status,create_time as createTime,update_time as updateTime from orangelala_coupoon_user where coupon_id=#{couponId} and user_id=#{userId}";
	public static final String countCouponId = "select count(coupon_id) from orangelala_coupon_user where user_id=#{userId} and coupon_id=#{couponId}";
	public static final String updateCouponNum = "update orangelala_coupon set version=#{version}+1,coupon_num=#{couponNum} where coupon_id=#{couponId}";
	public static final String findCouponUserByCouponIdAndUserIdAndStatus = "select id,coupon_id as couponId,user_id as userId,status,create_time as createTime,update_time as updateTime from orangelala_coupon_user where coupon_id=#{couponId} and status=#{status} and user_id=#{userId}";
	public static final String updateCouponUserStatus = "update orangelala_coupon_user set status=#{status},update_time=#{updateTime} where user_id=#{userId},coupon_id=#{couponId}";
	public static final String findNoUsedCoupon = "select a.id,a.name,a.type,a.status,a.money,a.sales_money as salesMoney,a.start_time as startTime,a.end_time as endTime,a.create_time as createTime,a.getTimes,a.coupon_num as couponNum,a.shop_id as shopId,a.isUsedWithOther,a.version,count(b.coupon_id) as owner from orangelala_coupon a left join orangelala_coupon_user b on a.id=b.coupon_id and user_id=#{userId} and b.status=#{couponUserStatus} group by a.id having count(b.coupon_id)>0";
}
