package com.domi.controller;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.domi.model.Banner;
import com.domi.model.Config;
import com.domi.model.InviteUser;
import com.domi.model.Manager;
import com.domi.model.Version;
import com.domi.service.AccountBookService;
import com.domi.service.AdminBasicService;
import com.domi.service.AdminService;
import com.domi.service.AppService;
import com.domi.service.BannerService;
import com.domi.service.StateConstant.ManagerType;
import com.domi.service.util.Results;
import com.domi.support.utils.CookieInfo;
import com.domi.support.utils.CookieUtil;
import com.domi.support.utils.ExceptionParamLog;
import com.domi.support.utils.MD5;
import com.domi.support.utils.MapUtils;
import com.taobao.api.internal.util.StringUtils;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private AppService appService;
    @Autowired
    private BannerService bannerService;
    @Autowired
    private AdminBasicService adminBasicService;
    @Autowired
    private AccountBookService accountBookService;

    Logger log = Logger.getLogger(AdminController.class);

    @RequestMapping("login")
    @ResponseBody
    public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response, Manager manager) {

        Map<String, Object> map = null;
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            boolean result = adminService.validateManager(manager.getName(), manager.getPassword());
            if (!result) {
                map = adminService.generateReturnMap(1, "用户名不存在或者密码错误！", "login", data);
                return map;
            }
            String loginKey = adminService.updateLoginKey(manager.getName());
            //把managerName写入cookies
            CookieInfo info = new CookieInfo("loginKey", loginKey, 7 * 24 * 3600);
            CookieUtil.addCookie(response, info);
            String managerName = adminService.getNameByLoginKey(loginKey);
            Manager curManager = adminService.getManager(managerName);
            data.put("type", curManager.getType());
            map = adminService.generateReturnMap(0, "登录成功！", "login", data);

            request.getSession().getServletContext().setAttribute("managerName", manager.getName());
            request.getSession().setAttribute("managerName", manager.getName());
            request.setAttribute("managerName", manager.getName());
            return map;
        } catch (Exception e) {
            map = adminService.generateReturnMap(10, "操作失效！", "login", data);
            return map;
        }
    }

    @RequestMapping("logout")
    @ResponseBody
    public Map<String, Object> logout(HttpServletResponse response) {
        Map<String, Object> map = null;
        Map<String, Object> data = null;
        try {
            CookieUtil.deleteCookie(response, "loginKey");
            map = adminService.generateReturnMap(0, "退出！", "logout", data);
            return map;
        } catch (Exception e) {
            log.error("exception in logout", e);
            map = adminService.generateReturnMap(10, "操作失效！", "logout", data);
            return map;
        }
    }

    @RequestMapping("isLogin")
    @ResponseBody
    public Map<String, Object> isLogin(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        Map<String, Object> map = null;
        Map<String, Object> data = null;

        try {
            if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
                map = adminService.generateReturnMap(2, "还没登录！", "isLogin", data);
                return map;
            }
            String managerName = adminService.getNameByLoginKey(CookieUtil.getString(request, "loginKey"));
            Manager curManager = adminService.getManager(managerName);
            data = new HashMap<>();
            data.put("managerName", managerName);
            data.put("type", curManager.getType());
            data.put("adminId", curManager.getId());
            map = adminService.generateReturnMap(0, "已登录！", "isLogin", data);
            return map;
        } catch (Exception e) {
            log.error("exception in isLogin", e);
            map = adminService.generateReturnMap(10, "操作失效！", "isLogin", data);
            return map;
        }
    }
    

    //***************************************************** 管 理 员 管 理 *************************************************//*

    @RequestMapping("addManager")
    @ResponseBody
    public Map<String, Object> addManager(HttpServletRequest request, Manager manager) throws UnsupportedEncodingException {
        Map<String, Object> map = null;
        Map<String, Object> data = new HashMap<String, Object>();

        try {
            if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
                map = adminService.generateReturnMap(2, "还没登录！", "addManager", data);
                return map;
            }
            String managerName = adminService.getNameByLoginKey(CookieUtil.getString(request, "loginKey"));
            Manager curManager = adminService.getManager(managerName);
            if (curManager.getType() != ManagerType.superManager) {
                map = adminService.generateReturnMap(4, "该账号无权限添加管理员", "addManager", data);
            }

            if (adminService.addManager(manager)) {
                map = adminService.generateReturnMap(0, "添加管理员账号成功", "addManager", data);
            } else {
                map = adminService.generateReturnMap(3, "添加管理员账号失败", "addManager", data);
            }

            return map;
        } catch (Exception e) {
            log.error("exception in addManager", e);
            map = adminService.generateReturnMap(10, "操作失效！", "addManager", data);
            return map;
        }
    }


    @RequestMapping("delManager")
    @ResponseBody
    public Map<String, Object> delManager(HttpServletRequest request, Integer adminId) throws UnsupportedEncodingException {
        Map<String, Object> map = null;
        Map<String, Object> data = new HashMap<String, Object>();

        try {
            if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
                map = adminService.generateReturnMap(2, "还没登录！", "delManager", data);
                return map;
            }
            String managerName = adminService.getNameByLoginKey(CookieUtil.getString(request, "loginKey"));
            Manager curManager = adminService.getManager(managerName);
            if (curManager.getType() != 0) {
                map = adminService.generateReturnMap(4, "该账号无权限删除管理员", "delManager", data);
            }
            if (adminService.delManager(adminId)) {
                map = adminService.generateReturnMap(0, "删除管理员账号成功", "delManager", data);
            } else {
                map = adminService.generateReturnMap(3, "删除管理员账号失败", "delManager", data);
            }

            return map;
        } catch (Exception e) {
            log.error("exception in delManager", e);
            map = adminService.generateReturnMap(10, "操作失效！", "delManager", data);
            return map;
        }
    }


    @RequestMapping("updatePassword")
    @ResponseBody
    public Map<String, Object> updatePassword(HttpServletRequest request, Manager manager) throws UnsupportedEncodingException {
        Map<String, Object> map = null;
        Map<String, Object> data = new HashMap<String, Object>();

        try {
            if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
                map = adminService.generateReturnMap(2, "还没登录！", "updatePassWord", data);
                return map;
            }
            String managerName = adminService.getNameByLoginKey(CookieUtil.getString(request, "loginKey"));
            Manager curManager = adminService.getManager(managerName);
            if (curManager.getType() != 0) {
                map = adminService.generateReturnMap(4, "该账号无权限修改管理员密码", "updatePassword", data);
                return map;
            }
            if (adminService.updatePassWord(manager)) {
                map = adminService.generateReturnMap(0, "改密码成功", "updatePassword", data);
            } else {
                map = adminService.generateReturnMap(3, "原密码校验错误！", "updatePassword", data);
            }
            return map;
        } catch (Exception e) {
            log.error("exception in updatePassword", e);
            map = adminService.generateReturnMap(10, "操作失效！", "updatePassword", data);
            return map;
        }
    }


    @RequestMapping("getManagers")
    @ResponseBody
    public Map<String, Object> getManagers(HttpServletRequest request) {
        Map<String, Object> map = null;
        Map<String, Object> data = new HashMap<String, Object>();

        try {
            if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
                map = adminService.generateReturnMap(2, "还没登录！", "getManagers", data);
                return map;
            }
            String managerName = adminService.getNameByLoginKey(CookieUtil.getString(request, "loginKey"));
            Manager curManager = adminService.getManager(managerName);
            if (curManager.getType() != ManagerType.superManager) {
                map = adminService.generateReturnMap(4, "该账号无权限获取管理员列表", "addManager", data);
                return map;
            }

            data = adminService.getManagers();
            map = adminService.generateReturnMap(0, "获取成功", "getManagers", data);
            return map;
        } catch (Exception e) {
            log.error("exception in getManagers", e);
            map = adminService.generateReturnMap(10, "操作失效！", "getManagers", data);
            return map;
        }
    }
    
    //反馈信息
