package cn.sibetech.core.util.excel;

import cn.sibetech.core.config.ResourceUtils;
import cn.sibetech.core.exception.ServiceException;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.Validate;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ExcelUtil {
    /**
     * 异步读取数据，返回bean集合
     * @param stream
     * @param obj
     * @param listener
     */
    public static<T> void read(InputStream stream, Class<T> obj, PageReadListener<T> listener){
        EasyExcel.read(stream, obj, listener).sheet().doRead();
    }

    /**
     * 异步读取数据，返回List<List<String>>
     * @param stream
     * @param listener
     */
    public static void read(InputStream stream, NoModelDataListener listener) {
        EasyExcel.read(stream, listener).sheet().doRead();
    }
    /**
     * 一次性读取所有数据，返回bean集合
     * @param stream
     * @param obj
     * @return
     */
    public static<T> List<T> readAll(InputStream stream, Class<T> obj){
        return EasyExcel.read(stream).head(obj).sheet().doReadSync();
    }

    /**
     * 根据xlsx模版导出
     * @throws IOException
     */
    public static void export2007(ExcelTemplateParam excelParam) {
        exportExcel(excelParam, ExcelTypeEnum.XLSX);
    }
    public static void export2003(ExcelTemplateParam excelParam) {
        exportExcel(excelParam, ExcelTypeEnum.XLS);
    }

    public static void exportFile(ExcelTableParam excelParam){
        if(!ObjectUtils.allNotNull(excelParam.getFilePath())){
            throw new ServiceException("excel filePath params is not valid");
        }
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(excelParam.getFilePath()).autoCloseStream(Boolean.TRUE).head(excelParam.getHeads());
        if(excelParam.getMergeSameRowsStrategy()!=null){
            excelWriterBuilder.registerWriteHandler(excelParam.getMergeSameRowsStrategy());
        }
        excelWriterBuilder.registerWriteHandler(new CustomCellWeightWeightConfig()).sheet().doWrite(excelParam.getData());
    }

    public static void exportFileForTemplate(ExcelTemplateParam excelParam){
        InputStream is = null;
        try {
            String root = ResourceUtils.getStaticPath();
            //模版输入流
            is = new FileInputStream(root + excelParam.getTemplatePath());
            HttpServletResponse response = excelParam.getResponse();
            ExcelWriter excelWriter = EasyExcel.write(excelParam.getFilePath()).autoCloseStream(true).withTemplate(is).build();
            ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.writerSheet();
            if (excelParam.isMerge()) {
                excelWriterSheetBuilder.registerWriteHandler(new MergeStrategy(excelParam.getMaxRow(), excelParam.getMergeCellIndex()));
            }
            WriteSheet writeSheet = excelWriterSheetBuilder.build();
            FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
            if( null != excelParam.getData() ) {
                for (RowModel row : excelParam.getData()) {
                    if (row.getData() instanceof Map) {
                        excelWriter.fill(row.getData(), writeSheet);
                    } else if (row.getData() instanceof Collection) {
                        if (row.isHorizontal()) {
                            excelWriter.fill(new FillWrapper(row.getName(), (Collection) row.getData()), fillConfig, writeSheet);
                        } else {
                            excelWriter.fill(new FillWrapper(row.getName(), (Collection) row.getData()), writeSheet);
                        }
                    }
                }
            }
            excelWriter.finish();

        } catch (IOException e){
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
    private static void exportExcel(ExcelTemplateParam excelParam, ExcelTypeEnum typeEnum){
        Validate.isTrue(excelParam.isValid(), "excel params is not valid");
        InputStream is = null;
        ServletOutputStream out = null;
        try {
            String root = ResourceUtils.getStaticPath();
            //模版输入流
            is = new FileInputStream(root + excelParam.getTemplatePath());
            HttpServletResponse response = excelParam.getResponse();
            out = response.getOutputStream();
            prepareResponds(response, excelParam.getFilename(), typeEnum);
            ExcelWriter excelWriter = EasyExcel.write(out).withTemplate(is).build();
            ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.writerSheet();
            if (excelParam.isMerge()) {
                excelWriterSheetBuilder.registerWriteHandler(new MergeStrategy(excelParam.getMaxRow(), excelParam.getMergeCellIndex()));
            }
            WriteSheet writeSheet = excelWriterSheetBuilder.build();
            FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
            if( null != excelParam.getData() ) {
                for (RowModel row : excelParam.getData()) {
                    if (row.getData() instanceof Map) {
                        excelWriter.fill(row.getData(), writeSheet);
                    } else if (row.getData() instanceof Collection) {
                        if (row.isHorizontal()) {
                            excelWriter.fill(new FillWrapper(row.getName(), (Collection) row.getData()), fillConfig, writeSheet);
                        } else {
                            excelWriter.fill(new FillWrapper(row.getName(), (Collection) row.getData()), writeSheet);
                        }
                    }
                }
            }
            excelWriter.finish();
            out.flush();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (is != null) {
                    is.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private static void prepareResponds(HttpServletResponse response, String filename, ExcelTypeEnum typeEnum) throws UnsupportedEncodingException {
        switch (typeEnum){
            case XLSX:
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setCharacterEncoding("utf-8");
                String fileName = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
                /*                response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ExcelTypeEnum.XLSX.getValue());
                 */
                response.setHeader("Content-disposition", "attachment;filename=" + fileName + ExcelTypeEnum.XLSX.getValue());
                break;
            case XLS:
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GB2312"), "ISO8859-1") + ExcelTypeEnum.XLS.getValue());
                break;
            default:
                break;
        }
    }
    private static Object getGetMethod(Object ob, String name) {
        try {
            Field declaredField = ob.getClass().getDeclaredField(name);
            declaredField.setAccessible(true);
            PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(ob.getClass(), declaredField.getName());
            Method readMethod = pd.getReadMethod();
            return readMethod.invoke(ob);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 导出列表数据
     * @param param
     */
    public static void export(ExcelTableParam param){
        Validate.isTrue(param.isValid(), "excel params is not valid");
        ServletOutputStream out = null;
        try {
            HttpServletResponse response = param.getResponse();
            out = response.getOutputStream();
            prepareResponds(response, param.getFilename(), ExcelTypeEnum.XLSX);
            EasyExcel.write(out).autoCloseStream(Boolean.FALSE).head(param.getHeads())
                    .registerWriteHandler(param.getMergeSameRowsStrategy())
                    .registerWriteHandler(new CustomCellWeightWeightConfig()).sheet().doWrite(param.getData());
            out.flush();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
