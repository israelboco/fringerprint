package com.presence.testpresence.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import com.presence.testpresence.model.repositories.EnrollInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class  ImageProcess {

	@Autowired
	EnrollInfoRepository enrollInfoRepository;

	 public static boolean base64toImage(String base64String, String picName) {
	     //   String savePath = InitializationCfg.getCfg("attachment.path");
	        Date now = new Date();
		/*
		 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); String
		 * subPath = dateFormat.format(now);
		 */
	        String imagePath = "C:/dynamicface/picture/";
	        String file = picName + ".jpg";
	        System.out.println("Trajectoire de l'image" + imagePath + file);
	        File file2 = new File(imagePath + file);
	        if (base64String == null) {
	            return false;
	        } else {
	            //Base64Encoder decoder = new Base64Encoder();

	            try {
	                if (!file2.exists()) {
	                    file2.getParentFile().mkdir();
	                    file2.createNewFile();
	                }

	                byte[] b = Base64.getDecoder().decode(base64String);

	                for(int i = 0; i < b.length; ++i) {
	                    if (b[i] < 0) {
	                        b[i] = (byte)(b[i] + 256);
	                    }
	                }

	                OutputStream out = new FileOutputStream(file2);
	                out.write(b);
	                out.flush();
	                out.close();
	                return true;
	            } catch (Exception e) {
	                e.printStackTrace();
	                return false;
	            }
	        }
	    }
	 
	  public static String multipartFileToBASE64(MultipartFile mFile) throws Exception{
	        //Base64Encoder bEncoder=new Base64Encoder();
	        String[] suffixArra=mFile.getOriginalFilename().split("\\.");
	        String preffix="data:image/jpg;base64,".replace("jpg", suffixArra[suffixArra.length - 1]);
	        String base64EncoderImg = preffix + Base64.getEncoder().encodeToString(mFile.getBytes()).replaceAll("[\\s*\t\n\r]", "");
	        return base64EncoderImg;
	    }
	  
	  /**
	   * 图片转base64字符串
	   * @param imgFile 图片路径
	   * @return
	   */
	  public static String imageToBase64Str(String imgFile) {
	   InputStream inputStream = null;
	   byte[] data = null;
	   try {
	    inputStream = new FileInputStream(imgFile);
	    data = new byte[inputStream.available()];
	    inputStream.read(data);
	    inputStream.close();
	   } catch (IOException e) {
	    e.printStackTrace();
	   }
	   // 加密
	//   BASE64Encoder encoder = new BASE64Encoder();
		/*
		 * String s=encoder.encode(data); String s2=s.replaceAll("[+]", "%2B");
		 */
	   return Base64.getEncoder().encodeToString(data);//org.apache.commons.codec.binary.Base64.encodeBase64String(data);
	  }
	  
	  
	  public static void main(String[] args) {
	//	EnrollInfo enrollInfo=
	}
}
