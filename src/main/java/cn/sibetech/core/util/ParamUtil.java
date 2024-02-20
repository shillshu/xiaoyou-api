package cn.sibetech.core.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;

public class ParamUtil {
    private static final Log logger = LogFactory.getLog(ParamUtil.class);

    public static Float getFloatValue(Object obj){
        if( obj instanceof BigDecimal){
            return ((BigDecimal) obj).floatValue();
        }else if( obj instanceof  String ){
            if(StringUtils.isNotEmpty((String)obj)) {
                return Float.valueOf((String) obj);
            }else{
                return null;
            }
        }else if( obj instanceof  Integer ){
            return Float.valueOf((Integer)obj);
        }else if( obj instanceof  Float ){
            return (Float)obj;
        }else{
            return new Float(0);
        }
    }

    public static Double getDoubleValue(Object obj){
        if( obj instanceof BigDecimal){
            return ((BigDecimal) obj).doubleValue();
        }else if( obj instanceof  String ){
            if(StringUtils.isNotEmpty((String)obj)) {
                return Double.valueOf((String) obj);
            }else{
                return null;
            }
        }else if( obj instanceof  Integer ){
            return Double.valueOf((Integer)obj);
        }else if( obj instanceof  Double ){
            return (Double)obj;
        }else if( obj instanceof  Float ){
            return ((Float) obj).doubleValue();
        }else{
            return new Double(0);
        }
    }

    public static BigDecimal getBigDecimalValue(Object obj){
        if( obj instanceof BigDecimal){
            return (BigDecimal) obj;
        }else if( obj instanceof  String ){
            if(StringUtils.isNotEmpty((String)obj)) {
                return new BigDecimal((String) obj);
            }else{
                return new BigDecimal("0");
            }
        }else if( obj instanceof  Integer ){
            return  new BigDecimal((Integer)obj);
        }else{
            return new BigDecimal(0);
        }
    }

    public static String getStringValue(Object obj){
        if (obj instanceof Float) {
            return (Float) obj + "";
        } else if (obj instanceof Integer) {
            return (Integer) obj + "";
        } else if (obj instanceof BigDecimal) {
            BigDecimal bvalue = (BigDecimal) obj;
            return bvalue.toString();
        } else if (obj instanceof String) {
            return (String) obj;
        }else{
            return null;
        }
    }

    public static Boolean getBooleanValue(Object obj){
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        } else {
            return false;
        }
    }

    public static Double doubleAdd(Double score1, Double score2){
        score1 = null == score1 ? 0d : score1;
        score2 = null == score2 ? 0d : score2;
        /* 四位小数 四舍五入*/
        return new BigDecimal(score1).add( new BigDecimal(score2)).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static Double doubleScale(Double score){
        score = null == score ? 0d : score;
        /* 四位小数 四舍五入*/
        return new BigDecimal(score).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static Double doubleDivide(Double score1, Double score2){
        score1 = null == score1 ? 0d : score1;
        score2 = null == score2 ? 0d : score2;
        /* 四位小数 四舍五入*/
        return new BigDecimal(score1).divide( new BigDecimal(score2)).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
