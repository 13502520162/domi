package com.domi.support.utils;

import java.util.LinkedList;
import java.util.List;

public class SubList{
	
	public static List<Object> subList(List<Object> objectList, int startIndex, int count){
		
		if(objectList == null){
			return new LinkedList<Object>();
		}
		
   		if(objectList.size() <= startIndex){
   			return new LinkedList<Object>();
   		}
   		
   		if(startIndex + count > objectList.size()){
   			return objectList.subList(startIndex, objectList.size());
   		}
   		
   		else{
   			return objectList.subList(startIndex, startIndex + count);
   		}		
	}
}
