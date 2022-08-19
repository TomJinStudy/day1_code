package com.jin.Controller;

import com.jin.Controller.util.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("common")
public class commoncontroller {
    @Value("${project.upload}")
    private String base;
    @PostMapping("upload")
    public R<String> upload(MultipartFile file){
        String filename = file.getOriginalFilename();
        String substring = filename.substring(filename.length() - 4);
        String s = UUID.randomUUID().toString()+substring;
        File file1 = new File(base);
        if(!file1.exists()){
           file1.mkdir();
        }
        try {
            file.transferTo(new File(base+s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(s);
    }
    @GetMapping("download")
    public void download(String name ,HttpServletResponse respone){
        try {
            FileInputStream fileInputStream=new FileInputStream(new File(base+name));
            ServletOutputStream outputStream = respone.getOutputStream();
            respone.setContentType("image/jpeg");
            int len=0;
            byte[] bytes = new byte[1024];
            while ((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
