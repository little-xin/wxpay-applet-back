package com.pay.wxpayback.api;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.pay.wxpayback.constant.SystemConstant;
import com.pay.wxpayback.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Description controller层封装返回结果
 * @Author 小乌龟
 * @Date 2022/11/12 15:14
 */
@Slf4j
public class CommonController {
    /**
     *
     * @param br
     * @param <M>
     * @return
     */
    public <M> CommonResult<M> process(BindingResult br){
        if (br.hasErrors()){
            return errorResult(br);
        }else {
            return successResult();
        }
    }

    public <M> CommonResult<M> process(Supplier<M> sp){
        try {
            M val = sp.get();
            if (ObjectUtils.isEmpty(val)){
                return  CommonResult.failed();
            }
            return CommonResult.success(val);
        }catch (ApiException e) {
            log.error(this.getClass().getSimpleName(), e);
            log.error(this.getClass().getSimpleName(),e.getLocalizedMessage());
            return CommonResult.failed(e.getErrorCode());
        } catch (Exception e){
            log.error(this.getClass().getSimpleName(), e);
            log.error(this.getClass().getSimpleName(),e.getLocalizedMessage());
            return CommonResult.failed(e.getMessage());
        }
    }

    public <M> CommonResult<M> process(Supplier<M> sp, BindingResult br){
        if (br.hasErrors()){
            return errorResult(br);
        }else {
            return process(sp);
        }
    }

    private <M> CommonResult<M> errorResult(BindingResult br) {
        List<String> errs = br.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList());
        return (CommonResult<M>) CommonResult.failed(ResultCode.VALIDATE_FAILED,errs.get(0));
    }


    private <M> CommonResult<M>  successResult() {
        CommonResult<M> CommonResult = new CommonResult<>();
        CommonResult.setCode(ResultCode.SUCCESS.getCode());
        CommonResult.setMessage(ResultCode.SUCCESS.getMessage());
        CommonResult.setData(null);
        return CommonResult;
    }

}
