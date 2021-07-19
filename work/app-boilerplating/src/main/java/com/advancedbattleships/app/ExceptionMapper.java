package com.advancedbattleships.app;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.advancedbattleships.app.model.CustomErrorResponse;

@RestControllerAdvice
public class ExceptionMapper {
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public CustomErrorResponse mapException(Exception npEx, HttpServletRequest httpServletRequest) {
		npEx.printStackTrace();

		String uri = "not set";
		try {
			uri = httpServletRequest.getRequestURI();
		} catch(Exception exc) {
			uri = "unknown";
		}
		
		return new CustomErrorResponse(500, npEx.getClass().getSimpleName(), npEx.getMessage(), uri);
	}

}
