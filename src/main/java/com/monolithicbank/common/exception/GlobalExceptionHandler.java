package com.monolithicbank.common.exception;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = {
    "com.monolithicbank.account.controller",
    "com.monolithicbank.b2bt.controller",
    "com.monolithicbank.customer.controller",
    "com.monolithicbank.product.controller",
    "com.monolithicbank.transfer.controller",
    "com.monolithicbank.user.controller"
})
public class GlobalExceptionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<Object> handleException(BusinessException e) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("message", "[Notice]\n" + e.getMessage());
        result.put("httpStatus", e.getHttpStatus().value());

        return new ResponseEntity<>(result, e.getHttpStatus());
    }
    
    @ExceptionHandler(SystemException.class)
    protected ResponseEntity<Object> handleException(SystemException e) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("message", "[System Error]\n" + e.getMessage());
        result.put("httpStatus", e.getHttpStatus().value());

        return new ResponseEntity<>(result, e.getHttpStatus());
    }
    
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception e) {
        Map<String, Object> result = new HashMap<String, Object>();
        ResponseEntity<Object> ret = null;
        
        if (e instanceof BusinessException) {
            BusinessException b = (BusinessException) e;
            result.put("message", "[Notice]\n" + e.getMessage());
            result.put("httpStatus", b.getHttpStatus().value());
            ret = new ResponseEntity<>(result, b.getHttpStatus());
        } else if (e instanceof SystemException) {
            SystemException s = (SystemException)e;
            result.put("message", "[System Error]\n" + s.getMessage());
            result.put("httpStatus", s.getHttpStatus().value());
            ret = new ResponseEntity<>(result, s.getHttpStatus());
            
            LOGGER.error(s.getMessage(), s);
        } else {
            String msg = "An unexpected problem has occurred.\nPlease contact the administrator.";
            result.put("message", msg);
            result.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR.value());
            ret = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
           
            LOGGER.error(e.getMessage(), e);
        }
        
        return ret;
    }

    private String getErrorMessageFromJsonString(String jsonString) {
        try {
            return new Gson().fromJson(jsonString, JsonObject.class).get("message").getAsString();
        } catch (Exception e) {
            return jsonString;
        }
    }
}