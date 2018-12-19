package com.domi.mapper;


import org.springframework.stereotype.Component;

import com.domi.model.SafeCall;

@Component
public interface SafeCallMapper {
	public int insert(SafeCall safeCall);
}
