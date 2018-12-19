package com.domi.mapper.loanmarket;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.domi.model.SafeCall;
import com.domi.model.loanmarket.AppLoanPlatformDto;
import com.domi.model.loanmarket.LoanPlatform;
import com.domi.model.loanmarket.LoanPlatformData;

@Component
public interface LoanPlatformMapper {
	public int insert(LoanPlatform loanPlatform);
	public int insertSafeCall(SafeCall safeCall);
	public int update(LoanPlatform loanPlatform);
	public LoanPlatform queryById(int id);
	public List<LoanPlatform> queryListByParam(Map<String, Object> param);
	public int querySizeByParam(Map<String, Object> param);
	public List<LoanPlatform> queryToBeOnlineList();
	public int updateState(LoanPlatform loanPlatform);
	
	//app
	public List<AppLoanPlatformDto> queryListByPlatformState(Map<String, Object> param);
	public List<AppLoanPlatformDto> queryListByPlatformStateForTwo(Map<String, Object> param);
	public List<LoanPlatform> queryListByAppParam(Map<String, Object> param);
	public List<String> queryAllNames();
	
	public List<LoanPlatformData> queryPlatformData(Map<String, Object> param);
}
