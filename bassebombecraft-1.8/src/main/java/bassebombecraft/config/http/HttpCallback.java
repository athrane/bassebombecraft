package bassebombecraft.config.http;

import org.apache.http.concurrent.FutureCallback;
import org.apache.logging.log4j.Logger;

import bassebombecraft.BassebombeCraft;

/**
 * Async HTTP client callback object.
 */
public class HttpCallback implements FutureCallback<Boolean> {

    public void failed(final Exception ex) {
		//Logger logger = BassebombeCraft.getLogger();
    	//logger.error("Http Post failed with exception: "+ex.getMessage());	    	
    }

    public void completed(final Boolean result) {
        // do something
    }

    public void cancelled() {
        // do something
    }	
}
