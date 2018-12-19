package com.domi.service.util;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
/**
 * 按顺序读取properties文件
 * Properties是继承自 Hashtable，无序
 * 覆盖原来的 put/keys，keySet，stringPropertyNames，使用LinkedHashSet来保存它的所有 key
 * @author chenhuanshuo
 *
 * @Date 2017年5月22日
 */
public class OrderedProperties extends Properties {  
	   
    private static final long serialVersionUID = -4627607243846121965L;  
       
    private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();  
   
    public Enumeration<Object> keys() {  
        return Collections.<Object> enumeration(keys);  
    }  
   
    public Object put(Object key, Object value) {  
        keys.add(key);  
        return super.put(key, value);  
    }  
   
    public Set<Object> keySet() {  
        return keys;  
    }  
   
    public Set<String> stringPropertyNames() {  
        Set<String> set = new LinkedHashSet<String>();  
   
        for (Object key : this.keys) {  
            set.add((String) key);  
        }  
   
        return set;  
    }  
}