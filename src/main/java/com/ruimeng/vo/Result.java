package com.ruimeng.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局统一返回结果类
 */
@Data
public class Result {

	private Boolean success; //是否成功

	private Integer code; //状态码

	private String message;// 返回消息

	private Map<String, Object> data = new HashMap<String, Object>(); //返回数据

	private Result(){}

	public static Result ok(){
		Result result = new Result();
		result.setSuccess(ResultCodeEnum.SUCCESS.getSuccess());
		result.setCode(ResultCodeEnum.SUCCESS.getCode());
		result.setMessage(ResultCodeEnum.SUCCESS.getMessage());
		return result;
	}

	public static Result error(){
		Result result = new Result();
		result.setSuccess(ResultCodeEnum.UNKNOWN_REASON.getSuccess());
		result.setCode(ResultCodeEnum.UNKNOWN_REASON.getCode());
		result.setMessage(ResultCodeEnum.UNKNOWN_REASON.getMessage());
		return result;
	}

	public static Result setResult(ResultCodeEnum resultCodeEnum){
		Result result = new Result();
		result.setSuccess(resultCodeEnum.getSuccess());
		result.setCode(resultCodeEnum.getCode());
		result.setMessage(resultCodeEnum.getMessage());
		return result;
	}

	public Result success(Boolean success){
		this.setSuccess(success);
		return this;
	}

	public Result message(String message){
		this.setMessage(message);
		return this;
	}

	public Result code(Integer code){
		this.setCode(code);
		return this;
	}

	public Result data(String key, Object value){
		this.data.put(key, value);
		return this;
	}

	public Result data(Map<String, Object> map){
		this.setData(map);
		return this;
	}
}