package bassebombecraft.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import bassebombecraft.ModConstants;

/**
 * Unit tests for {@link VersionUtils} class.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("VersionUtils Tests")
class VersionUtilsTest {

	private static final String TEST_UID = "test-user-123";
	private static final String TEST_CATEGORY = "TestCategory";
	private static final String TEST_ACTION = "TestAction";
	private static final String TEST_OBSERVATION = "TestObservation";
	private static final String TEST_DESCRIPTION = "Test exception description";

	@BeforeEach
	void setUp() {
		// Setup common test data
	}

	@Test
	@DisplayName("Test createStartAppSessionParameters returns correct parameters")
	void testCreateStartAppSessionParameters() {
		// Act
		List<NameValuePair> parameters = VersionUtils.createStartAppSessionParameters(
			TEST_UID, TEST_CATEGORY, TEST_ACTION);

		// Assert
		assertNotNull(parameters, "Parameters should not be null");
		assertEquals(13, parameters.size(), "Should have 13 parameters");
		
		// Verify key parameters
		assertParameter(parameters, "v", ModConstants.GA_API_VERSION);
		assertParameter(parameters, "t", ModConstants.GA_HITTYPE_EVENT);
		assertParameter(parameters, "tid", ModConstants.GA_PROPERTY);
		assertParameter(parameters, "ds", ModConstants.GA_SOURCE);
		assertParameter(parameters, "an", ModConstants.NAME);
		assertParameter(parameters, "aid", ModConstants.GA_APP_ID);
		assertParameter(parameters, "av", ModConstants.VERSION);
		assertParameter(parameters, "cid", TEST_UID);
		assertParameter(parameters, "uid", TEST_UID);
		assertParameter(parameters, "ec", TEST_CATEGORY);
		assertParameter(parameters, "ea", TEST_ACTION);
		assertParameter(parameters, "el", TEST_UID);
		assertParameter(parameters, "sc", ModConstants.GA_SESSION_START);
	}

	@Test
	@DisplayName("Test createEndAppSessionPerameters returns correct parameters")
	void testCreateEndAppSessionPerameters() {
		// Act
		List<NameValuePair> parameters = VersionUtils.createEndAppSessionPerameters(
			TEST_UID, TEST_CATEGORY, TEST_ACTION);

		// Assert
		assertNotNull(parameters, "Parameters should not be null");
		assertEquals(13, parameters.size(), "Should have 13 parameters");
		
		// Verify session end parameter
		assertParameter(parameters, "sc", ModConstants.GA_SESSION_END);
		assertParameter(parameters, "v", ModConstants.GA_API_VERSION);
		assertParameter(parameters, "t", ModConstants.GA_HITTYPE_EVENT);
		assertParameter(parameters, "cid", TEST_UID);
		assertParameter(parameters, "uid", TEST_UID);
		assertParameter(parameters, "ec", TEST_CATEGORY);
		assertParameter(parameters, "ea", TEST_ACTION);
	}

	@Test
	@DisplayName("Test createPostItemUsageEventParameters returns correct parameters")
	void testCreatePostItemUsageEventParameters() {
		// Act
		List<NameValuePair> parameters = VersionUtils.createPostItemUsageEventParameters(
			TEST_UID, TEST_CATEGORY, TEST_ACTION);

		// Assert
		assertNotNull(parameters, "Parameters should not be null");
		assertEquals(12, parameters.size(), "Should have 12 parameters");
		
		// Verify key parameters
		assertParameter(parameters, "v", ModConstants.GA_API_VERSION);
		assertParameter(parameters, "t", ModConstants.GA_HITTYPE_EVENT);
		assertParameter(parameters, "tid", ModConstants.GA_PROPERTY);
		assertParameter(parameters, "cid", TEST_UID);
		assertParameter(parameters, "uid", TEST_UID);
		assertParameter(parameters, "ec", TEST_CATEGORY);
		assertParameter(parameters, "ea", TEST_ACTION);
		assertParameter(parameters, "el", TEST_UID);
	}

	// Note: createSystemInfoParameters test is skipped because it depends on Forge/Minecraft initialization
	// which is not available in unit test environment

	@Test
	@DisplayName("Test createExceptionEventParameters returns correct parameters")
	void testCreateExceptionEventParameters() {
		// Act
		List<NameValuePair> parameters = VersionUtils.createExceptionEventParameters(
			TEST_UID, TEST_CATEGORY, TEST_DESCRIPTION);

		// Assert
		assertNotNull(parameters, "Parameters should not be null");
		assertEquals(12, parameters.size(), "Should have 12 parameters");
		
		// Verify key parameters
		assertParameter(parameters, "v", ModConstants.GA_API_VERSION);
		assertParameter(parameters, "t", ModConstants.GA_HITTYPE_EVENT);
		assertParameter(parameters, "ea", "Exception");
		assertParameter(parameters, "el", TEST_DESCRIPTION);
	}

	@Test
	@DisplayName("Test createExceptionParameters returns correct parameters")
	void testCreateExceptionParameters() {
		// Act
		List<NameValuePair> parameters = VersionUtils.createExceptionParameters(
			TEST_UID, TEST_CATEGORY, TEST_DESCRIPTION);

		// Assert
		assertNotNull(parameters, "Parameters should not be null");
		assertEquals(11, parameters.size(), "Should have 11 parameters");
		
		// Verify key parameters
		assertParameter(parameters, "v", ModConstants.GA_API_VERSION);
		assertParameter(parameters, "t", ModConstants.GA_HITTYPE_EXCEPTION);
		assertParameter(parameters, "tid", ModConstants.GA_PROPERTY);
		assertParameter(parameters, "cid", TEST_UID);
		assertParameter(parameters, "uid", TEST_UID);
		assertParameter(parameters, "exd", TEST_DESCRIPTION);
		assertParameter(parameters, "exf", "0");
	}

	@Test
	@DisplayName("Test createPostAiEventParameters returns correct parameters")
	void testCreatePostAiEventParameters() {
		// Act
		List<NameValuePair> parameters = VersionUtils.createPostAiEventParameters(
			TEST_UID, TEST_CATEGORY, TEST_OBSERVATION);

		// Assert
		assertNotNull(parameters, "Parameters should not be null");
		assertEquals(12, parameters.size(), "Should have 12 parameters");
		
		// Verify key parameters
		assertParameter(parameters, "v", ModConstants.GA_API_VERSION);
		assertParameter(parameters, "t", ModConstants.GA_HITTYPE_EVENT);
		assertParameter(parameters, "ea", "AI Observe");
		assertParameter(parameters, "el", TEST_OBSERVATION);
		assertParameter(parameters, "cid", TEST_UID);
		assertParameter(parameters, "uid", TEST_UID);
		assertParameter(parameters, "ec", TEST_CATEGORY);
	}

	// Note: createUserInfo tests are skipped because they depend on Forge/Minecraft initialization
	// which is not available in unit test environment (BassebombeCraft.getBassebombeCraft())

	@Test
	@DisplayName("Test getJvmArgs returns non-empty string")
	void testGetJvmArgs() {
		// Act
		String jvmArgs = VersionUtils.getJvmArgs();

		// Assert
		assertNotNull(jvmArgs, "JVM args should not be null");
		assertFalse(jvmArgs.isEmpty(), "JVM args should not be empty");
		assertTrue(jvmArgs.startsWith("JVM args:"), "JVM args should start with prefix");
	}

	@Test
	@DisplayName("Test parameter lists contain correct basic name-value pairs")
	void testParameterListStructure() {
		// Arrange
		List<NameValuePair> parameters = VersionUtils.createStartAppSessionParameters(
			TEST_UID, TEST_CATEGORY, TEST_ACTION);

		// Act & Assert
		for (NameValuePair pair : parameters) {
			assertNotNull(pair.getName(), "Parameter name should not be null");
			assertNotNull(pair.getValue(), "Parameter value should not be null");
			assertTrue(pair instanceof BasicNameValuePair, 
				"Parameter should be instance of BasicNameValuePair");
		}
	}

	@Test
	@DisplayName("Test createStartAppSessionParameters with empty category")
	void testCreateStartAppSessionParametersWithEmptyCategory() {
		// Act
		List<NameValuePair> parameters = VersionUtils.createStartAppSessionParameters(
			TEST_UID, "", TEST_ACTION);

		// Assert
		assertNotNull(parameters, "Parameters should not be null");
		assertEquals(13, parameters.size(), "Should have 13 parameters");
		assertParameter(parameters, "ec", "");
	}

	@Test
	@DisplayName("Test version constants are used correctly")
	void testVersionConstantsUsage() {
		// Act
		List<NameValuePair> parameters = VersionUtils.createStartAppSessionParameters(
			TEST_UID, TEST_CATEGORY, TEST_ACTION);

		// Assert - verify that ModConstants values are used
		assertParameter(parameters, "an", ModConstants.NAME);
		assertParameter(parameters, "av", ModConstants.VERSION);
		assertParameter(parameters, "aid", ModConstants.GA_APP_ID);
		assertEquals("BasseBombeCraft", ModConstants.NAME, "Mod name should match");
		assertTrue(ModConstants.VERSION.contains("1.17.1-3.1"), "Version should contain expected value");
	}

	@Test
	@DisplayName("Test analytics constants are properly defined")
	void testAnalyticsConstants() {
		// Assert
		assertEquals("1", ModConstants.GA_API_VERSION);
		assertEquals("event", ModConstants.GA_HITTYPE_EVENT);
		assertEquals("exception", ModConstants.GA_HITTYPE_EXCEPTION);
		assertEquals("start", ModConstants.GA_SESSION_START);
		assertEquals("end", ModConstants.GA_SESSION_END);
		assertEquals("app", ModConstants.GA_SOURCE);
		assertEquals("bassebombecraft", ModConstants.GA_APP_ID);
	}

	/**
	 * Helper method to assert a parameter exists with expected value.
	 */
	private void assertParameter(List<NameValuePair> parameters, String name, String expectedValue) {
		String actualValue = getParameterValue(parameters, name);
		assertEquals(expectedValue, actualValue, 
			String.format("Parameter '%s' should have value '%s'", name, expectedValue));
	}

	/**
	 * Helper method to get parameter value by name.
	 */
	private String getParameterValue(List<NameValuePair> parameters, String name) {
		return parameters.stream()
			.filter(p -> name.equals(p.getName()))
			.map(NameValuePair::getValue)
			.findFirst()
			.orElse(null);
	}
}
