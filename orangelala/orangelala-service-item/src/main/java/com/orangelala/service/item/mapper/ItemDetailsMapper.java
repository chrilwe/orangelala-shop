package com.orangelala.service.item.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.orangelala.framework.model.item.details.ItemDetails;
import com.orangelala.service.item.mapper.sql.ItemDetailsSqlString;

public interface ItemDetailsMapper {
	@Insert(ItemDetailsSqlString.add)
	public int add(ItemDetails itemDetails);
	
	@Delete(ItemDetailsSqlString.delete)
	public int delete(String id);
	
	@Update(ItemDetailsSqlString.update)
	public int update(ItemDetails itemDetails);
	
	@Select(ItemDetailsSqlString.findById)
	public ItemDetails findById(String id);
}
