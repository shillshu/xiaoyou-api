package cn.sibetech.core.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 将文件夹下面的文件
 * 打包成zip压缩文件
 * <p>
 * Created by sibe2 on 2017/4/5.
 */
public class ZipUtil {
    private ZipUtil() {
    }

    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下 flag 是否删除源文件 true 删除
     *
     * @param sourceFilePath :待压缩的文件路径
     * @param zipFilePath    :压缩后存放路径
     * @param fileName       :压缩后文件的名称
     * @return
     */
    public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName, boolean delFlag) {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis;
        BufferedInputStream bis = null;
        FileOutputStream fos;
        ZipOutputStream zos = null;

        if (sourceFile.exists() == false) {
            System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在.");
        } else {
            try {
                File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
                if (zipFile.exists()) {
                    System.out.println(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件.");
                } else {
                    File[] sourceFiles = sourceFile.listFiles();
                    if (null == sourceFiles || sourceFiles.length < 1) {
                        System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
                    } else {
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[1024 * 10];
                        for (int i = 0; i < sourceFiles.length; i++) {
                            //创建ZIP实体，并添加进压缩包
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                            zos.putNextEntry(zipEntry);
                            //读取待压缩的文件并写进压缩包里
                            fis = new FileInputStream(sourceFiles[i]);
                            bis = new BufferedInputStream(fis, 1024 * 10);
                            int read = 0;
                            while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                                zos.write(bufs, 0, read);
                            }
                            fis.close();
                            zos.closeEntry();
                            //当文件放进压缩包后，删除文件
                            if(delFlag) {
                                sourceFiles[i].delete();
                            }
                        }
                        zos.closeEntry();
                        flag = true;
                    }
                    //待压缩目录为空或清空压缩目录后，删除目录
                    if(delFlag) {
                        sourceFile.delete();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                //关闭流
                try {
                    if (null != bis) {
                        bis.close();
                    }
                    if (null != zos) {
                        zos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return flag;
    }

    public static void compressToZip(String sourceFilePath, String zipFilePath, String zipFilename) {
        try {
            File sourceFile = new File(sourceFilePath);
            File zipFile = new File(zipFilePath );
            if (!zipFile.exists()) {
                zipFile.mkdirs();
            }
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile + "/" + zipFilename + ".zip"));
            if (sourceFile.isDirectory()) {
                File[] files = sourceFile.listFiles();
                for (File fileSec : files) {
                    if (false) {
                        recursionZip(zipOut, fileSec, zipFile.getName() + File.separator);
                    } else {
                        recursionZip(zipOut, fileSec, "");
                    }
                }
            }
            zipOut.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void recursionZip(ZipOutputStream zipOut, File file, String baseDir) throws Exception{
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File fileSec:files){
                recursionZip(zipOut, fileSec, baseDir + file.getName() + File.separator);
            }
        }else{
            byte[] buf = new byte[1024];
            InputStream input = new FileInputStream(file);
            zipOut.putNextEntry(new ZipEntry(baseDir + file.getName()));
            int len;
            while((len = input.read(buf)) != -1){
                zipOut.write(buf, 0, len);
            }
            input.close();
        }
    }

    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     * @return
     */
    public static boolean fileToZip(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws Exception {
        boolean flag = false;
        byte[] buf = new byte[1024];

        if (sourceFile.exists() == false) {
            System.out.println("待压缩的文件目录：" + sourceFile.getPath() + "不存在.");
        } else {
            try {
                if(sourceFile.getName().lastIndexOf("zip") <= 0 ) {
                    if (sourceFile.isFile()) {
                        // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
                        zos.putNextEntry(new ZipEntry(name));
                        // copy文件到zip输出流中
                        int len;
                        FileInputStream in = new FileInputStream(sourceFile);
                        while ((len = in.read(buf)) != -1) {
                            zos.write(buf, 0, len);
                        }
                        // Complete the entry
                        zos.closeEntry();
                        in.close();
                    } else {
                        File[] listFiles = sourceFile.listFiles();
                        if (listFiles == null || listFiles.length == 0) {
                            // 需要保留原来的文件结构时,需要对空文件夹进行处理
                            if (KeepDirStructure) {
                                // 空文件夹的处理
                                zos.putNextEntry(new ZipEntry(name + "/"));
                                // 没有文件，不需要文件的copy
                                zos.closeEntry();
                            }

                        } else {
                            for (File file : listFiles) {
                                // 判断是否需要保留原来的文件结构
                                if (KeepDirStructure) {
                                    // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                                    // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                                    fileToZip(file, zos, name + File.separator + file.getName(), KeepDirStructure);
                                } else {
                                    fileToZip(file, zos, file.getName(), KeepDirStructure);
                                }

                            }
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {

                //关闭流
            }
        }
        return flag;
    }

    /**
     * 将导入的sZipPathFile文件解压到指定sDestPath路径下
     *
     * @param sZipPathFile :待解压缩的文件路径
     * @param sDestPath    :解压缩后存放路径
     * @return
     */
    public static List<String> zipToFile(String sZipPathFile, String sDestPath) {
//        boolean flag = false;
        final int BUFFER_SIZE = 1024;
        List<String> result = new ArrayList<>();
        String ext = sZipPathFile.substring(sZipPathFile.lastIndexOf(".") + 1);
        try {
            File file = new File(sZipPathFile);
            String path = ZipUtil.class.getResource("/").getPath();
            if (!"zip".equalsIgnoreCase(ext) && !"rar".equalsIgnoreCase(ext)) {
                throw new Exception("只能上传rar或zip格式文件");
            }
            BufferedOutputStream dest;
            FileInputStream fis = new FileInputStream(file);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry ze;
            File zfile;
            //循环遍历zip中的每一个文件进行处理
            while ((ze = zis.getNextEntry()) != null) {
                zfile = new File(sDestPath + "//" + ze.getName());
                //如果是文件夹，且不存在就新建一个
                if (ze.isDirectory()) {
                    if (!zfile.exists()) {
                        zfile.mkdirs();
                    }
                    continue;
                } else { //如果是文件
                    ext = ze.getName().substring(ze.getName().lastIndexOf("."));
                    if (".doc".equalsIgnoreCase(ext) || ".docx".equalsIgnoreCase(ext)) {
                        zfile = new File(path + "word" + File.separator + ze.getName());
                    }
                    File fpath = new File(zfile.getParentFile().getPath());
                    if (!fpath.exists()) {
                        fpath.mkdirs();//文件的上级文件目录不存在则创建
                    }
                    result.add(zfile.getPath());
                    FileOutputStream fos = new FileOutputStream(zfile);
                    dest = new BufferedOutputStream(fos, BUFFER_SIZE);
                    int i;
                    byte ch[] = new byte[BUFFER_SIZE];
                    while ((i = zis.read(ch, 0, BUFFER_SIZE)) != -1) {
                        dest.write(ch, 0, i);
                    }
                    dest.flush();
                    dest.close();
                    fos.close();
                }
            }
            zis.close();
            fis.close();
            // if necessary, delete original zip-file
//            file.delete();
//            flag = true;
        } catch (Exception e) {
            System.err.println("Extract error:" + e.getMessage());
        }
        return result;
    }
}
