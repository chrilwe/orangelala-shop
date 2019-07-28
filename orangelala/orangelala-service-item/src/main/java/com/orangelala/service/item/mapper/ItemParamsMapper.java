package com.orangelala.service.item.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.orangelala.framework.model.item.params.ItemParams;
import com.orangelala.service.item.mapper.sql.ItemParamsSqlString;

public interface ItemParamsMapper {
	@Insert(ItemParamsSqlString.add)
	public int add(ItemParams itemParams);
	
	@Delete(ItemParamsSqlString.delete)
	public int delete(String id);
	
	@Update(ItemParamsSqlString.update)
	public int update(ItemParams itemParams);
	
	@Select(ItemParamsSqlString.findById)
	public ItemParams findById(String id);
}