/*    @RequestMapping("getFeedBack")
    @ResponseBody
    public Map<String, Object> getFeedBack(HttpServletRequest request, Integer pageIndex, Integer pageSize) {
        Map<String, Object> map = null;
        Map<String, Object> data = null;

        try {
            if(pageIndex==null) pageIndex=1;
            if(pageSize==null) pageSize=20;
            if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
                map = adminService.generateReturnMap(2, "还没登录！", "getFeedBack", data);
                return map;
            }
            List<Feedback> list = adminService.getFeedbackList(pageIndex - 1, pageSize);
            int size = adminService.getFeedbackListSize();
            data = new HashMap<>();
            data.put("feedbackList", list);
            data.put("size", size);
            map = adminService.generateReturnMap(0, "返回反馈列表！", "getFeedBack", data);
            return map;
        } catch (Exception e) {
            log.error("exception in getFeedBack", e);
            map = adminService.generateReturnMap(10, "操作失效！", "getFeedBack", data);
            return map;
        }
    }
*/
    
	//反馈信息
    @RequestMapping("getFeedBack")
    @ResponseBody
    public Map<String, Object> getFeedBack(HttpServletRequest req, Integer pageIndex, Integer pageSize, 
    		String phone, String beginDate, String endDate) {
        Map<String, Object> map = null;
        Map<String, Object> data = null;

        try {
        	if(null == pageIndex) pageIndex=1;
        	if(null == pageSize) pageSize=20;
        	if(null == beginDate) beginDate=""; 
        	if(null == endDate) endDate = "";
        	if(null == phone) phone = "";
        	data = adminService.getFeedBacks(pageIndex, pageSize, phone, beginDate, endDate);
			map = MapUtils.generateReturnMap(0, "获取成功", req.getRequestURI(), data);
			return map;
		} catch (Exception e) {
			log.error("exception in " + req.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(req), e);
			map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, req.getRequestURI(), data);
			return map;
        }
    }
    
    @RequestMapping("getBanners")
    @ResponseBody
    public Map<String, Object> getBanners(HttpServletRequest request) {
        try {
            if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
                return Results.results(2, "还没登录！", "getBanners", Collections.EMPTY_MAP);
            }
            return Results.results(0, "返回成功", "getBanners"
                    , adminService.getBanners());
        } catch (Exception e) {
            log.error("exception in getVersion", e);
            return Results.results(10, "操作失效", "getBanners", Collections.EMPTY_MAP);
        }
    }

    @RequestMapping("updateBanner")
    @ResponseBody
    public Map<String, Object> updateBanner(HttpServletRequest request, Banner banner) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<>();
        try {
            if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
                map = adminService.generateReturnMap(2, "还没登录！", "updateBanner", data);
                return map;
            }
            if (bannerService.updateBanner(banner)) {
                map = adminService.generateReturnMap(0, "更新成功", "updateBanner", data);
            } else {
                map = adminService.generateReturnMap(3, "更新失败", "updateBanner", data);
            }
            return map;
        } catch (Exception e) {
            log.error("exception in updateBanner", e);
            map = adminService.generateReturnMap(10, "操作失效", "updateBanner", data);
            return map;
        }
    }

    @RequestMapping("addBanner")
    @ResponseBody
    public Map<String, Object> addBanner(HttpServletRequest request, Banner banner) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<>();
        try {
            if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
                map = adminService.generateReturnMap(2, "还没登录！", "addBanner", data);
                return map;
            }
            bannerService.addBanner(banner);
            map = adminService.generateReturnMap(0, "返回成功", "addBanner", data);
            return map;
        } catch (Exception e) {
            log.error("exception in addBanner", e);
            map = adminService.generateReturnMap(10, "操作失效", "addBanner", data);
            return map;
        }
    }

    @RequestMapping("delBanner")
    @ResponseBody
    public Map<String, Object> delBanner(HttpServletRequest request, Integer bannerId) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<>();
        try {
            if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
                map = adminService.generateReturnMap(2, "还没登录！", "delBanner", data);
                return map;
            }
            bannerService.delBanner(bannerId);
            map = adminService.generateReturnMap(0, "返回成功", "delBanner", data);
            return map;
        } catch (Exception e) {
            log.error("exception in delBanner", e);
            map = adminService.generateReturnMap(10, "操作失效", "delBanner", data);
            return map;
        }
    }

    
    @RequestMapping("getAppData")
    @ResponseBody
    public Map<String, Object> getAppData(HttpServletRequest request, String beginDate, String endDate) {
    	 Map<String, Object> map = new HashMap<String, Object>();
         Map<String, Object> data = new HashMap<>();
    	try {
    		 if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
                 map = adminService.generateReturnMap(2, "还没登录！", request.getRequestURI(), data);
                 return map;
             }
    		 data = adminService.getAppData(beginDate, endDate);
    		 map = adminService.generateReturnMap(0, "返回数据成功", request.getRequestURI(), data);
    		 return map;
    		 
    	} catch (Exception e) {
    		log.error("exception in "+request.getRequestURI(), e);
    		return Results.results(10, "操作失效", "getBanners", Collections.EMPTY_MAP);
    	}
    }
      
    @RequestMapping("getUserData")
    @ResponseBody
    public Map<String, Object> getUserData(HttpServletRequest request, String beginDate, String endDate,Integer hasSubmitInfo, String info, Integer page) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	Map<String, Object> data = new HashMap<>();
    	try {
    		if(null==info) info = "";
    		if(null==page) page = 1;
    		if(null==hasSubmitInfo) hasSubmitInfo= -1;
    		
    		if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
    			map = adminService.generateReturnMap(2, "还没登录！", request.getRequestURI(), data);
    			return map;
    		}
    		data = adminService.getUserData(beginDate, endDate, hasSubmitInfo, info);
    		map = adminService.generateReturnMap(0, "返回数据成功", request.getRequestURI(), data);
    		return map;
    		
    	} catch (Exception e) {
    		log.error("exception in "+request.getRequestURI(), e);
    		return Results.results(10, "操作失效", "getBanners", Collections.EMPTY_MAP);
    	}
    }
    

