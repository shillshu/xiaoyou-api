package cn.sibetech.fellow.domain.validate;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author liwj
 * @date 2022/1/24 15:59
 */
public class BaseValidate {
    public static void validateString(List<String> errors, String name, String value, String selectValue, int max, boolean validateNull){
        if(validateNull){
            if(StringUtils.isEmpty(value)){
                errors.add(name+"不能为空");
            }
            else {
                if(max>0 && value.length()>max){
                    errors.add(name+"不能超过"+max+"字");
                }
            }
        }
        else {
            if(StringUtils.isNotEmpty(value) && max>0 && value.length()>max){
                errors.add(name+"不能超过"+max+"字");
            }
        }
        if( StringUtils.isNotEmpty(selectValue) && StringUtils.isNotEmpty(value)){
            if( !selectValue.contains(value)){
                errors.add(name+"只能为"+selectValue);
            }
        }
    }
    public static void validateFloat(List<String> errors, String name, Float value, float max, boolean validateNull){
        if(validateNull){
            if(value == null){
                errors.add(name+"不能为空");
            }
            else {
                if(max>0 && value<=max){
                    errors.add(name+"不能小于等于"+max);
                }
            }
        }
        else {
            if(value!=null && max>0 && value<=max){
                errors.add(name+"不能小于等于"+max);
            }
        }

    }

    public static void validateDouble(List<String> errors, String name, Double value, double max, boolean validateNull){
        if(validateNull){
            if(value == null){
                errors.add(name+"不能为空");
            }
            else {
                if(max>0 && value<=max){
                    errors.add(name+"不能小于等于"+max);
                }
            }
        }
        else {
            if(value!=null && max>0 && value<=max){
                errors.add(name+"不能小于等于"+max);
            }
        }

    }

}
