package com.github.frankzengjj.Wiki.controller;

import com.github.frankzengjj.Wiki.exception.BusinessException;
import com.github.frankzengjj.Wiki.exception.BusinessExceptionCode;
import com.github.frankzengjj.Wiki.response.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理、数据预处理等
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 校验异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public CommonResponse validExceptionHandler(BindException e) {
        CommonResponse commonResponse = new CommonResponse();
        LOG.warn("参数校验失败：{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        commonResponse.setSuccess(false);
        commonResponse.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return commonResponse;
    }

    /**
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public CommonResponse validExceptionHandler(BusinessException e) {
        CommonResponse commonResponse = new CommonResponse();
        LOG.warn("Business Exception：{}", e.getCode().getDesc());
        commonResponse.setSuccess(false);
        commonResponse.setMessage(e.getCode().getDesc());
        return commonResponse;
    }

    /**
     * 校验异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResponse validExceptionHandler(Exception e) {
        CommonResponse commonResponse = new CommonResponse();
        LOG.error("System Exception: ", e);
        commonResponse.setSuccess(false);
        commonResponse.setMessage("System Exception. Please contact");
        return commonResponse;
    }
}

