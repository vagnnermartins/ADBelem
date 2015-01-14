package com.vagnnermartins.adbelem.util;

import java.io.ByteArrayOutputStream;
import java.net.SocketTimeoutException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class ServiceUtil {

	public static String requestGet(String path, Map<String, String> headers)
			throws SocketTimeoutException, Exception {
		String retorno = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(path);
            for (Map.Entry<String, String> item : headers.entrySet()){
                httpGet.addHeader(item.getKey(), item.getValue());
            }
			httpGet.setParams(HTTPUtil.setTimeout());

			HttpResponse response = httpclient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();

			// se o status da chamada for 200
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				retorno = out.toString();
			} else {
				// fecha conexoes
				response.getEntity().getContent().close();
				throw new Exception();
			}
		} catch (SocketTimeoutException e) {
			throw new Exception();
		}
		return retorno;
	}
}
