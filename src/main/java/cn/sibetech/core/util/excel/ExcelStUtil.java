package cn.sibetech.core.util.excel;


import cn.sibetech.core.util.HttpUtils;
import cn.sibetech.core.util.VFSUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Excel 写操作类
 *
 * @author liwj
 * @date 2019/4/22
 */
public class ExcelStUtil {
    public static final String EXCEL97_EXT = ".xls";

    public static void export(OutputStream outputStream,  List<String[]> header, List<Map<String, Object>> target) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        long startTime = System.currentTimeMillis();
        StringTemplateGroup stGroup = new StringTemplateGroup("stringTemplate");
        //解决可能发生的中文乱码
        stGroup.setFileCharEncoding("UTF-8");
        //写入excel文件头部信息
        StringTemplate head =  new StringTemplate(ST_HEAD);//stGroup.getInstanceOf("st/body");//stGroup.getInstanceOf("st/head");
        PrintWriter writer = new PrintWriter(new BufferedOutputStream(outputStream));
        writer.print(head.toString());
        writer.flush();

        int totalRowNum = target.size();
        int maxRowNum = 60000;
        int sheets = totalRowNum % 60000 == 0 ? (totalRowNum/maxRowNum) : (totalRowNum/maxRowNum +1);
        //excel单表最大行数是65535

        List<Map<String, Object>>  record = target;
        List<String> headers = new ArrayList<String>();
        List<String> keys = new ArrayList<String>();
        for (String[] h : header) {
            keys.add(h[0]);
            headers.add(h[1]);
        }
        int columnLength = keys.size();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //写入excel文件数据信息
        for(int i=0;i<sheets;i++){
            StringTemplate body =  new StringTemplate(ST_BODY);//stGroup.getInstanceOf("st/body");
            Worksheet worksheet = new Worksheet();
            worksheet.setTitle(headers);
            worksheet.setSheet(" "+(i+1)+" ");
            worksheet.setColumnNum(columnLength);
            worksheet.setRowNum(maxRowNum+1);
            List<Workrow> rows = new ArrayList<Workrow>();
            int startIndex = i*maxRowNum;
            int endIndex = Math.min((i+1)*maxRowNum -1,totalRowNum-1);
            for(int j=startIndex;j<=endIndex;j++){
                Workrow row = new Workrow();
                List<String> result = new ArrayList<String>(columnLength);
                for(int n=0;n<keys.size();n++){
                    Object value = record.get(j).get(keys.get(n));
                    if(value == null){
                        result.add("");
                    }else{
                        if(value instanceof Date){
                            result.add(sdf.format((Date)value));
                        }else{
                            result.add(value.toString());
                        }
                    }

                }
                row.setResult(result);
                rows.add(row);
            }
            worksheet.setRows(rows);
            body.setAttribute("worksheet", worksheet);
            writer.print(body.toString());
            writer.flush();
            rows.clear();
            rows = null;
            worksheet = null;
            body = null;
            Runtime.getRuntime().gc();
            System.out.println("正在生成excel文件的 sheet"+(i+1));
        }

