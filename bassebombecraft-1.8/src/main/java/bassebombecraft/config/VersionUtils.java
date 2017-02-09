package bassebombecraft.config;

import static bassebombecraft.ModConstants.ANALYTICS_URL;
import static bassebombecraft.ModConstants.DOWNLOAD_URL;
import static bassebombecraft.ModConstants.GA_API_VERSION;
import static bassebombecraft.ModConstants.GA_APP_ID;
import static bassebombecraft.ModConstants.GA_HITTYPE_EVENT;
import static bassebombecraft.ModConstants.GA_PROPERTY;
import static bassebombecraft.ModConstants.GA_SESSION_END;
import static bassebombecraft.ModConstants.GA_SESSION_START;
import static bassebombecraft.ModConstants.GA_SOURCE;
import static bassebombecraft.ModConstants.NAME;
import static bassebombecraft.ModConstants.VERSION;
import static bassebombecraft.ModConstants.VERSION_URL;
import static bassebombecraft.player.PlayerUtils.getPlayerUId;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.FutureRequestExecutionService;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpRequestFutureTask;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.Logger;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;

import bassebombecraft.ModConstants;
import bassebombecraft.config.http.HttpCallback;
import bassebombecraft.config.http.HttpRequestHandler;

/**
 * Helper class for handling versions
 */
public class VersionUtils {

	/**
	 * HTTP client.
	 */
	static HttpClient httpClient = HttpClientBuilder.create().build();

	/**
	 * HTTP context.
	 */
	static final HttpClientContext HTTP_CONTEXT = HttpClientContext.create();

	/**
	 * Executor service.
	 */
	static ExecutorService executorService = Executors.newFixedThreadPool(ModConstants.NUMBER_HTTP_THREADS);

	/**
	 * Future request service.
	 */
	static FutureRequestExecutionService executionService = new FutureRequestExecutionService(httpClient,
			executorService);

	/**
	 * HTTP callback instance.
	 */
	static HttpCallback callBack = new HttpCallback();

	/**
	 * HTTP response handler instance.
	 */
	static HttpRequestHandler requestHandler = new HttpRequestHandler();

	/**
	 * Initialize analytics.
	 * 
	 * @param logger
	 */
	public static void initializeAnalytics(Logger logger) {

		try {
			startSession(getPlayerUId());

		} catch (Exception ex) {
			logger.error("Usage initialization failed with: " + ex.getMessage());
		}
	}

	/**
	 * Shutdown analytics.
	 * 
	 * @param logger
	 */
	public static void shutdownAnalytics(Logger logger) {

		try {
			endSession(getPlayerUId());

		} catch (Exception ex) {
			// NO-OP

			// logger.error("Usage initialization failed with: " +
			// ex.getMessage());
		}
	}

	/**
	 * Post item usage.
	 * 
	 * @param itemName
	 *            item to register usage of.
	 */
	public static void postItemUsage(String itemName) {

		try {
			postItemUsageEvent(getPlayerUId(), itemName);

		} catch (Exception ex) {
			// NO-OP
			// Logger logger = BassebombeCraft.getLogger();
			// logger.error("Usage logging failed with: " + ex.getMessage());
		}

	}

	/**
	 * Start session.
	 * 
	 * @param uid
	 *            user ID.
	 * 
	 * @throws Exception
	 */
	static void startSession(String uid) throws Exception {

		// Build the server URI together with the parameters
		String category = NAME + "-" + VERSION;
		String action = "Start session";
		List<NameValuePair> postParameters = createStartAppSessionParameters(uid, category, action);
		URIBuilder uriBuilder = new URIBuilder(ANALYTICS_URL);
		uriBuilder.addParameters(postParameters);

		// build request
		URI uri = uriBuilder.build();
		HttpPost request = new HttpPost(uri);

		// post
		HttpRequestFutureTask<Boolean> task = executionService.execute(request, HTTP_CONTEXT, requestHandler, callBack);
	}

	/**
	 * End session
	 * 
	 * @param uid
	 *            user ID.
	 * @throws Exception
	 */
	static void endSession(String uid) throws Exception {
		// Build the server URI together with the parameters
		String category = NAME + "-" + VERSION;
		String action = "End session";
		List<NameValuePair> postParameters = createEndAppSessionPerameters(uid, category, action);
		URIBuilder uriBuilder = new URIBuilder(ANALYTICS_URL);
		uriBuilder.addParameters(postParameters);

		// build request
		URI uri = uriBuilder.build();
		HttpPost request = new HttpPost(uri);

		// post
		HttpRequestFutureTask<Boolean> task = executionService.execute(request, HTTP_CONTEXT, requestHandler, callBack);
	}

	/**
	 * Post item usage event.
	 * 
	 * @param uid
	 *            user ID.
	 * 
	 * @throws Exception
	 */
	static void postItemUsageEvent(String uid, String itemName) throws Exception {

		// Build the server URI together with the parameters
		String category = NAME + "-" + VERSION;
		String action = itemName;
		List<NameValuePair> postParameters = createPostItemUsageEventParameters(uid, category, action);
		URIBuilder uriBuilder = new URIBuilder(ANALYTICS_URL);
		uriBuilder.addParameters(postParameters);

		// build request
		URI uri = uriBuilder.build();
		HttpPost request = new HttpPost(uri);

		// post
		HttpRequestFutureTask<Boolean> task = executionService.execute(request, HTTP_CONTEXT, requestHandler, callBack);
	}

