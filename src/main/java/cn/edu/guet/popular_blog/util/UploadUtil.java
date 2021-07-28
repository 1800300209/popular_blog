package cn.edu.guet.popular_blog.util;

import cn.edu.guet.popular_blog.exception.OpcException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author pangjian
 * @ClassName UploadUtil
 * @Description 多文件上传工具类
 * @date 2021/7/6 17:26
 */

public class UploadUtil {

    public static String fileUpload(MultipartFile[] files, String fileRootPath) {
        String filePath = "";
        String result = "";
        File file = new File(fileRootPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 多文件上传
        for (int i = 0; i < files.length; i++) {
            // 上传简单文件名
            String originalFilename = files[i].getOriginalFilename();
            originalFilename = originalFilename.substring(originalFilename.lastIndexOf('.'));
            // 存储路径
            String fileName = new StringBuffer()
                    .append(UUID.randomUUID().toString().replace("-",""))
                    .append(originalFilename).toString();

            filePath = new StringBuilder(fileRootPath)
                    .append(fileName)
                    .toString();

            fileRootPath = fileRootPath.substring(fileRootPath.indexOf(":")+1);

            if (i != files.length - 1) {
                result = result + fileRootPath + fileName + ",";
            } else {
                result = result + fileRootPath + fileName;
            }
            try {
                // 保存文件
                files[i].transferTo(new File(filePath));
            } catch (IOException e) {
                throw new OpcException(e.getMessage());
            }
        }
        if (result.length() == 0) {
            throw new OpcException("上传失败！");
        }
        return result;
    }

}
