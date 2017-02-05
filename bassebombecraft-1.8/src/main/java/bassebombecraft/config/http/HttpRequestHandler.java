package bassebombecraft.config.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

/**
 * HTTP response handler, returns true if HTTP response contains HTTP 200.
 */
public class HttpRequestHandler implements ResponseHandler<Boolean> {

	public Boolean handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
        return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
    }
	
}
