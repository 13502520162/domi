package com.domi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.domi.mapper.InviteUserMapper;
import com.domi.model.InviteUser;

@Service
public class WebAdminService {

	@Autowired
	private InviteUserMapper inviteUserMapper;
	
	public InviteUser getInviteUser(String account) {
		return inviteUserMapper.getByAccount(account);
	}
	
	public boolean updateLoginToken(String account, String token) {
		return inviteUserMapper.updateToken(account, token) == 1;
	}
}
