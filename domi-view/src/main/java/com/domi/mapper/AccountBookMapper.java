package com.domi.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.domi.model.AccountBook;

@Component
public interface AccountBookMapper {
	
	public int insert(AccountBook AccountBook);
	public int updateRemark(@Param("id") int id, @Param("remark") String remark);	
	public AccountBook queryById(int id);	
	public List<Map<String, Object>> queryAccountBooks(Map<String, Object> paramMap);
	public List<AccountBook> queryByUserId(Map<String, Object> paramMap);
	public int queryCountByUserId(int userId);
	
	public Integer deleteById(int id);	
	public float queryMoneyByUserId(int userId);	
}