//    @RequestMapping("updateInviteUser")
//    @ResponseBody
//    public Map<String, Object> updateInviteUser(HttpServletRequest request, Integer userId, String account, String password) {
//    	Map<String, Object> map = new HashMap<String, Object>();
//    	Map<String, Object> data = new HashMap<>();
//    	try {
//    		adminService.updateInviteUser(userId, account, password);
//    		map = adminService.generateReturnMap(0, "成功", request.getRequestURI(), data);
//    		return map;
//    		
//    	} catch (Exception e) {
//    		log.error("exception in "+request.getRequestURI(), e);
//    		return Results.results(10, "操作失效", "getBanners", Collections.EMPTY_MAP);
//    	}
//    }
    
	   @RequestMapping("viewVersion")
	   @ResponseBody
	   public Map<String, Object> viewVersion(HttpServletRequest req, Integer type) {
	       Map<String, Object> map = new HashMap<String, Object>();
	       Map<String, Object> data = new HashMap<>();
	       try {

	    	   if (type == null) {
	    		   map = MapUtils.generateReturnMap(4, "参数异常,需要传类型", "viewVersion", data);
	    		   return map;
	    	   }
	           List<Version> list = adminBasicService.viewVersion(type);
	           if (list == null || list.size() == 0) {
	        	   map = MapUtils.generateReturnMap(3, "列表为空", "viewVersion", data);
	        	   return map;
	           }

	           data.put("list", list);
	           map = MapUtils.generateReturnMap(0, "返回列表", "viewVersion", data);
	           return map;
	       } catch (Exception e) {
	    	   log.error("exception in " + req.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(req), e);
				map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, req.getRequestURI(), data);
				return map;
	       }
	   }

	   @RequestMapping("updateVersion")
	   @ResponseBody
	   public Map<String, Object> updateVersion(HttpServletRequest req, Version version) {
	       Map<String, Object> map = new HashMap<String, Object>();
	       Map<String, Object> data = new HashMap<>();
	       try {
	           if (!adminBasicService.validateVersion(version)) {
	               map = MapUtils.generateReturnMap(1, "有些项目为空", "updateVersion", data);
	               return map;
	           }


	           if (adminBasicService.updateVersion(version)) {
	               map = MapUtils.generateReturnMap(0, "修改成功", "updateVersion", data);
	               return map;
	           }

	           map = MapUtils.generateReturnMap(3, "修改失败", "updateVersion", data);
	           return map;
	       } catch (Exception e) {
	    	   	log.error("exception in " + req.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(req), e);
				map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, req.getRequestURI(), data);
				return map;
	       }
	   }

	   @RequestMapping("addVersion")
	   @ResponseBody
	   public Map<String, Object> addVersion(HttpServletRequest req, Version version) {
		   Map<String, Object> map = new HashMap<String, Object>();
		   Map<String, Object> data = new HashMap<>();
		   try {
			   if (!adminBasicService.validateVersion(version)) {
				   map = MapUtils.generateReturnMap(1, "有些项目为空", "updateVersion", data);
				   return map;
			   }
			   
			   
			   if (adminBasicService.addVersion(version)) {
				   map = MapUtils.generateReturnMap(0, "添加成功", "updateVersion", data);
				   return map;
			   }
			   
			   map = MapUtils.generateReturnMap(3, "添加失败", "updateVersion", data);
			   return map;
		   } catch (Exception e) {
			   log.error("exception in " + req.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(req), e);
			   map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, req.getRequestURI(), data);
			   return map;
		   }
	   }
	   @RequestMapping("getVersion")
	   @ResponseBody
	   public Map<String, Object> getVersion(HttpServletRequest req, int type) {
	       Map<String, Object> map = new HashMap<String, Object>();
	       Map<String, Object> data = new HashMap<>();
	       try {

	           Version version = adminBasicService.getVersion(type);
	           if (version == null) {
	               map = MapUtils.generateReturnMap(3, "返回为空", "getVersion", data);
	               return map;
	           }
	           data.put("version", version);
	           map = MapUtils.generateReturnMap(0, "返回成功", "getVersion", data);
	           return map;
	       } catch (Exception e) {
	    	   log.error("exception in " + req.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(req), e);
				map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, req.getRequestURI(), data);
				return map;
	       }
	   }
	
	   
	   @RequestMapping("getUptoken")
		@ResponseBody
		public Map<String, Object> getUptoken(HttpServletRequest request) {
			Map<String, Object> map = new HashMap<String, Object>();
			Map<String, Object> data = new HashMap<>();
			try{		
				String uptoken = adminService.getUptoken();
				map.put("uptoken", uptoken);
				return map;
			} catch (Exception e) {
				  log.error("exception in " + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request), e);
					map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, request.getRequestURI(), data);
					return map;
			}
		}
    
	   
		@RequestMapping("getAccountBooksForAdmin")
		@ResponseBody
		public Map<String, Object> getAccountBooksForAdmin(HttpServletRequest request, String info, Integer page) {
			
			Map<String, Object> map = null;
			Map<String, Object> data = new HashMap<>();
			try {	
				if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
	    			map = adminService.generateReturnMap(2, "还没登录！", request.getRequestURI(), data);
	    			return map;
	    		}
				if(null == page) page=1;
				data = accountBookService.getAccountBooksForAdmin(info, page);			
				map = MapUtils.generateReturnMap(0, "成功", request.getRequestURI(), data);
				return map;
			} catch (Exception e) {
				log.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
				map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, request.getRequestURI(), data);
				return map;
			}
		}
    
		@RequestMapping("getConfig")
		@ResponseBody
		public Map<String, Object> getConfig(HttpServletRequest request) {
			
			Map<String, Object> map = null;
			Map<String, Object> data = new HashMap<>();
			try {	
				if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
					map = adminService.generateReturnMap(2, "还没登录！", request.getRequestURI(), data);
					return map;
				}
				data = adminService.getConfig();			
				map = MapUtils.generateReturnMap(0, "成功", request.getRequestURI(), data);
				return map;
			} catch (Exception e) {
				log.error("exception in:" + request.getRequestURI() + " requestParam: " + ExceptionParamLog.logExceptionParam(request),e);
				map = MapUtils.generateReturnMap(MapUtils.ERROR_NUM, MapUtils.ERROR_INFO, request.getRequestURI(), data);
				return map;
			}
		}
		
		
		@RequestMapping("updateConfig")
	    @ResponseBody
	    public Map<String, Object> updateConfig(HttpServletRequest request, Config config, String password) {
	        Map<String, Object> map = null;
	        Map<String, Object> data = null;

	        if( config.getBank1GotoUrl()==null || config.getBank1ImgUrl()==null || config.getBank2GotoUrl()==null
	        		||config.getBank2ImgUrl()==null  || config.getMoreBankUrl()==null || config.getServicePhone()==null || config.getServiceQQ()==null
	        		||config.getServiceWx()==null){        	
	        	map = adminService.generateReturnMap(4, "参数异常", request.getRequestURI(), data);
	        	return map;
	        }
	        
	        try {
	            if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
	                map = adminService.generateReturnMap(2, "还没登录！", request.getRequestURI(), data);
	                return map;
	            }
	            if (adminService.updateConfig(config)) {
	                map = adminService.generateReturnMap(0, "更新配置信息成功", request.getRequestURI(), data);
	            } else {
	                map = adminService.generateReturnMap(3, "更新配置信息失败", request.getRequestURI(), data);
	            }
	            return map;
	        } catch (Exception e) {
	            log.error(request.getRequestURI(), e);
	            map = adminService.generateReturnMap(10, "操作失效！", request.getRequestURI(), data);
	            return map;
	        }
	    }
		
		
		@RequestMapping("getInviteUserList")
	    @ResponseBody
	    public Map<String, Object> getInviteUserList(HttpServletRequest request, int page) {
	        Map<String, Object> map = null;
	        Map<String, Object> data = null;
	        
	        try {
	        	int start = (page - 1) * 10;
	        	data = adminService.getInviteUserList(start, 10);
	        	map = MapUtils.generateReturnMap(0, "", "", data);
	            return map;
	        } catch (Exception e) {
	            log.error(request.getRequestURI(), e);
	            map = adminService.generateReturnMap(10, "操作失效！", request.getRequestURI(), data);
	            return map;
	        }
	    }
		
		@RequestMapping("addInviteUser")
	    @ResponseBody
	    public Map<String, Object> addInviteUser(HttpServletRequest request, String account, String password) {
	        Map<String, Object> map = null;
	        Map<String, Object> data = null;
	        
	        try {
	        	if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
	        		return MapUtils.generateReturnMap(1, "参数错误", "", data);
	        	}
	        	if (adminService.getInviteUser(account) != null) {
	        		return MapUtils.generateReturnMap(3, "帐号已存在", "", data);
	        	}
	        	InviteUser inviteUser = new InviteUser();
	        	inviteUser.setAccount(account);
	        	inviteUser.setPassword(MD5.MD5Encrypt(password));
	        	inviteUser.setAddtime(new Date());
	        	adminService.addInviteUser(inviteUser);
	        	map = MapUtils.generateReturnMap(0, "成功", "", data);
	            return map;
	        } catch (Exception e) {
	            log.error(request.getRequestURI(), e);
	            map = adminService.generateReturnMap(10, "操作失效！", request.getRequestURI(), data);
	            return map;
	        }
	    }
		

    //静态页面跳转
    @RequestMapping("loginFTL")
    public String login() {
        return "admin/login";
    }

    @RequestMapping("")
    public String index() {
         return "admin/index";
    }

    //反馈与建议
    @RequestMapping("feedback")
    public String feedback() {
        return "admin/feedback";
    }

    

}
