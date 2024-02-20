package cn.sibetech.core.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.security.MessageDigest;

public class FileUtils {
    public final static String RESOURCE_STORAGE_PATHNAME = "files";
    public static final String CONTENT_TYPE_ZIP = "application/zip";
    public static final String CONTENT_TYPE_PDF = "application/pdf";
    public static final String CONTENT_TYPE_ZIP_COMPRESSED = "application/x-zip-compressed";
    /**
     * 获得合法的目录，避免目录遍历漏洞，比如说传入/../../这种目录结构会过滤掉
     *
     * @param folderName
     * @return
     */
    public static String getValidFolderName(String folderName) {
        String token = "/..";
        if (StringUtils.contains(folderName, token)) {
            folderName = StringUtils.replace(folderName, token, "/NOT-SUPPORT");
        }
        token = "../";
        if (StringUtils.contains(folderName, token)) {
            folderName = StringUtils.replace(folderName, token, "/NOT-SUPPORT");
        }
        token = "\\..";
        if (StringUtils.contains(folderName, token)) {
            folderName = StringUtils.replace(folderName, token, "\\NOT-SUPPORT");
        }
        token = "..\\";
        if (StringUtils.contains(folderName, token)) {
            folderName = StringUtils.replace(folderName, token, "NOT-SUPPORT\\");
        }
        if ("..".equals(folderName)) {
            folderName = "NOT-SUPPORT";
        }
        return folderName;
    }

    /**
     * 计算文件的md5
     * @param file
     * @return
     * @throws Exception
     */
    public static byte[] createChecksum(File file) throws Exception {
        int BUFFER_SIZE = 1024 * 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int count = 0;
        RandomAccessFile randomAccessFile = null;
        Long fileSize = file.length();
        long position = 0;

        if (fileSize >= 1024 * 1024 * 32) {//32M
            MessageDigest complete = MessageDigest.getInstance("MD5");

            do {//取文件前6M
                randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(position);
                count = randomAccessFile.read(buffer);

                if (randomAccessFile != null) {
                    randomAccessFile.close();
                    randomAccessFile = null;
                }
                if (count > 0) {
                    complete.update(buffer, 0, count);
                }
                position += count;
            } while (position <= 5242880); // 1024*1024*5M

            position=fileSize-BUFFER_SIZE;
            do {//取文件后6M
                randomAccessFile = new RandomAccessFile(file, "rw");

                randomAccessFile.seek(position);
                count = randomAccessFile.read(buffer);
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                    randomAccessFile = null;
                }
                if (count > 0) {
                    complete.update(buffer, 0, count);
                }
                position -= count;
            } while (position >= fileSize-5242880); // 1024*1024*5M

            complete.update(fileSize.byteValue());
            return complete.digest();
        } else {
            InputStream fis = new FileInputStream(file);
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead;

            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            fis.close();
            return complete.digest();
        }

    }
    public static byte[] createChecksum(InputStream  is)  throws Exception{
        int BUFFER_SIZE = 1024 * 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int count = 0;
        Long fileSize = (long) is.available();
        long position = 0;
        if(fileSize >=  32*1024*1024) {
            MessageDigest complete = MessageDigest.getInstance("MD5");
            do {//取文件前6M
                count = is.read(buffer);
                if (count > 0) {
                    complete.update(buffer, 0, count);
                } else {
                    break;
                }
                position += count;
            } while (position <= 5242880); // 1024*1024*5M
            long total = (is.available() - 12582912)/1048576;
            long first = total /2;
            long last = total - first;
            is.skip(first * 1048576 );
            position = 0;
            do { //中间6M
                count = is.read(buffer);
                if (count > 0) {
                    complete.update(buffer, 0, count);
                }else {
                    break;
                }
                if (count > 0) {
                    complete.update(buffer, 0, count);
                }
                position += count;
            } while (position <= 5242880);
            is.skip(last * 1048576);
            position = 0;
            do {//取文件后6M以后数据
                count = is.read(buffer);
                if (count > 0) {
                    complete.update(buffer, 0, count);
                }else {
                    break;
                }
                position =+ count;
            } while (position <= 5242880); // 1024*1024*5M
            complete.update(fileSize.byteValue());
            return complete.digest();
        } else {
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead;
            do {
                numRead = is.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while(numRead != -1);
            return complete.digest();
        }
    }
    public static String getFileMD5Checksum(File file) throws Exception {
        byte[] b = createChecksum(file);
        return md5(b);
    }

    public static String getFileMD5Checksum(InputStream  inputStream) throws Exception {
        byte[] b = createChecksum(inputStream);
        return md5(b);
    }
    private static String md5(byte []b) {
        return new String(Hex.encodeHexString(b));
    }

//    public static String getHashFilepath(String md5) {
//        if (md5 == null || md5.length() < 4) {
//            md5 = CryptUtils.md5(Identities.uuid());
//        }
//        return md5;
//    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                temp.delete();
                flag = true;
            }

        }
        return flag;
    }
    public static String getHashFilepath(String md5) {
        if (md5 == null || md5.length() < 4) {
            md5 = CryptUtils.md5(Identities.uuid());
        }
        StringBuffer path = new StringBuffer();
        path.append("/").append(RESOURCE_STORAGE_PATHNAME).append("/").append(StringUtils.substring(md5, 0, 2))
                .append("/").append(StringUtils.substring(md5, md5.length()-2));
        return path.toString();
    }
    public static String generateMD5(InputStream InputStream) {
        String md5 = null;
        try {
            md5 = FileUtils.getFileMD5Checksum(InputStream);
            return md5;
        } catch (Exception e) {
            return Identities.getUUID8();
        } finally {
            IOUtils.closeQuietly(InputStream);
        }
    }
//    public static List<Map<String, Object>> loadFileList(String jsonStr, HttpServletRequest request) {
//        List<Map<String,Object>> result = new ArrayList<>();
//        if (StringUtils.isEmpty(jsonStr)) {
//            return result;
//        }
//        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
//        for (int i=0;i<jsonArray.size();i++) {
//            Map<String,Object> map = new HashMap<>(2);
//            String pathStr = jsonArray.getString(i);
//            JSONObject obj = JSONObject.parseObject(pathStr);
//            map.put("name",obj.getString("fileName"));
//            map.put("url", URIUtil.getWebPath(request)+"/resource/downloadFile?path="+obj.getString("path"));
//            result.add(map);
//        }
//        return result;
//    }
}