        //写入excel文件尾部
        writer.print("</Workbook>");
        writer.flush();
        writer.close();
        System.out.println("生成excel文件完成");
        long endTime = System.currentTimeMillis();
        System.out.println("用时="+((endTime-startTime)/1000)+"秒");
    }


    public static <T> void exportData(OutputStream outputStream,  List<String[]> header,  List<T> target) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        long startTime = System.currentTimeMillis();
        StringTemplateGroup stGroup = new StringTemplateGroup("stringTemplate");
        //解决可能发生的中文乱码
        stGroup.setFileCharEncoding("UTF-8");
        //写入excel文件头部信息
        StringTemplate head =  new StringTemplate(ST_HEAD);//stGroup.getInstanceOf("st/body");//stGroup.getInstanceOf("st/head");
        PrintWriter writer = new PrintWriter(new BufferedOutputStream(outputStream));
        writer.print(head.toString());
        writer.flush();

        int totalRowNum = target.size();
        int maxRowNum = 60000;
        int sheets = totalRowNum % 60000 == 0 ? (totalRowNum/maxRowNum) : (totalRowNum/maxRowNum +1);
        //excel单表最大行数是65535

        List<T>  record = target;
        List<String> headers = new ArrayList<String>();
        List<String> keys = new ArrayList<String>();
        for (String[] h : header) {
            keys.add(h[0]);
            headers.add(h[1]);
        }
        int columnLength = keys.size();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //写入excel文件数据信息
        for(int i=0;i<sheets;i++){
            StringTemplate body =  new StringTemplate(ST_BODY);//stGroup.getInstanceOf("st/body");
            Worksheet worksheet = new Worksheet();
            worksheet.setTitle(headers);
            worksheet.setSheet(" "+(i+1)+" ");
            worksheet.setColumnNum(columnLength);
            worksheet.setRowNum(maxRowNum+1);
            List<Workrow> rows = new ArrayList<Workrow>();
            int startIndex = i*maxRowNum;
            int endIndex = Math.min((i+1)*maxRowNum -1,totalRowNum-1);
            for(int j=startIndex;j<=endIndex;j++){
                Workrow row = new Workrow();
                List<String> result = new ArrayList<String>(columnLength);
                for(int n=0;n<keys.size();n++){
                    Map<String,Object> m = JSONObject.parseObject(JSON.toJSONString(record.get(j)));
                    Object value = m.get(keys.get(n));
                    if(value == null){
                        result.add("");
                    }else{
                        if(value instanceof Date){
                            result.add(sdf.format((Date)value));
                        }else{
                            result.add(value.toString());
                        }
                    }

                }
                row.setResult(result);
                rows.add(row);
            }
            worksheet.setRows(rows);
            body.setAttribute("worksheet", worksheet);
            writer.print(body.toString());
            writer.flush();
            rows.clear();
            rows = null;
            worksheet = null;
            body = null;
            Runtime.getRuntime().gc();
            System.out.println("正在生成excel文件的 sheet"+(i+1));
        }

        //写入excel文件尾部
        writer.print("</Workbook>");
        writer.flush();
        writer.close();
        System.out.println("生成excel文件完成");
        long endTime = System.currentTimeMillis();
        System.out.println("用时="+((endTime-startTime)/1000)+"秒");
    }

    public static void exportForMap(HttpServletResponse response, HttpServletRequest request, String filename, List<String[]> header, List<Map<String, Object>> data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        response.setContentType("application/octet-stream");// 均不提供直接打开
        response.setHeader("Content-Disposition", "attachment;filename="
                + HttpUtils.convert(filename + EXCEL97_EXT));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ExcelStUtil.export(byteArrayOutputStream,header, data);
        OutputStream output = null;
        try {
            //解决可能发生的中文乱码
            ByteArrayInputStream reader = new ByteArrayInputStream(byteArrayOutputStream.toString().getBytes("UTF-8"));
            output = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = reader.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(byteArrayOutputStream!=null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(output!=null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static <T> void exportForData(HttpServletResponse response, HttpServletRequest request, String filename, List<String[]> header, List<T> data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        response.setContentType("application/octet-stream");// 均不提供直接打开
        response.setHeader("Content-Disposition", "attachment;filename="
                + HttpUtils.convert(filename + EXCEL97_EXT));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ExcelStUtil.exportData(byteArrayOutputStream,header, data);
        OutputStream output = null;
        try {
            //解决可能发生的中文乱码
            ByteArrayInputStream reader = new ByteArrayInputStream(byteArrayOutputStream.toString().getBytes("UTF-8"));
            output = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = reader.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(byteArrayOutputStream!=null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(output!=null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static <T> void exportToVfs(String path, String filename, List<String[]> header, List<T> data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ExcelStUtil.exportData(byteArrayOutputStream,header, data);
        OutputStream output = null;
        try {
            //解决可能发生的中文乱码
            ByteArrayInputStream reader = new ByteArrayInputStream(byteArrayOutputStream.toString().getBytes("UTF-8"));
            String vfsPath =  VFSUtil.getVFSPath(path);
            File file = new File(vfsPath + File.separator + filename);
            VFSUtil.getFileIsNotExistCreate(path);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            output = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = reader.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(byteArrayOutputStream!=null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(output!=null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public static void main(String[] args) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
       /* System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
        System.out.println(ExcelStUtil.class.getResource("").getPath());
        System.out.println(ExcelStUtil.class.getClassLoader().getResource("").getPath());
        List<Sample> result = new ArrayList<Sample>();
        for(int i=0;i<100;i++){
            result.add(new Sample("放大双方的"+String.valueOf(i),String.valueOf(i)));
        }
        //OutputStream outputStream = new FileOutputStream("D:/output2.xls");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ExcelStUtil.export(byteArrayOutputStream,result);
        //ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        //解决可能发生的中文乱码
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toString().getBytes("UTF-8"));

        File file = new File("D:/output2.xls");
        OutputStream output = new FileOutputStream(file);
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
        //bufferedOutput.write(byteArrayOutputStream.toByteArray());
        bufferedOutput.write(byteArrayOutputStream.toString().getBytes("UTF-8"));
        bufferedOutput.flush();
        bufferedOutput.close();
*/
    }

    private static final String ST_HEAD = "<?xml version=\"1.0\"?>\n" +
            "<?mso-application progid=\"Excel.Sheet\"?>\n" +
            "<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"\n" +
            " xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n" +
            " xmlns:x=\"urn:schemas-microsoft-com:office:excel\"\n" +
            " xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"\n" +
            " xmlns:html=\"http://www.w3.org/TR/REC-html40\">\n" +
            " <DocumentProperties xmlns=\"urn:schemas-microsoft-com:office:office\">\n" +
            "  <Created>1996-12-17T01:32:42Z</Created>\n" +
            "  <LastSaved>2013-08-02T09:21:24Z</LastSaved>\n" +
            "  <Version>11.9999</Version>\n" +
            " </DocumentProperties>\n" +
            " <OfficeDocumentSettings xmlns=\"urn:schemas-microsoft-com:office:office\">\n" +
            "  <RemovePersonalInformation/>\n" +
            " </OfficeDocumentSettings>\n" +
            " <ExcelWorkbook xmlns=\"urn:schemas-microsoft-com:office:excel\">\n" +
            "  <WindowHeight>4530</WindowHeight>\n" +
            "  <WindowWidth>8505</WindowWidth>\n" +
            "  <WindowTopX>480</WindowTopX>\n" +
            "  <WindowTopY>120</WindowTopY>\n" +
            "  <AcceptLabelsInFormulas/>\n" +
            "  <ProtectStructure>False</ProtectStructure>\n" +
            "  <ProtectWindows>False</ProtectWindows>\n" +
            " </ExcelWorkbook>\n" +
            " <Styles>\n" +
            "  <Style ss:ID=\"Default\" ss:Name=\"Normal\">\n" +
            "   <Alignment ss:Vertical=\"Bottom\"/>\n" +
            "   <Borders/>\n" +
            "   <Font ss:FontName=\"宋体\" x:CharSet=\"134\" ss:Size=\"12\"/>\n" +
            "   <Interior/>\n" +
            "   <NumberFormat/>\n" +
            "   <Protection/>\n" +
            "  </Style>\n" +
            "</Styles>";

    private static final String ST_BODY = "$worksheet:{\n" +
            " <Worksheet ss:Name=\"$it.sheet$\">\n" +
            "  <Table ss:ExpandedColumnCount=\"$it.columnNum$\" ss:ExpandedRowCount=\"$it.rowNum$\" x:FullColumns=\"1\"\n" +
            "   x:FullRows=\"1\" ss:DefaultColumnWidth=\"54\" ss:DefaultRowHeight=\"14.25\">\n" +
            "   <Row>\n" +
            "   $it.title:{\n" +
            "   <Cell><Data ss:Type=\"String\">$it$</Data></Cell>\n" +
            "   }$\n" +
            "   </Row>\n" +
            " $it.rows:{\n" +
            " <Row>\n" +
            " $it.result:{\n" +
            " <Cell><Data ss:Type=\"String\">$it$</Data></Cell>\n" +
            " }$\n" +
            "   </Row>\n" +
            " }$\n" +
            "  </Table>\n" +
            " </Worksheet>\n" +
            "}$";
}
