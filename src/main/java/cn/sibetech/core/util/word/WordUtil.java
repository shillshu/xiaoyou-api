package cn.sibetech.core.util.word;

import cn.sibetech.core.config.ResourceUtils;
import cn.sibetech.core.util.HttpUtils;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.ddr.poi.html.HtmlRenderPolicy;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author liwj
 * @date 2022/3/24 15:45
 */
public class WordUtil {
    public static void exportTemplate(String path, String filename, HttpServletResponse response){
        InputStream reader = null;
        OutputStream out = null;
        try {
            String root = ResourceUtils.getStaticPath();
            reader = new FileInputStream(root + path);
            byte[] buf = new byte[1024];
            int len = 0;
            String ext = path.substring(path.lastIndexOf("."));
            response.setContentType("application/octet-stream; ");
            response.addHeader("Content-Disposition", "attachment;filename=" + HttpUtils.convert(filename+ext));
            out = response.getOutputStream();
            while ((len = reader.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ex) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception ex) {
                }
            }
        }

    }

    /**
     * 根据模版导出
     * @param param
     */
    public static void export(WordTemplateParam param){
        Validate.isTrue(param.isValid(), "word params is not valid");
        InputStream is = null;
        ServletOutputStream out = null;
        try {
            String root = WordUtil.class.getResource("/").getPath();
            //模版输入流
            is = new FileInputStream(root + param.getTemplatePath());
            String ext = param.getTemplatePath().substring(param.getTemplatePath().lastIndexOf("."));
            HttpServletResponse response = param.getResponse();
            response.setContentType("application/octet-stream; charset=UTF-8");
            String filename = param.getFilename();
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GB2312"), "ISO8859-1") + ext);
            out = response.getOutputStream();
            ConfigureBuilder configureBuilder = Configure.builder();
            configureBuilder.useSpringEL();
            configureBuilder.addPlugin('%', new DoubleRenderPolicy());
            if(CollectionUtils.isNotEmpty(param.getRenderPolicies())){
                for(WordRenderPolicy wordRenderPolicy:param.getRenderPolicies()) {
                    switch (wordRenderPolicy.getRenderEnum()) {
                        case TABLE:
                            configureBuilder.bind(wordRenderPolicy.getKey(), new LoopRowTableRenderPolicy());
                            break;
                        case HTML:
                            configureBuilder.bind(wordRenderPolicy.getKey(), new HtmlRenderPolicy());
                            break;
                        case PARAGRAPH:
                            configureBuilder.bind(wordRenderPolicy.getKey(), new TableParagraphRenderPolicy());
                            break;
                        default:
                            break;
                    }
                }
            }

            XWPFTemplate template = XWPFTemplate.compile(is, configureBuilder.build()).render(param.getData());
            template.writeAndClose(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    public static void exportFile(WordTemplateParam param){
        InputStream is = null;
        OutputStream out = null;
        try {
            String root = WordUtil.class.getResource("/").getPath();
            //模版输入流
            is = new FileInputStream(root + param.getTemplatePath());
            String ext = param.getTemplatePath().substring(param.getTemplatePath().lastIndexOf("."));

            String filename = param.getFilename();
            File file = new File(filename+ext);
            out = new FileOutputStream(param.getPath()+file);
            ConfigureBuilder configureBuilder = Configure.builder();
            configureBuilder.useSpringEL();
            configureBuilder.addPlugin('%', new DoubleRenderPolicy());
            if(CollectionUtils.isNotEmpty(param.getRenderPolicies())){
                for(WordRenderPolicy wordRenderPolicy:param.getRenderPolicies()) {
                    switch (wordRenderPolicy.getRenderEnum()) {
                        case TABLE:
                            configureBuilder.bind(wordRenderPolicy.getKey(), new LoopRowTableRenderPolicy());
                            break;
                        case HTML:
                            configureBuilder.bind(wordRenderPolicy.getKey(), new HtmlRenderPolicy());
                            break;
                        case PARAGRAPH:
                            configureBuilder.bind(wordRenderPolicy.getKey(), new TableParagraphRenderPolicy());
                            break;
                        default:
                            break;
                    }
                }
            }
//            Map<String,Object> data = param.getData();
//            LoopRowTableRenderPolicy loopRowTableRenderPolicy = new LoopRowTableRenderPolicy();
//            for(String key: data.keySet()) {
//                Object obj = data.get(key);
//                if(obj instanceof List) {
//                    configureBuilder.bind(key, loopRowTableRenderPolicy);
//                }
//            }
            XWPFTemplate template = XWPFTemplate.compile(is, configureBuilder.build()).render(param.getData());
            template.writeAndClose(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }


    public static void exportVfsFile(WordTemplateParam param){
        InputStream is = null;
        OutputStream out = null;
        try {
            //模版输入流
            is = new FileInputStream(param.getTemplatePath());
            String ext = param.getTemplatePath().substring(param.getTemplatePath().lastIndexOf("."));

            String filename = param.getFilename();
            File file = new File(filename+ext);
            out = new FileOutputStream(param.getPath()+file);
            ConfigureBuilder configureBuilder = Configure.builder();
            configureBuilder.useSpringEL();
            configureBuilder.addPlugin('%', new DoubleRenderPolicy());
            if(CollectionUtils.isNotEmpty(param.getRenderPolicies())){
                for(WordRenderPolicy wordRenderPolicy:param.getRenderPolicies()) {
                    switch (wordRenderPolicy.getRenderEnum()) {
                        case TABLE:
                            configureBuilder.bind(wordRenderPolicy.getKey(), new LoopRowTableRenderPolicy());
                            break;
                        case HTML:
                            configureBuilder.bind(wordRenderPolicy.getKey(), new HtmlRenderPolicy());
                            break;
                        case PARAGRAPH:
                            configureBuilder.bind(wordRenderPolicy.getKey(), new TableParagraphRenderPolicy());
                            break;
                        default:
                            break;
                    }
                }
            }
//            Map<String,Object> data = param.getData();
//            LoopRowTableRenderPolicy loopRowTableRenderPolicy = new LoopRowTableRenderPolicy();
//            for(String key: data.keySet()) {
//                Object obj = data.get(key);
//                if(obj instanceof List) {
//                    configureBuilder.bind(key, loopRowTableRenderPolicy);
//                }
//            }
            XWPFTemplate template = XWPFTemplate.compile(is, configureBuilder.build()).render(param.getData());
            template.writeAndClose(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}
