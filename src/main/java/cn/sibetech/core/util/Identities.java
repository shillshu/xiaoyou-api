package cn.sibetech.core.util;

import java.util.UUID;

/**
 * Created by sibetech on 2015-1-12.
 */
public class Identities {

    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    /**
     * 封装JDK自带的UUID, 通过Random数字生成
     * 生成32位UUID
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成八位UUID
     *
     * @return
     */
    public static String getUUID8() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuidStr = uuid();
        for (int i = 0; i < 8; i++) {
            String str = uuidStr.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    private static long lastID = genTime();

    /**
     * 生成时间毫秒16进制
     *
     * @return
     */
    public static synchronized String getTimeHex() {
        while (lastID == genTime()) {
        }
        lastID = genTime();
        return Long.toHexString(lastID);
    }

    private static long genTime() {
        return System.currentTimeMillis();
    }


    /**
     * 生成8位随机用户名
     *
     * @return
     */
    public static String getRandomUsername() {
        return getRandomUsername(8);
    }

    public static String getRandomUsername(int size) {
        StringBuffer shortBuffer = new StringBuffer();
        String uuidStr = uuid();
        for (int i = 0; i < size; i++) {
            String str = uuidStr.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }
}
