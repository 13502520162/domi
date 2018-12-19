package com.domi.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.domi.cache.VersionCache;
import com.domi.mapper.FeedbackMapper;
import com.domi.mapper.UserMapper;
import com.domi.mapper.VersionMapper;
import com.domi.model.Version;

@Service
public class AdminBasicService {
	
	@Autowired
	private FeedbackMapper feedbackMapper;
	@Autowired
	private VersionMapper versionMapper;
	@Autowired
	private UserMapper userMapper;
	
	public static  final int PAGESIZE = 20; 
	
/*	public Map<String, Object> getFeedBacks(int page, int pageSize, String phone, String beginDate, String endDate) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pageIndex", (page-1)*pageSize);
		paramMap.put("pageSize", pageSize);
		paramMap.put("phone", phone);
		paramMap.put("beginDate", beginDate);
		paramMap.put("endDate", endDate);
		List<Feedback> feedbacks = feedbackMapper.queryForPage(paramMap);		
		int size = feedbackMapper.querySize();
		data.put("list", feedbacks);
		data.put("size", size);
		return data;
	}*/
	
	public boolean validateVersion(Version version)
            throws UnsupportedEncodingException {
        if (	version.getCurrentVersion() == -1
                || version.getDescription().equals("")
                || version.getDownLoadUrl().equals("")) {
            return false;
        }
        return true;
    }

    public boolean isVersionTypeExist(int type) {
        VersionCache versionCache = VersionCache.getInstance(versionMapper);
        if (versionCache.getVersion(type) == null) {
            return false;
        }
        return true;
    }

    public boolean addVersion(Version version) {
        VersionCache versionCache = VersionCache.getInstance(versionMapper);
        if (versionMapper.insert(version) == 1) {
            versionCache.addVersion(version);
            return true;
        }
        return false;
    }

    public List<Version> viewVersion(int type) {
        List<Version> list = versionMapper.queryByType(type);
        return list;
    }

    
    public boolean updateVersion(Version version) {
        VersionCache versionCache = VersionCache.getInstance(versionMapper);
        if (versionMapper.update(version) == 1) {
            versionCache.updateVersion(version);
            return true;
        }
        return false;
    }

    
    public Version getVersion(int type) {
        VersionCache versionCache = VersionCache.getInstance(versionMapper);
        return versionCache.getVersion(type);
    }

	
	
	
}
