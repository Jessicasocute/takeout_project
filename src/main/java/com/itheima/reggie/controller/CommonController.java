package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
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
import java.io.IOException;
import java.util.UUID;

/**
 * @Description:
 * @Author: wx
 * @Date: 2023/7/18 17:54
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        log.info("file:{}",file);

        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName  = UUID.randomUUID().toString()+suffix;

        //创建一个目录对象
        File dir = new File(basePath);
        //判断当前目录是否存在
        if(!dir.exists()){
            dir.mkdirs();
        }

        //将临时文件转存到指定位置
        file.transferTo(new File(basePath+fileName));

        return R.success(fileName);
    }

    /**
     * 文件下载
     * @return
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {

        log.info("图片name:{}",name);

        //输入流，通过输入流读取文件内容
        FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
        //输出流，通过输出流将文件写回浏览器
        ServletOutputStream outputStream = response.getOutputStream();

        response.setContentType("image/jpeg");


        int length = 0;
        byte[] bytes = new byte[1024];

        while ((length=fileInputStream.read(bytes))!=-1)
        {
            outputStream.write(bytes,0,length);
            outputStream.flush();
        }
        //关闭资源
        fileInputStream.close();
        outputStream.close();
    }
}
