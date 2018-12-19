package com.domi.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.domi.support.identification.NumberUtil;
import com.domi.mapper.AccountBookMapper;
import com.domi.mapper.UserMapper;
import com.domi.model.AccountBook;
import com.domi.model.User;
import com.domi.service.StateConstant.AccountBookRepaymentType;
import com.domi.support.utils.MapUtils;

@Service
public class AccountBookService {
	
	private static final int PAGESIZE = 20;
	
	@Autowired
	private AccountBookMapper accountBookMapper;
	@Autowired
	private UserMapper userMapper;
	
	
	public boolean addAccountBook(AccountBook accountBook) {
		boolean result = false;		
		if(accountBook.getRepaymentType() == AccountBookRepaymentType.onePay){
			accountBook.setMoney(NumberUtil.formatNumber(accountBook.getRepaymentMoney()));
		}else {
			accountBook.setMoney(NumberUtil.formatNumber(accountBook.getRepaymentMoney()*accountBook.getLoanTerm()));
		}
		if(accountBookMapper.insert(accountBook) == 1){
			result = true;
		}
		return result;
	}
	
	public boolean updateRemark(int id, String remark) {
		boolean result = false;
		if(accountBookMapper.updateRemark(id, remark) == 1){
			result = true;
		}
		return result;
	}
	
	public Map<String, Object> getAccountBooks(int userId, int page) {
		Map<String, Object> data = new HashMap<>();
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("userId", userId);		
		paraMap.put("pageIndex", (page-1)*PAGESIZE);		
		paraMap.put("pageSize", PAGESIZE);		
		List<Map<String, Object>> list = accountBookMapper.queryAccountBooks(paraMap);
		float money = accountBookMapper.queryMoneyByUserId(userId);
		data.put("list", list);
		data.put("money", money);
		return data;
	}
	
	public Map<String, Object> getAccountBooksForAdmin(String info, int page) {
		Map<String, Object> data = new HashMap<>();
		Map<String, Object> paraMap = new HashMap<>();
		User user = userMapper.queryByInfo(info);
		if(user == null){
			return data;
		}else {
			paraMap.put("userId", user.getId());		
		}
		paraMap.put("pageIndex", (page-1)*PAGESIZE);		
		paraMap.put("pageSize", PAGESIZE);		
		List<AccountBook> list = accountBookMapper.queryByUserId(paraMap);
		int size = accountBookMapper.queryCountByUserId(user.getId());
		data.put("size", size);
		data.put("list", list);
		return data;
	}
	
	public Map<String, Object> getAccountBookInfo(int userId, int id, HttpServletRequest request) {
		
		Map<String, Object> data = new HashMap<>();
		AccountBook accountBook = accountBookMapper.queryById(id);
		if(accountBook.getUserId() != userId){
			return MapUtils.generateReturnMap(4, "您无权限获取他人账本信息", request.getRequestURI(), data);			
		}
		if(accountBook.getRepaymentType() == AccountBookRepaymentType.payForTerm){
			accountBook.setRepaymentDate(caculateMonthPaymentDate(accountBook.getRepaymentDay()));
		}
		data.put("accountBook", accountBook);
		return MapUtils.generateReturnMap(0, "获取账本信息成功", request.getRequestURI(), data);			
	}
	
	public Map<String, Object> delAccountBook(int userId, int id, HttpServletRequest request) {
		
		Map<String, Object> data = new HashMap<>();
		AccountBook accountBook = accountBookMapper.queryById(id);
		if(accountBook.getUserId() != userId){
			return MapUtils.generateReturnMap(4, "您无权限删除他人账本信息", request.getRequestURI(), data);			
		}
		if(accountBookMapper.deleteById(id) == 1){
			data = MapUtils.generateReturnMap(0, "删除账本信息成功", request.getRequestURI(), data);
		}else {
			data = MapUtils.generateReturnMap(0, "删除账本信息失败", request.getRequestURI(), data);
		}
		return data; 			
	}
	
	private String caculateMonthPaymentDate(int paymentDay){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		int today = calendar.get(Calendar.DAY_OF_MONTH);		
		if(today > paymentDay){
			calendar.add(Calendar.MONTH, 1);
			int maxDay = calendar.getActualMaximum(Calendar.DATE);
			calendar.set(Calendar.DAY_OF_MONTH, Math.min(paymentDay, maxDay));
		}else {
			int maxDay = calendar.getActualMaximum(Calendar.DATE);
			calendar.set(Calendar.DAY_OF_MONTH, Math.min(paymentDay, maxDay));
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(calendar.getTime());
	}
	
}
