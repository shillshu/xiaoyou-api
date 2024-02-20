package cn.sibetech.core.util;

import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author liwj
 * @date 2019-11-7
 */
public class VerifyImageUtil {
    private static int BOLD = 5;
    private static final String IMG_FILE_TYPE = "png";
    private static final String TEMP_IMG_FILE_TYPE = "png";
    public static Map<String,Object> pictureTemplatesCut(InputStream templateFile, InputStream targetFile) throws Exception{
        Map<String, Object> pictureMap = new HashMap<String, Object>();

        BufferedImage imageTemplate = ImageIO.read(templateFile);
        int templateWidth = imageTemplate.getWidth();
        int templateHeight = imageTemplate.getHeight();
        // 原图
        BufferedImage oriImage = ImageIO.read(targetFile);
        int oriImageWidth = oriImage.getWidth();
        int oriImageHeight = oriImage.getHeight();
        Random random = new Random();
        int widthRandom = random.nextInt(oriImageWidth - 2*templateWidth) + templateWidth;
        int heightRandom = random.nextInt(oriImageHeight - templateHeight);
        BufferedImage newImage = new BufferedImage(templateWidth, templateHeight, imageTemplate.getType());
        //得到画笔对象
        Graphics2D graphics = newImage.createGraphics();
        //如果需要生成RGB格式，需要做如下配置,Transparency 设置透明
        newImage = graphics.getDeviceConfiguration().createCompatibleImage(templateWidth, templateHeight, Transparency.TRANSLUCENT);

        // 新建的图像根据模板颜色赋值,源图生成遮罩
        cutByTemplate(oriImage,imageTemplate,newImage,widthRandom,heightRandom);

        // 设置“抗锯齿”的属性
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setStroke(new BasicStroke(BOLD, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        graphics.drawImage(newImage, 0, 0, null);
        graphics.dispose();

        ByteArrayOutputStream newImageOs = new ByteArrayOutputStream();
        ImageIO.write(newImage, TEMP_IMG_FILE_TYPE, newImageOs);
        byte[] newImagebyte = newImageOs.toByteArray();

        ByteArrayOutputStream oriImagesOs = new ByteArrayOutputStream();
        ImageIO.write(oriImage, IMG_FILE_TYPE, oriImagesOs);
        byte[] oriImageByte = oriImagesOs.toByteArray();

        pictureMap.put("smallImage", Base64Utils.encodeToString(newImagebyte));
        pictureMap.put("bigImage", Base64Utils.encodeToString(oriImageByte));
        pictureMap.put("xWidth",widthRandom);
        pictureMap.put("yHeight",heightRandom);
        return pictureMap;
    }
    public static Map<String,Object> pictureTemplatesCut(File templateFile, File targetFile) throws Exception {
        Map<String, Object> pictureMap = new HashMap<String, Object>();

        BufferedImage imageTemplate = ImageIO.read(templateFile);
        int templateWidth = imageTemplate.getWidth();
        int templateHeight = imageTemplate.getHeight();
        // 原图
        BufferedImage oriImage = ImageIO.read(targetFile);
        int oriImageWidth = oriImage.getWidth();
        int oriImageHeight = oriImage.getHeight();
        Random random = new Random();
        int widthRandom = random.nextInt(oriImageWidth - 2*templateWidth) + templateWidth;
        int heightRandom = random.nextInt(oriImageHeight - templateHeight);
        BufferedImage newImage = new BufferedImage(templateWidth, templateHeight, imageTemplate.getType());
        //得到画笔对象
        Graphics2D graphics = newImage.createGraphics();
        //如果需要生成RGB格式，需要做如下配置,Transparency 设置透明
        newImage = graphics.getDeviceConfiguration().createCompatibleImage(templateWidth, templateHeight, Transparency.TRANSLUCENT);

        // 新建的图像根据模板颜色赋值,源图生成遮罩
        cutByTemplate(oriImage,imageTemplate,newImage,widthRandom,heightRandom);

        // 设置“抗锯齿”的属性
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setStroke(new BasicStroke(BOLD, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        graphics.drawImage(newImage, 0, 0, null);
        graphics.dispose();

        ByteArrayOutputStream newImageOs = new ByteArrayOutputStream();
        ImageIO.write(newImage, TEMP_IMG_FILE_TYPE, newImageOs);
        byte[] newImagebyte = newImageOs.toByteArray();

        ByteArrayOutputStream oriImagesOs = new ByteArrayOutputStream();
        ImageIO.write(oriImage, IMG_FILE_TYPE, oriImagesOs);
        byte[] oriImageByte = oriImagesOs.toByteArray();

        pictureMap.put("smallImage", Base64Utils.encodeToString(newImagebyte));
        pictureMap.put("bigImage", Base64Utils.encodeToString(oriImageByte));
        pictureMap.put("xWidth",widthRandom);
        pictureMap.put("yHeight",heightRandom);
        return pictureMap;
    }
    private static void cutByTemplate(BufferedImage oriImage, BufferedImage templateImage,BufferedImage newImage, int x, int y){
        //临时数组遍历用于高斯模糊存周边像素值
        int[][] martrix = new int[3][3];
        int[] values = new int[9];

        int xLength = templateImage.getWidth();
        int yLength = templateImage.getHeight();
        // 模板图像宽度
        for (int i = 0; i < xLength; i++) {
            // 模板图片高度
            for (int j = 0; j < yLength; j++) {
                // 如果模板图像当前像素点不是透明色 copy源文件信息到目标图片中
                int rgb = templateImage.getRGB(i, j);
                if (rgb < 0) {
                    newImage.setRGB(i, j,oriImage.getRGB(x + i, y + j));

                    //抠图区域高斯模糊
                    readPixel(oriImage, x + i, y + j, values);
                    fillMatrix(martrix, values);
                    oriImage.setRGB(x + i, y + j, avgMatrix(martrix));
                }

                //防止数组越界判断
                if(i == (xLength-1) || j == (yLength-1)){
                    continue;
                }
                int rightRgb = templateImage.getRGB(i + 1, j);
                int downRgb = templateImage.getRGB(i, j + 1);
                //描边处理，,取带像素和无像素的界点，判断该点是不是临界轮廓点,如果是设置该坐标像素是白色
                if((rgb >= 0 && rightRgb < 0) || (rgb < 0 && rightRgb >= 0) || (rgb >= 0 && downRgb < 0) || (rgb < 0 && downRgb >= 0)){
                    newImage.setRGB(i, j, Color.white.getRGB());
                    oriImage.setRGB(x + i, y + j,Color.white.getRGB());
                }
            }
        }
    }

    private static void readPixel(BufferedImage img, int x, int y, int[] pixels) {
        int xStart = x - 1;
        int yStart = y - 1;
        int current = 0;
        for (int i = xStart; i < 3 + xStart; i++)
            for (int j = yStart; j < 3 + yStart; j++) {
                int tx = i;
                if (tx < 0) {
                    tx = -tx;

                } else if (tx >= img.getWidth()) {
                    tx = x;
                }
                int ty = j;
                if (ty < 0) {
                    ty = -ty;
                } else if (ty >= img.getHeight()) {
                    ty = y;
                }
                pixels[current++] = img.getRGB(tx, ty);

            }
    }

    private static void fillMatrix(int[][] matrix, int[] values) {
        int filled = 0;
        for (int i = 0; i < matrix.length; i++) {
            int[] x = matrix[i];
            for (int j = 0; j < x.length; j++) {
                x[j] = values[filled++];
            }
        }
    }

    private static int avgMatrix(int[][] matrix) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (int i = 0; i < matrix.length; i++) {
            int[] x = matrix[i];
            for (int j = 0; j < x.length; j++) {
                if (j == 1) {
                    continue;
                }
                Color c = new Color(x[j]);
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }
        return new Color(r / 8, g / 8, b / 8).getRGB();
    }
}