	/**
	 * Create parameters for app session start.
	 * 
	 * @param uid
	 *            user ID.
	 * @param category
	 *            event category.
	 * @param action
	 *            event action
	 * 
	 * @return parameters for session start.
	 */
	static List<NameValuePair> createStartAppSessionParameters(String uid, String category, String action) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("v", GA_API_VERSION));
		parameters.add(new BasicNameValuePair("t", GA_HITTYPE_EVENT));
		parameters.add(new BasicNameValuePair("tid", GA_PROPERTY));
		parameters.add(new BasicNameValuePair("ds", GA_SOURCE));
		parameters.add(new BasicNameValuePair("an", NAME));
		parameters.add(new BasicNameValuePair("aid", GA_APP_ID));
		parameters.add(new BasicNameValuePair("av", VERSION));
		parameters.add(new BasicNameValuePair("cid", uid));
		parameters.add(new BasicNameValuePair("uid", uid));
		parameters.add(new BasicNameValuePair("ec", category));
		parameters.add(new BasicNameValuePair("ea", action));
		parameters.add(new BasicNameValuePair("el", uid));		
		parameters.add(new BasicNameValuePair("sc", GA_SESSION_START));
		return parameters;
	}

	/**
	 * Create parameters for app session end.
	 * 
	 * @param uid
	 *            user ID.
	 * @param category
	 *            event category.
	 * @param action
	 *            event action
	 * @return parameters for session start.
	 */
	static List<NameValuePair> createEndAppSessionPerameters(String uid, String category, String action) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("v", GA_API_VERSION));
		parameters.add(new BasicNameValuePair("t", GA_HITTYPE_EVENT));
		parameters.add(new BasicNameValuePair("tid", GA_PROPERTY));
		parameters.add(new BasicNameValuePair("ds", GA_SOURCE));
		parameters.add(new BasicNameValuePair("an", NAME));
		parameters.add(new BasicNameValuePair("aid", GA_APP_ID));
		parameters.add(new BasicNameValuePair("av", VERSION));		
		parameters.add(new BasicNameValuePair("cid", uid));
		parameters.add(new BasicNameValuePair("uid", uid));
		parameters.add(new BasicNameValuePair("ec", category));
		parameters.add(new BasicNameValuePair("ea", action));
		parameters.add(new BasicNameValuePair("el", uid));		
		parameters.add(new BasicNameValuePair("sc", GA_SESSION_END));
		return parameters;
	}

	/**
	 * Create parameters for event based item usage.
	 * 
	 * @param uid
	 *            user ID.
	 * @param category
	 *            event category.
	 * @param action
	 *            event action
	 * 
	 * @return parameters for session start.
	 */
	static List<NameValuePair> createPostItemUsageEventParameters(String uid, String category, String action) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("v", GA_API_VERSION));
		parameters.add(new BasicNameValuePair("t", GA_HITTYPE_EVENT));
		parameters.add(new BasicNameValuePair("tid", GA_PROPERTY));
		parameters.add(new BasicNameValuePair("ds", GA_SOURCE));
		parameters.add(new BasicNameValuePair("an", NAME));
		parameters.add(new BasicNameValuePair("aid", GA_APP_ID));
		parameters.add(new BasicNameValuePair("av", VERSION));				
		parameters.add(new BasicNameValuePair("cid", uid));
		parameters.add(new BasicNameValuePair("uid", uid));
		parameters.add(new BasicNameValuePair("ec", category));
		parameters.add(new BasicNameValuePair("ea", action));
		parameters.add(new BasicNameValuePair("el", uid));
		return parameters;
	}

	/**
	 * Validate version.
	 * 
	 * @param logger
	 *            mod logger.
	 */
	public static void validateVersion(Logger logger) {
		Gson gson = new Gson();

		// declare stream
		InputStream is = null;

		try {

			// get version info from server
			URL url = new URL(VERSION_URL);
			is = url.openStream();
			String json = new String(ByteStreams.toByteArray(is), "UTF-8");
			is.close();

			// calculate version
			VersionInfo info = gson.fromJson(json, VersionInfo.class);
			String version = info.minecraftVersion + "-" + info.modVersion;

			logger.info("Starting version check at: " + VERSION_URL);

			// validate version info
			if (!VERSION.equalsIgnoreCase(version)) {
				logger.info("A newer version of BasseBombeCraft is available.");
				logger.info("The newest version is: " + version);
				logger.info("The new version can be downloaded from: " + DOWNLOAD_URL);
				return;
			}

			logger.info("The most current version of BasseBombeCraft is used: " + version);

		} catch (Exception e) {
			logger.info("Failed to validate version due to exception:" + e.getMessage());
		} finally {
			if (is == null)
				return;
			try {
				is.close();
			} catch (IOException e) {
				logger.info("Failed to close connection for version validation due to exception:" + e.getMessage());
			}
		}
	}

}
