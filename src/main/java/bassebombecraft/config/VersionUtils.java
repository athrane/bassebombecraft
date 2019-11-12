package bassebombecraft.config;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.Logger;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.ModConstants;
import bassebombecraft.config.http.HttpCallback;
import bassebombecraft.config.http.HttpRequestHandler;
import net.minecraftforge.versions.forge.ForgeVersion;
import net.minecraftforge.versions.mcp.MCPVersion;

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
	 * Start session.
	 * 
	 * @param uid user ID.
	 * 
	 * @throws Exception
	 */
	public static void startSession(String uid) throws Exception {

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
		executionService.execute(request, HTTP_CONTEXT, requestHandler, callBack);

		// Build the server URI together with the parameters
		action = "System info";
		postParameters = createSystemInfoParameters(uid, category, action);
		uriBuilder = new URIBuilder(ANALYTICS_URL);
		uriBuilder.addParameters(postParameters);

		// build request
		uri = uriBuilder.build();
		request = new HttpPost(uri);

		// post
		executionService.execute(request, HTTP_CONTEXT, requestHandler, callBack);
	}

	/**
	 * End session
	 * 
	 * @param uid user ID.
	 * @throws Exception
	 */
	public static void endSession(String uid) throws Exception {
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
		executionService.execute(request, HTTP_CONTEXT, requestHandler, callBack);
	}

	/**
	 * Post item usage event.
	 * 
	 * @param uid  user ID.
	 * @param item name.
	 * 
	 * @throws Exception
	 */
	public static void postItemUsageEvent(String uid, String itemName) throws Exception {

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
		executionService.execute(request, HTTP_CONTEXT, requestHandler, callBack);
	}

	/**
	 * start server session.
	 * 
	 * @param uid server ID.
	 * @throws Exception
	 */
	public static void startServerSession(String uid) throws Exception {
		// Build the server URI together with the parameters
		String category = NAME + "-" + VERSION;
		String action = "Start server session";
		List<NameValuePair> postParameters = createStartAppSessionParameters(uid, category, action);
		URIBuilder uriBuilder = new URIBuilder(ANALYTICS_URL);
		uriBuilder.addParameters(postParameters);

		// build request
		URI uri = uriBuilder.build();
		HttpPost request = new HttpPost(uri);

		// post
		executionService.execute(request, HTTP_CONTEXT, requestHandler, callBack);
	}

	/**
	 * End server session.
	 * 
	 * @param uid user ID.
	 * @throws Exception
	 */
	public static void endServerSession(String uid) throws Exception {
		// Build the server URI together with the parameters
		String category = NAME + "-" + VERSION;
		String action = "End server session";
		List<NameValuePair> postParameters = createEndAppSessionPerameters(uid, category, action);
		URIBuilder uriBuilder = new URIBuilder(ANALYTICS_URL);
		uriBuilder.addParameters(postParameters);

		// build request
		URI uri = uriBuilder.build();
		HttpPost request = new HttpPost(uri);

		// post
		executionService.execute(request, HTTP_CONTEXT, requestHandler, callBack);
	}

	/**
	 * Post exception.
	 * 
	 * @param uid user ID.
	 * @param e   exception to report.
	 * 
	 * @throws Exception
	 */
	public static void postException(String uid, Throwable e) throws Exception {

		// Build the server URI together with the parameters
		String category = NAME + "-" + VERSION;

		// get stack trace as string
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String description = sw.toString();
				
		List<NameValuePair> postParameters = createExceptionParameters(uid, category, description);
		URIBuilder uriBuilder = new URIBuilder(ANALYTICS_URL);
		uriBuilder.addParameters(postParameters);

		// build request
		URI uri = uriBuilder.build();
		HttpPost request = new HttpPost(uri);

		// post
		executionService.execute(request, HTTP_CONTEXT, requestHandler, callBack);
	}

	/**
	 * Post AI observation.
	 * 
	 * @param uid         user ID.
	 * @param type        observation type.
	 * @param observation observation data. *
	 * @throws Exception
	 */
	public static void postAiObservation(String uid, String type, String observation) throws Exception {

		// Build the server URI together with the parameters
		String category = NAME + "-" + VERSION;
		List<NameValuePair> postParameters = createPostAiEventParameters(uid, category, observation);
		URIBuilder uriBuilder = new URIBuilder(ANALYTICS_URL);
		uriBuilder.addParameters(postParameters);

		// build request
		URI uri = uriBuilder.build();
		HttpPost request = new HttpPost(uri);

		// post
		executionService.execute(request, HTTP_CONTEXT, requestHandler, callBack);
	}

	/**
	 * Create parameters for app session start.
	 * 
	 * @param uid      user ID.
	 * @param category event category.
	 * @param action   event action
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
	 * Create parameters for app system info.
	 * 
	 * @param uid      user ID.
	 * @param category event category.
	 * @param action   event action
	 * 
	 * @return parameters for system info.
	 */
	static List<NameValuePair> createSystemInfoParameters(String uid, String category, String action) {

		// get Minecraft version
		String mcVersion = getBassebombeCraft().getServer().getMinecraftVersion();		

		// get Forge version
		String forgeVersion = ForgeVersion.getVersion();

		// get MCP version
		String mcpVersion = MCPVersion.getMCPVersion();
		
		String userInfo = new StringBuilder()
				.append(System.getProperty("user.name")).append(";")
				.append(System.getProperty("os.name")).append(",").append(System.getProperty("os.version")).append(",").append(System.getProperty("os.arch")).append(";")
				.append(System.getProperty("java.version")).append(";")
				.append(mcVersion).append(";")
				.append(forgeVersion).append(";")
				.append(mcpVersion).append(";")								
				.toString();

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
		parameters.add(new BasicNameValuePair("el", userInfo));
		// parameters.add(new BasicNameValuePair("cd1", "System information"));
		// parameters.add(new BasicNameValuePair("cm1", userAgentStr));
		return parameters;
	}

	/**
	 * Create parameters for app session end.
	 * 
	 * @param uid      user ID.
	 * @param category event category.
	 * @param action   event action
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
	 * @param uid      user ID.
	 * @param category event category.
	 * @param action   event action
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
	 * Create parameters for exception.
	 * 
	 * @param uid         user ID.
	 * @param category    event category.
	 * @param description exception description.
	 */
	static List<NameValuePair> createExceptionParameters(String uid, String category, String description) {
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
		parameters.add(new BasicNameValuePair("ea", "Exception"));
		parameters.add(new BasicNameValuePair("el", description));
		return parameters;
	}

	/**
	 * Create parameters for event for AI.
	 * 
	 * @param uid      user ID.
	 * @param category event category.
	 * @param action   event action
	 * 
	 * @return parameters for session start.
	 */
	static List<NameValuePair> createPostAiEventParameters(String uid, String category, String observation) {
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
		parameters.add(new BasicNameValuePair("ea", "AI Observe"));
		parameters.add(new BasicNameValuePair("el", observation));
		return parameters;
	}

	/**
	 * Validate version.
	 */
	public static void validateVersion() {
		Logger logger = getBassebombeCraft().getLogger();
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
			getBassebombeCraft().reportException(e);
		} finally {
			if (is == null)
				return;
			try {
				is.close();
			} catch (IOException e) {
				logger.info("Failed to close connection for version validation due to exception:" + e.getMessage());
				getBassebombeCraft().reportException(e);
			}
		}
	}

}
