package com.allianz.view.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Assert;

import com.specmate.common.SpecmateException;
import com.specmate.persistency.event.EChangeKind;

public class RestClient {

	
	private Client restClient;
	private String restUrl;
	
	public RestClient(String restUrl){
		this.restClient=initializeClient();
		this.restUrl=restUrl;
	}

	private  Client initializeClient() {
		Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
		return client;
	}
	
	private  Future<Boolean> startEventListener(String url, Collection<Predicate<JSONObject>> eventPredicates) {
		final WebTarget eventTarget = restClient
				.target(restUrl + (StringUtils.isEmpty(url) ? "" : "/" + url) + "/_events");

		ExecutorService executor = Executors.newFixedThreadPool(1);
		Future<Boolean> future = executor.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() {
				if (eventPredicates.isEmpty()) {
					return true;
				}
				List<Predicate<JSONObject>> predicatesToCheck = new ArrayList<>(eventPredicates);
				final EventInput eventInput = eventTarget.request().get(EventInput.class);
				while (true) {
					if (predicatesToCheck.isEmpty()) {
						return true;
					}
					final InboundEvent inboundEvent = eventInput.read();
					if (inboundEvent == null) {
						return null;
					}
					JSONObject jsonObject = new JSONObject(new JSONTokener(inboundEvent.readData(String.class)));
					Iterator<Predicate<JSONObject>> predicateIterator = predicatesToCheck.iterator();
					while (predicateIterator.hasNext()) {
						if (predicateIterator.next().test(jsonObject)) {
							predicateIterator.remove();
						}
					}
				}
			}
		});
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// ingore
		}
		return future;
	}



	private Response rawGet(String url) {
		WebTarget getTarget = restClient.target(restUrl + url);
		Invocation.Builder invocationBuilder = getTarget.request();
		Response response = invocationBuilder.get();
		return response;
	}
	
	protected  RestResult<JSONObject> get(String url) {
		Response response = rawGet(url);
		String result = response.readEntity(String.class);
		if (response.getStatusInfo().getStatusCode() == Status.OK.getStatusCode()) {
			return new RestResult<JSONObject>(response,url,new JSONObject(new JSONTokener(result)));
		} else {
			return new RestResult<JSONObject>(response,url,null);
		}
	}
	
	protected  RestResult<JSONArray> getList(String url) {
		Response response = rawGet(url);
		String result = response.readEntity(String.class);
		if (response.getStatusInfo().getStatusCode() == Status.OK.getStatusCode()) {
			return new RestResult<JSONArray>(response,url,new JSONArray(new JSONTokener(result)));
		} else {
			return new RestResult<JSONArray>(response,url,null);
		}
	}

	public  RestResult<JSONObject> post(String url, JSONObject jsonObject) {
		WebTarget getTarget = restClient.target(restUrl + url);
		Invocation.Builder invocationBuilder = getTarget.request();
		Response response = invocationBuilder.post(Entity.json(jsonObject.toString()));
		return new RestResult<JSONObject>(response, url,null);
	}

	public  void put(String url,JSONObject objectJson) throws SpecmateException {
		WebTarget getTarget = restClient.target(restUrl + "/" + url);
		
		Invocation.Builder invocationBuilder = getTarget.request();
		Response response = invocationBuilder.put(Entity.json(objectJson.toString()));
		Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatusInfo().getStatusCode());
	}

	public  void delete(String url) {
		WebTarget getTarget = restClient.target(restUrl + "/" + url);

		Invocation.Builder invocationBuilder = getTarget.request();
		Response response = invocationBuilder.delete();
		Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatusInfo().getStatusCode());
	}


	
}
