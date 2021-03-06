package com.baofeng.mj.net;

import android.content.Context;

import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fei-ke on 2015/1/20.
 */
public class AsyncHttpClient extends com.loopj.android.http.AsyncHttpClient {

    public RequestHandle get(Context context, String url, RequestParams params,
                             boolean isCacheAble, String cacheKey, File cacheDir, int cacheTime,
                             TextHttpResponseHandler responseHandler) {
        if (isCacheAble) {
            if(context != null) {
                if (isCacheAble) {
                    cacheDir = context.getExternalCacheDir();
                }
                if (cacheDir == null) {
                    cacheDir = context.getCacheDir();
                }
            }
        }
        return sendRequest((DefaultHttpClient) getHttpClient(), getHttpContext(),
                new HttpGet(getUrlWithQueryString(isUrlEncodingEnabled(), url, params)), null,
                responseHandler, context, isCacheAble, cacheKey, cacheDir, cacheTime);
    }

    public RequestHandle get(Context context, String url, RequestParams params,
                             boolean isCacheAble, File cacheDir, int cacheTime,
                             TextHttpResponseHandler responseHandler) {
        return get(context, url, params, isCacheAble, null, cacheDir, cacheTime, responseHandler);
    }

    public RequestHandle get(Context context, String url, RequestParams params,
                             boolean isCacheAble, File cacheDir, TextHttpResponseHandler responseHandler) {
        return get(context, url, params, isCacheAble, cacheDir, 0, responseHandler);
    }

    public RequestHandle get(Context context, String url, boolean isCacheAble, File cacheDir,
                             TextHttpResponseHandler responseHandler) {
        return get(context, url, null, isCacheAble, cacheDir, responseHandler);
    }

    public RequestHandle get(Context context, String url, RequestParams params,
                             boolean isCacheAble,
                             TextHttpResponseHandler responseHandler) {
        return get(context, url, params, isCacheAble, null, 0, responseHandler);
    }

    public RequestHandle get(Context context, String url, boolean isCacheAble,
                             TextHttpResponseHandler responseHandler) {
        return get(context, url, null, isCacheAble, responseHandler);
    }

    /**
     * @param context
     * @param url
     * @param params
     * @param isCacheAble     是否进行缓存
     * @param cacheKey        缓存的key
     * @param responseHandler
     * @return
     */
    public RequestHandle get(Context context, String url, RequestParams params,
                             boolean isCacheAble, String cacheKey,
                             TextHttpResponseHandler responseHandler) {
        return get(context, url, params, isCacheAble, cacheKey, null, 0, responseHandler);
    }

    /**
     * @param context
     * @param url
     * @param isCacheAble     是否进行缓存
     * @param cacheKey        缓存的key
     * @param responseHandler
     * @return
     */
    public RequestHandle get(Context context, String url, boolean isCacheAble, String cacheKey,
                             TextHttpResponseHandler responseHandler) {
        return get(context, url, null, isCacheAble, cacheKey, responseHandler);
    }

    /**
     * Puts a new request in queue as a new thread in pool to be executed
     *
     * @param client          HttpClient to be used for request, can differ in single requests
     * @param contentType     MIME body type, for POST and PUT requests, may be null
     * @param context         Context of Android application, to hold the reference of request
     * @param httpContext     HttpContext in which the request will be executed
     * @param responseHandler ResponseHandler or its subclass to put the response into
     * @param uriRequest      instance of HttpUriRequest, which means it must be of HttpDelete,
     *                        HttpPost, HttpGet, HttpPut, etc.
     * @return RequestHandle of future request process
     */
    protected RequestHandle sendRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType, TextHttpResponseHandler responseHandler, Context context
            , boolean isCacheAble, String cacheKey, File cacheDir, int cacheTime) {
        if (uriRequest == null) {
            throw new IllegalArgumentException("HttpUriRequest must not be null");
        }

        if (responseHandler == null) {
            throw new IllegalArgumentException("ResponseHandler must not be null");
        }

        if (responseHandler.getUseSynchronousMode()) {
            throw new IllegalArgumentException("Synchronous ResponseHandler used in AsyncHttpClient. You should create your response handler in a looper thread or use SyncHttpClient instead.");
        }

        if (contentType != null) {
            uriRequest.setHeader(HEADER_CONTENT_TYPE, contentType);
        }

        responseHandler.setRequestHeaders(uriRequest.getAllHeaders());
        responseHandler.setRequestURI(uriRequest.getURI());

        AsyncHttpRequest request = newAsyncHttpRequest(client, httpContext, uriRequest,
                contentType, responseHandler, context, isCacheAble, cacheKey, cacheDir, cacheTime);
        getThreadPool().submit(request);
        RequestHandle requestHandle = new RequestHandle(request);

        if (context != null) {
            // Add request to request map
            List<RequestHandle> requestList = requestMap.get(context);
            if (requestList == null) {
                requestList = new LinkedList<RequestHandle>();
                requestMap.put(context, requestList);
            }

            //if (responseHandler instanceof RangeFileAsyncHttpResponseHandler)
            //    ((RangeFileAsyncHttpResponseHandler) responseHandler).updateRequestHeaders(uriRequest);

            requestList.add(requestHandle);

            Iterator<RequestHandle> iterator = requestList.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().shouldBeGarbageCollected()) {
                    iterator.remove();
                }
            }
        }

        return requestHandle;
    }

    protected AsyncHttpRequest newAsyncHttpRequest(DefaultHttpClient client,
                                                   HttpContext httpContext, HttpUriRequest uriRequest,
                                                   String contentType, TextHttpResponseHandler responseHandler, Context context,
                                                   boolean isCacheAble, String cacheKey, File cacheDir, int cacheTime) {
        return new AsyncHttpRequest(client, httpContext, uriRequest, responseHandler, isCacheAble,
                cacheKey, cacheDir, cacheTime);
    }
}
