import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Testing {

	private String baseUrl;

	@BeforeClass
	public void setup() {
		baseUrl = "https://reqres.in/api";
	}

	// Utility method for sending requests
	private Response sendRequest(String method, String endpoint, String body) {
		switch (method.toUpperCase()) {
		case "GET":
			return RestAssured.get(baseUrl + endpoint);
		case "POST":
			return RestAssured.given().header("Content-Type", "application/json").body(body).post(baseUrl + endpoint);
		case "PUT":
			return RestAssured.given().header("Content-Type", "application/json").body(body).put(baseUrl + endpoint);
		case "DELETE":
			return RestAssured.delete(baseUrl + endpoint);
		default:
			throw new IllegalArgumentException("Invalid HTTP method: " + method);
		}
	}

	// Test GET endpoint
	@Test
	public void testGetUser() {
		Response response = sendRequest("GET", "/users/2", null);

		// Assertions
		Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch");
		Assert.assertTrue(response.getBody().asString().contains("data"), "Response body missing 'data'");
		System.out.println("GET Response: " + response.getBody().asString());
	}

	// Test POST endpoint
	@Test
	public void testCreateUser() {
		String jsonBody = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";
		Response response = sendRequest("POST", "/users", jsonBody);

		// Assertions
		Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch");
		Assert.assertTrue(response.getBody().asString().contains("id"), "Response body missing 'id'");
		System.out.println("POST Response: " + response.getBody().asString());
	}

	// Test PUT endpoint
	@Test
	public void testUpdateUser() {
		String jsonBody = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";
		Response response = sendRequest("PUT", "/users/2", jsonBody);

		// Assertions
		Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch");
		Assert.assertTrue(response.getBody().asString().contains("updatedAt"), "Response body missing 'updatedAt'");
		System.out.println("PUT Response: " + response.getBody().asString());
	}

	// Test DELETE endpoint
	@Test
	public void testDeleteUser() {
		Response response = sendRequest("DELETE", "/users/2", null);

		// Assertions
		Assert.assertEquals(response.getStatusCode(), 204, "Status code mismatch");
		Assert.assertTrue(response.getBody().asString().isEmpty(), "Response body should be empty for DELETE");
		System.out.println("DELETE Response Code: " + response.getStatusCode());
	}
}
