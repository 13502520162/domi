package com.domi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.domi.model.InviteData;

@Component
public interface InviteDataMapper {
	List<InviteData> getList(@Param("inviteId") int inviteId, @Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("start") int start, @Param("length") int length);
	int getSize(@Param("inviteId") int inviteId, @Param("beginDate") String beginDate, @Param("endDate") String endDate);
	
	int updateInviteData(InviteData inviteData);
	int insertMany(@Param("list") List<InviteData> list);
}
