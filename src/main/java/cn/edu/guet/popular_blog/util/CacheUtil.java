package cn.edu.guet.popular_blog.util;

import java.io.*;

/**
 * @author pangjian
 * @ClassName CacheUtil
 * @Description 序列化工具类
 * @date 2021/7/22 15:26
 */

public class CacheUtil {

    /**
     * @Description:将对象序列化
     * @Param obj:
     * @return byte[]
     * @date 2021/7/22 15:27
    */
    static public byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }


    /**
     * @Description: 将对象反序列化
     * @Param bytes:
     * @return java.lang.Object
     * @date 2021/7/22 15:28
    */
    static public  Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }
}
