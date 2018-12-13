package com.domi.mapper.loanmarket;

import java.util.List;

import org.springframework.stereotype.Component;

import com.domi.model.loanmarket.AppLoanPlatformDto;
import com.domi.model.loanmarket.PlatformInfo;

@Component
public interface PlatformInfoMapper {
	public int insert(PlatformInfo platformInfo);
	public int update(PlatformInfo platformInfo);
	public AppLoanPlatformDto queryByLoanPlatformIId(int loanPlatformId);
	public List<PlatformInfo> queryAll();
}
