package com.app.wte.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.app.wte.model.DummyResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class PollController {

	private static final Object TIMEOUT_RESULT = null;
	@Autowired
	BroadcastCounter broadcastCounter;
	
	@Autowired
	DummyBroadcastCounter dummyBroadcastCounter;

	int i = 0;
	
	@ApiOperation(value = "Poll message")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Server error") })
	@GetMapping(path = "/pollBroadcast")
	public DeferredResult<String> ajaxReply(final HttpServletRequest request, final HttpServletResponse response,
			ModelMap mm) throws Exception {
		final DeferredResult<String> dr = new DeferredResult<String>(TimeUnit.MINUTES.toMillis(1), TIMEOUT_RESULT);
		broadcastCounter.addSubscribed(dr);
		System.out.println("Result: " + dr.getResult());
		return dr;
	}
	
	@ApiOperation(value = "Poll message2")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Server error") })
	@GetMapping(path = "/pollBroadcastStr")
	public String ajaxStringReply() throws Exception {
		i++;
		System.out.println("Result i: " + i);
		return "{ \"data\" : "+ i  +"}";
	}
	
	@GetMapping(path = "/pollBroadcastFuture")
	public Future<String> get(@RequestParam String input) throws InterruptedException {
	//  CompletableFuture<String> future = new CompletableFuture<>();
		System.out.println("in the pollBroadcastFuture()");
		i++;
		Thread.sleep(3000);
		System.out.println("after sleep the pollBroadcastFuture()");
	  return CompletableFuture.supplyAsync(() -> "{ \"data\" : "+ i  +"}");
	}
	
	@GetMapping(path = "/dummyStatus")
	public DeferredResult<DummyResponse> ajaxDummyResponse(@RequestParam(name="process") String process) throws Exception {
		final DeferredResult<DummyResponse> dr = new DeferredResult<DummyResponse>(TimeUnit.MINUTES.toMillis(1), TIMEOUT_RESULT);
		dummyBroadcastCounter.addSubscribed(dr, process);
		return dr;
	}

}
