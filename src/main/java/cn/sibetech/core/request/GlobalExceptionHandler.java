package cn.sibetech.core.request;

import cn.sibetech.core.config.ApplicationContextUtils;
import cn.sibetech.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * 统一异常处理类
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler
    @ResponseBody
    public ResponseBean<String> handleException(Exception e){
        HttpServletRequest request = getRequest();
        if(e instanceof HttpRequestMethodNotSupportedException){
            String method = request.getMethod();
            if (HttpMethod.GET.toString().equals(method)) {
                return ResponseBean.error("500", "请求方法应为POST");
            } else if(HttpMethod.POST.toString().equals(method)) {
                return ResponseBean.error("500", "请求方法应为GET");
            } else {
                return ResponseBean.error("500", "请求方法仅支持GET或POST");
            }
        }
        else if (e instanceof NotPermissionException){
            return ResponseBean.error("403", "没有权限访问");
        }
        else if (e instanceof HttpMessageNotReadableException){
            if(e.getMessage().contains("JSON parse error")){
                return ResponseBean.error("500", "参数格式错误");
            }
            else {
                return ResponseBean.error("500", "请使用JSON方式传参");
            }
        }
        else if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException)e).getBindingResult();
            String message = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(";"));
            return ResponseBean.error("500", message);
        }
        else if(e instanceof ServiceException) {
            return ResponseBean.error("500", e.getMessage());
        }
        else {
            String profile = ApplicationContextUtils.getActiveProfile();
            log.error(">>> 服务器未知异常，请求地址：{}，具体信息：", request.getRequestURL(), e);
            return ResponseBean.error("500", "服务器异常"+(profile.equals("dev")?e.getMessage():""));
        }
    }

    private HttpServletRequest getRequest(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getRequest();
    }

}
