/**
 * 
 */
package com.vagnnermartins.adbelem.util;

import com.vagnnermartins.adbelem.R;

import org.apache.http.HttpStatus;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * @author vagnnermartins
 *
 */
public class HTTPUtil {
	private static final int UM_MINUTO = 120000;
	public static final int ERR_INTERNET_DISCONNECTED = 106;
	
	public static HttpParams setTimeout() {
		return setTimeout(UM_MINUTO);
	}
	
	public static HttpParams setTimeout(int tempo) {
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = tempo;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		int timeoutSocket = tempo;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		
		return httpParameters;
	}
	
	public static int obterMensagemException(int httpStatus){
		int retorno = 0;
		switch (httpStatus) {
		case HttpStatus.SC_BAD_REQUEST:
			retorno = R.string.exception_erro_400_bad_request;
			break;
		case HttpStatus.SC_UNAUTHORIZED:
			retorno = R.string.exception_erro_401_unauthorized;
			break;
		case HttpStatus.SC_INTERNAL_SERVER_ERROR:
			retorno = R.string.exception_erro_500_internal_server_error;
			break;
		case HttpStatus.SC_BAD_GATEWAY:
			retorno = R.string.exception_erro_502_bad_gateway;
			break;
		case HttpStatus.SC_REQUEST_TIMEOUT:
			retorno = R.string.exception_erro_502_bad_gateway;
			break;	
		case ERR_INTERNET_DISCONNECTED:
			retorno = R.string.exception_erro_err_internet_disconnected;
			break;
		case HttpStatus.SC_GATEWAY_TIMEOUT:
			retorno = R.string.exception_erro_504_gateway_time_out;
			break;
		default:
			retorno = R.string.exception_erro_desconhecido;
			break;
		}
		return retorno;
	}
}
