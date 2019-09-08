package com.orangelala.service.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.orangelala.framework.model.shop.Activity;
import com.orangelala.framework.model.shop.ActivityDetail;
import com.orangelala.service.shop.mapper.sql.Sql;

public interface ActivityMapper {
	//创建满减促销活动
	@Insert(Sql.insertActivity)
	@Options(useGeneratedKeys=true,keyProperty="id")
	public void insertActivity(Activity activity);
	
	//创建促销活动detail
	@Insert(Sql.insertActivityDetail)
	public void insertActivityDetail(ActivityDetail activityDetail);
	
	//更新活动状态
	@Update(Sql.updateActivityStatus)
	public void updateActivityStatus(@Param("status")int status,@Param("activityId")int activityId);
	@Update(Sql.updateActivityDetailStatus)
	public void updateActivityDetailStatus(@Param("status")int status,@Param("activityId")int activityId);
	
	//根据activityId查询activity
	@Select(Sql.findActivityById)
	public Activity findActivityById(int activityId);
	
	//根据店铺号查询优先级别最高和可以叠加的状态正常的未过期的活动
	@Select(Sql.findUsefullActivity)
	public List<Activity> findUsefullActivity(String shopId);
	
	//根据活动号查询activityDetail并且通过payMoney进行升序排序
	@Select(Sql.findActivityDetailAscByPaymoney)
	public List<ActivityDetail> findActivityDetailAscByPayMoney(int activityId);
}
