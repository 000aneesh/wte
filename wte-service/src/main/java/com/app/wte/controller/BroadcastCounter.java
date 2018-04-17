package com.app.wte.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

@Component
public class BroadcastCounter {
	private Thread t;
	// private AtomicLong counter = new AtomicLong();
	private List<DeferredResult<String>> subscribedClient = Collections
			.synchronizedList(new ArrayList<DeferredResult<String>>());

	public BroadcastCounter() {
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					int index = -1;
					// counter.incrementAndGet();
					// if (counter.get() > Long.MAX_VALUE - 100) {
					// counter.set(0);
					// }
					try {
						Thread.sleep(10000);
						index++;
					} catch (InterruptedException e) {

					}
					synchronized (subscribedClient) {
						Iterator<DeferredResult<String>> it = subscribedClient.iterator();
						while (it.hasNext()) {
							DeferredResult<String> dr = it.next();
							dr.setResult("{key:value}");
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

	public void addSubscribed(DeferredResult<String> client) {
		synchronized (subscribedClient) {
			subscribedClient.add(client);
		}
	}

}
