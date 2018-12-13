package com.domi.support.utils.test;

import junit.framework.Assert;

import org.junit.Test;

import com.domi.support.utils.AddressMatch;

public class AddressMatchTest {
	@Test
	public void getAddressTest() {
		String address1 = AddressMatch.getAddress("广工");
		Assert.assertEquals("广工", address1);
		
		String address2 = AddressMatch.getAddress("广工站");
		Assert.assertEquals("广工", address2);
		
		String address3 = AddressMatch.getAddress("中山大学");
		Assert.assertEquals("中大", address3);
		
		String address4 = AddressMatch.getAddress("中山大学北校区");
		Assert.assertEquals("中大", address4);
		
		String address5 = AddressMatch.getAddress("什么垃圾学校？");
		Assert.assertEquals("其他", address5);
		
		String address6 = AddressMatch.getAddress("");
	}
}
