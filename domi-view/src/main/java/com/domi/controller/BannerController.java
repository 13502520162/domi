package com.domi.controller;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.domi.service.util.Base64Img;
import com.domi.service.util.UploadUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.domi.model.Banner;
import com.domi.service.AdminService;
import com.domi.service.BannerService;
import com.domi.service.util.Results;
import com.domi.support.utils.CookieUtil;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;


@Controller
@RequestMapping("admin/banner")
public class BannerController {
	
	Logger log = Logger.getLogger(BannerController.class);
	
	@Autowired
	private BannerService bannerService;
	@Autowired
	private AdminService adminService;
	@Autowired
    private ServletContext ctx;
	
	/*@RequestMapping("getBanners")
    @ResponseBody
    public Map<String, Object> oldgetBanners(HttpServletRequest request) {
        try {
            if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
                return Results.results(2, "还没登录！", "getBanners", Collections.EMPTY_MAP);
            }
            return Results.results(0, "返回成功", "getBanners"
                    , bannerService.getBanners());
        } catch (Exception e) {
            log.error("exception in getVersion", e);
            return Results.results(10, "操作失效", "getBanners", Collections.EMPTY_MAP);
        }
    }*/
    @RequestMapping(value = "getBanners",method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    public @ResponseBody String getBanners(HttpServletRequest request) {
        JSONObject data = new JSONObject();
        JSONArray object = new JSONArray();
        try{
            if(CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))){
                data.put("state",0);
                data.put("msg","请登录");
                return data.toString();
            }
            List<Banner> banners = bannerService.getNewBanner();
            for (Banner banner : banners) {
                JSONObject jsonObject = bannerService.getJsonBanner(banner);
                object.add(jsonObject);
            }
        }catch (Exception e){
            data.put("status", 0);
            data.put("msg", "失败!");
            return data.toString();
        }
        data.put("status", 1);
        data.put("msg", "ok!");
        data.put("data",object);
        return data.toString();
    }
    @RequestMapping(value = "updateBanner",produces = "text/html;charset=UTF-8")
    public @ResponseBody String updateBanner(MultipartFile bgUrl,HttpServletRequest request,Banner banner,String path){
        JSONObject data = new JSONObject();
        if(bgUrl != null && bgUrl.getSize() >0 && bgUrl.getContentType().startsWith("image/")){
            try{
            if(CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))){
                data.put("state",0);
                data.put("msg","请登录");
                return data.toString();
            }
            String contextPath = ctx.getRealPath("/upload");
            String upload = UploadUtil.upload(bgUrl, contextPath);
            String newUpload = "E:/domi/out/artifacts/domi_war_exploded"+upload;
            InputStream inputStream = null;
            byte[] data1 = null;
            inputStream = new FileInputStream(newUpload);
            data1 = new byte[inputStream.available()];
            inputStream.read(data1);
            inputStream.close();
            BASE64Encoder encoder = new BASE64Encoder();
            String encode = encoder.encode(data1);
            banner.setBackground(upload);
            int i = bannerService.update(banner);
            if(i>0){
                data.put("status", 1);
                data.put("msg", "ok!");
                data.put("background","data:image/jpeg;base64,"+encode);
            }
            }catch (Exception e){
                data.put("status", 0);
                data.put("msg", "操作失败!");
            }
        }else if(bgUrl == null && bgUrl.isEmpty()){
            data.put("status", 1);
            data.put("msg", "ok!");
            data.put("background",path);
        }
        return data.toString();
    }
    /*@RequestMapping("updateBanner")
    @ResponseBody
    public Map<String, Object> updateBanner(MultipartFile bgUrl,HttpServletRequest request, Banner banner,String path) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<>();
        try {
            if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
                map = adminService.generateReturnMap(2, "还没登录！", "updateBanner", data);
                return map;
            }
            System.out.println(bgUrl);
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
*/
    @RequestMapping("addBanner")
    @ResponseBody
    public Map<String, Object> addBanner(HttpServletRequest request, Banner banner,MultipartFile bgUrl){
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<>();

        try {
            if (CookieUtil.getString(request, "loginKey") == null || !adminService.validateLoginKey(CookieUtil.getString(request, "loginKey"))) {
                map = adminService.generateReturnMap(2, "还没登录！", "addBanner", data);
                return map;
            }
            if(StringUtils.hasLength(banner.getBackground())){
                UploadUtil.deleteFile(ctx,banner.getBackground());
            }
            String contextPath = ctx.getRealPath("/upload");
            String upload = UploadUtil.upload(bgUrl, contextPath);
            String newUpload = "E:/domi/out/artifacts/domi_war_exploded"+upload;
            InputStream inputStream = null;
            byte[] data1 = null;
            inputStream = new FileInputStream(newUpload);
            data1 = new byte[inputStream.available()];
            inputStream.read(data1);
            inputStream.close();
            BASE64Encoder encoder = new BASE64Encoder();
            String encode = encoder.encode(data1);
            if(bannerService.addBanner(banner)){
                data.put("background","data:image/jpeg;base64,"+encode);
                map = adminService.generateReturnMap(0, "添加成功", "addBanner", data);
            }else {
            	map = adminService.generateReturnMap(3, "添加失败", "addBanner", data);
			}
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
            if(bannerService.delBanner(bannerId)){
            	map = adminService.generateReturnMap(0, "删除成功", "delBanner", data);
            }else {
            	map = adminService.generateReturnMap(3, "删除失败", "delBanner", data);
			}
            return map;
        } catch (Exception e) {
            log.error("exception in delBanner", e);
            map = adminService.generateReturnMap(10, "操作失效", "delBanner", data);
            return map;
        }
    }

}
