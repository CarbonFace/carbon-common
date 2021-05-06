package cn.carbonface.carboncommon.tools;

import org.springframework.util.DigestUtils;

import java.util.Random;

/**
 * Classname: EncryptUtil
 * Description: EncryptUtil for encrypt strings
 * @author CarbonFace <553127022@qq.com>
 * Date: 2021/3/19 12:02
 * @version V1.0
 */
public class EncryptUtil {
    /**
     * Description:: md5Encrypt
     * encoding strings without salt input
     * @return java.lang.String
     * @author: CarbonFace  <553127022@qq.com>
     * Date:: 2021/3/25 16:16
     * @version: 1.0
     */
    public static String md5Encrypt(String encryptString){
        String encodeString = DigestUtils.md5DigestAsHex(encryptString.getBytes());
        return encodeString;
    }

    /**
     * Description:: laySalt
     * generate salt with strings input which is randomly cut by the length of encryptString
     * @return java.lang.String
     * @author: CarbonFace  <553127022@qq.com>
     * Date:: 2021/3/25 16:15
     * @version: 1.0
     */
    public static String laySalt(String saltString){
        String md5EncryptString = md5Encrypt(saltString);
        int length = md5EncryptString.length();
        Random random = new Random(System.currentTimeMillis());
        int half = length / 2;
        int head = random.nextInt(half);
        int tail = head + half;
        String salt = md5EncryptString.substring(head,tail);
        return salt;
    }

    /**
     * Description:: md5EncryptWithSalt
     * encode strings with salt input
     * @param encryptString
     * @param salt
     * @return java.lang.String
     * @author: CarbonFace <553127022@qq.com>
     * Date:: 2021/3/16 13:56
     * @version: 1.0
     */
    public static String md5EncryptWithSalt(String encryptString, String salt){
        String encryptStr = encryptString + "." +salt;
        String encodeString = DigestUtils.md5DigestAsHex(encryptStr.getBytes());
        return encodeString;
    }


}
