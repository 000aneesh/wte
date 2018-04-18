package com.app.wte.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import com.app.wte.model.DummyResponse;

@Component
public class DummyBroadcastCounter {
	private Thread t;
	// private AtomicLong counter = new AtomicLong();
	private List<DeferredResult<DummyResponse>> subscribedClient = Collections
			.synchronizedList(new ArrayList<DeferredResult<DummyResponse>>());
	int index;
	public DummyBroadcastCounter() {
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					
					// counter.incrementAndGet();
					// if (counter.get() > Long.MAX_VALUE - 100) {
					// counter.set(0);
					// }
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {

					}
					synchronized (subscribedClient) {
						Iterator<DeferredResult<DummyResponse>> it = subscribedClient.iterator();
						while (it.hasNext()) {
							DeferredResult<DummyResponse> dr = it.next();
							dr.setResult(getDummyRespList().get(index));
							it.remove();
						}
					}
				}
			}
		});
		t.setDaemon(true);
		t.setName("BroadcastDeferredThread");
		t.start();
	}

	public void addSubscribed(DeferredResult<DummyResponse> client, String process) {
		if(process != null && process.equals("fileGeneration")) {
			index = -1;
		}
		index++;
		synchronized (subscribedClient) {
			subscribedClient.add(client);
		}
	}
	
	private List<DummyResponse> getDummyRespList(){
		List<DummyResponse> respList = new ArrayList<DummyResponse>();
		DummyResponse dummyResp;
		
		/*dummyResp = new DummyResponse();
		dummyResp.setProcess("fileUpload");
		dummyResp.setStatus("success");
		respList.add(dummyResp);*/
		
		dummyResp = new DummyResponse();
		dummyResp.setProcess("fileGeneration");
		dummyResp.setStatus("success");
		respList.add(dummyResp);
		
		dummyResp = new DummyResponse();
		dummyResp.setProcess("fileCopy");
		dummyResp.setStatus("success");
		respList.add(dummyResp);
		
		dummyResp = new DummyResponse();
		dummyResp.setProcess("process1");
		dummyResp.setStatus("success");
		respList.add(dummyResp);
		
		dummyResp = new DummyResponse();
		dummyResp.setProcess("process2");
		dummyResp.setStatus("success");
		respList.add(dummyResp);
		
		dummyResp = new DummyResponse();
		dummyResp.setProcess("process3");
		dummyResp.setStatus("success");
		respList.add(dummyResp);
		
		return respList;
		
	}

}
