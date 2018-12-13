package com.domi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.domi.model.InviteUser;

@Component
public interface InviteUserMapper {
	int insert(InviteUser inviteUser);
	int updatePassword(@Param("inviteId") int inviteId, @Param("password") String password);
	int updateToken(@Param("account") String account, @Param("token") String token);
	InviteUser getByAccount(@Param("account") String account);
	InviteUser getByInviteId(@Param("inviteId") int inviteId);
	
	List<InviteUser> getAll();
	List<InviteUser> getList(@Param("start") int start, @Param("length") int length);
	int getSize();
}
