package bassebombecraft.config;

import static bassebombecraft.ModConstants.DOWNLOAD_URL;
import static bassebombecraft.ModConstants.VERSION;
import static bassebombecraft.ModConstants.VERSION_URL;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.ibm.icu.impl.duration.impl.DataRecord.ESeparatorVariant;

/**
 * Helper class for handling versions
 */
public class VersionUtils {

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

			// extract JSON
			json = StringUtils.substringBetween(json, "BEGIN", "END");
			json = StringEscapeUtils.unescapeHtml4(json);
			
			// calculate version
			VersionInfo info = gson.fromJson(json, VersionInfo.class);
			String version = info.minecraftVersion + "-" + info.modVersion;

			// validate version info
			if (!VERSION.equalsIgnoreCase(version)) {
				logger.info("A newer version of BasseBombeCraft is available: " + version);
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
