package com.app.wte.controller;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class TestPollConroller {
	
		private static final Object TIMEOUT_RESULT = null;
		@Autowired
		BroadcastCounter broadcastCounter;
	
	  @RequestMapping(value="/pollBroadcast")
	  @ResponseBody
	  public DeferredResult<String> ajaxReply(final HttpServletRequest request, final HttpServletResponse response,
	                                                            ModelMap mm) throws Exception {
	         final DeferredResult<String> dr = new DeferredResult<String>(
	                             TimeUnit.MINUTES.toMillis(1), TIMEOUT_RESULT);
	         broadcastCounter.addSubscribed(dr);
	         System.out.println("Result: "+dr.getResult());
	         return dr;
	   }

}
