package com.domi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.domi.model.CommonKeyValue;
import com.domi.model.Manager;

@Component
public interface ManagerMapper {
	public int insert(Manager manager);
	public int update(Manager manager);
	public List<Manager> queryAll();
	public int delManager(int id);
	
	public int updatePassword(Manager manager);
	public List<Manager> getManagerListBytype(@Param("type") Integer type);
	public List<CommonKeyValue> getIdAndNameBytype(@Param("type") Integer type);
	public Manager getManagerByName(String string);
}
