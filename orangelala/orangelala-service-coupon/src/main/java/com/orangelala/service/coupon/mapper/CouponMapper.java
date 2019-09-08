package com.orangelala.service.coupon.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.orangelala.framework.model.coupon.Coupon;
import com.orangelala.framework.model.coupon.CouponUser;
import com.orangelala.framework.model.coupon.MyCoupon;
import com.orangelala.service.coupon.mapper.sql.CouponSqlString;

public interface CouponMapper {
	@Insert(CouponSqlString.addCoupon)
	public int addCoupon(Coupon coupon);
	
	@Insert(CouponSqlString.addCouponUser)
	public int addCouponUser(CouponUser couponUser);
	
	@Select(CouponSqlString.findCouponByCidAndSid)
	public Coupon findCouponByCouponIdAndShopId(@Param("couponId")int couponId,@Param("shopId")String shopId,@Param("status")String status);

	@Update(CouponSqlString.updateCouponStatus)
	public int updateCouponStatus(Coupon coupon);
	
	//查询优惠券状态正常的领取次数小于最大领取次数的优惠券并且数量大于0
	@Select(CouponSqlString.findCouponByCIdAndStatus)
	public Coupon findCouponByCIdAndStatus(@Param("userId")String userId,@Param("status")String status,@Param("couponId")int couponId);

	//查询用户所有未过期的当前店铺优惠券和全品类优惠券
	@Select(CouponSqlString.findUsefullCouponList)
	public List<Coupon> findUsefullCouponList(@Param("userId")String userId,@Param("status")int status,@Param("shopId")String shopId);
	
	//findByCouponId
	@Select(CouponSqlString.findCouponById)
	public Coupon findCouponById(int couponId);
	
	//根据couponId和userId查询couponUser
	@Select(CouponSqlString.findCouponUserByCouponIdAndUserId)
	public List<CouponUser> findCouponUserByCouponIdAndUserId(@Param("couponId")int couponId,@Param("userId")String userId);
	
	//统计couponId=#{couponId}的数量
	@Select(CouponSqlString.countCouponId)
	public int count_couponId(@Param("couponId")int couponId,@Param("userId")String userId);

	//更新优惠券数量
	@Update(CouponSqlString.updateCouponNum)
	public int updateCouponNum(@Param("version")int version,@Param("couponId")int couponId);

	//根据couponId,userId,status查询couponUser
	@Select(CouponSqlString.findCouponUserByCouponIdAndUserIdAndStatus)
	public List<CouponUser> findCouponUserByCouponIdAndUserIdAndStatus(@Param("couponId")int couponId,@Param("userId")String userId,@Param("status")int status);
	
	@Update(CouponSqlString.updateCouponUserStatus)
	public int updateCouponUserStatus(CouponUser couponUser);
	
	//根据userId查询未使用的优惠券的数量以及coupon信息
	@Select(CouponSqlString.findNoUsedCoupon)
	public List<MyCoupon> findNoUsedCoupon(@Param("userId")String userId,@Param("couponUserStatus")int couponUserStatus);
}
