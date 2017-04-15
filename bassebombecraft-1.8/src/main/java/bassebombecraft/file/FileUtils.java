package bassebombecraft.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Helper functions for using files.
 */
public class FileUtils {

	/**
	 * Save text as JSON to disk.
	 * 
	 * @param json
	 *            JSON model of the captured content.
	 */
	public static void saveJsonFile(String json) {
		try {

			String userHome = System.getProperty("java.io.tmpdir");
			File userHomeDir = new File(userHome);
			File contentFile = new File(userHomeDir, RandomStringUtils.randomAlphabetic(4).toLowerCase() + ".json");
			FileWriter writer = new FileWriter(contentFile);
			writer.write(json);
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save text as JSON to disk.
	 * 
	 * @param json
	 *            JSON model of the captured content.
	 * @param file
	 *            path tp file.
	 */
	public static void saveJsonFile(String json, File file) {
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(json);
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
