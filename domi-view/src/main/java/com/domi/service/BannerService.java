package com.domi.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


import com.alibaba.fastjson.JSONObject;
import com.domi.service.util.Base64Img;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.domi.mapper.BannerMapper;
import com.domi.model.Banner;
import com.domi.service.util.Results;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletContext;

@Service
public class BannerService {
	
	private static final Logger logger = Logger.getLogger(BannerService.class);
	
	@Autowired
	private BannerMapper bannerMapper;
	
	
    public Map<String, Object> getBanners() {
      List<Banner> list = bannerMapper.queryListByParam(2);
        /*for (Banner banner : list) {
            String oldBackground = banner.getBackground();
            String background  = "http://localhost"+oldBackground;
            String base64url = Base64Img.GetImageStrFromUrl(background);

        }*/
      return Results.of()
              .put("list", list).toMap();
  }
   

    public boolean addBanner(Banner banner) {
        /*//删除就图片
        if(pic != null && pic.getSize() >0 && pic.getContentType().startsWith("image/")){
            if(StringUtils.hasLength(banner.getBackground())){
                UploadUtil.deleteFile(ctx,banner.getBackground());
            }
            String imagePath = UploadUtil.upload(pic, ctx.getRealPath("/upload"));
            banner.setBackground(imagePath);
        }
        Banner brand = bannerMapper.query(banner.getId());
        brand.setBackground(brand.getBackground());*/
        boolean result = false;
        if(bannerMapper.insert(banner) == 1){
        	result = true;
        }
        return result;
    }


    public boolean updateBanner(Banner banner) {
        boolean result = false;
        Banner oldBanner = bannerMapper.query(banner.getId());
        if (oldBanner != null && (bannerMapper.update(banner) == 1)) {
            oldBanner.setOrderNo(banner.getOrderNo());
            oldBanner.setImgUrl(banner.getImgUrl());
            oldBanner.setUrl(banner.getUrl());
            result = true;
        }
        return result;
    }


    public boolean delBanner(int bannerId) {
        boolean result = false;
        if(bannerMapper.delete(bannerId) == 1){
        	result = true;
        } 
        return result;
    }

    public List<Banner> getNewBanner() {
        List<Banner>banners = bannerMapper.getNewBanner();
        return  banners;
    }

    public JSONObject getJsonBanner(Banner banner) {
        JSONObject jsonObj = new JSONObject();
        String newUpload = "E:/domi/out/artifacts/domi_war_exploded"+banner.getBackground();
        try{
            InputStream inputStream = null;
            byte[] data1 = null;
            inputStream = new FileInputStream(newUpload);
            data1 = new byte[inputStream.available()];
            inputStream.read(data1);
            inputStream.close();
            BASE64Encoder encoder = new BASE64Encoder();
            String encode = encoder.encode(data1);
            String newCode = "data:image/jpeg;base64,"+encode;
            jsonObj.put("background",newCode);
        }catch (Exception e){
            jsonObj.put("state",0);
            jsonObj.put("type","失败");
            return jsonObj;
        }
        jsonObj.put("id",banner.getId());
        jsonObj.put("type",banner.getType());
        jsonObj.put("url",banner.getUrl());
        jsonObj.put("orderNo",banner.getOrderNo());
        jsonObj.put("imgUrl",banner.getImgUrl());
        return jsonObj;
    }

    public int update(Banner banner) {
        int update = bannerMapper.update(banner);
        return update;
    }
}
