package GroupKTAPIFramework.GroupKTAPIFramework;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APIExecutor {
	public RequestSpecification httpRequest;

	@BeforeTest
	public void httpRequest() {
		// baseURI is used to specify the RESTful web service
		RestAssured.baseURI = "http://services.groupkt.com/country";

		// Get the RequestSpecification of the request that you want to sent to the
		// server.
		httpRequest = RestAssured.given();
		Log.info("HttpRequest is created");
	}

	public Response getAllRequest(RequestSpecification httpRequest, String code) {
		// Send a request to the server by specifying the method Type and the method
		// URL. This will return the Response from the server.
		Response response = httpRequest.request(Method.GET, "/get/" + code);
		Log.info("###get all request is executed");
		return response;
	}

	public Response getCountryRequest(RequestSpecification httpRequest, String code) {
		// Send a request to the server by specifying the method Type and the method
		// URL. This will return the Response from the server.
		Response response = httpRequest.request(Method.GET, "/get/iso2code/" + code);
		Log.info("###get request is executed with country code " + code);
		return response;
	}

	public Response postRequest() {
		JsonObject reqParams = new JsonObject();
		reqParams.addProperty("name", "Test Country");
		reqParams.addProperty("alpha2_code", "TC");
		reqParams.addProperty("alpha3_code", "TCY");

		// Add a header stating the Request body is a JSON
		httpRequest.header("Content-Type", "application/json");

		// Add the Json to the body of the request
		httpRequest.body(reqParams.toString());

		// Post the request and check the response
		Response response = httpRequest.post("/addCountry");
		return response;
	}

	public AllCountries getExpectedData() {
		AllCountries allCountries = null;
		// Get Gson object
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// read JSON file data as String
		String fileData;
		try {
			fileData = new String(Files.readAllBytes(Paths.get("AllCountries.json")));
			// parse json string to object
			allCountries = gson.fromJson(fileData, AllCountries.class);

			// print object data
			// System.out.println("All Countries\n" + allCountries);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allCountries;
	}

	public HashMap<String, String> getExpectedCountryByCode(String alpha2_code) {
		// String name = null, alpha3_code = null;
		HashMap<String, String> countryDetails = new HashMap<String, String>();

		// Get the expected data from json file
		AllCountries allCountries = getExpectedData();
		Result[] results = allCountries.getRestResponse().getResult();
		for (Result result : results) {
			if (result.getAlpha2_code().equals(alpha2_code)) {
				countryDetails.put("name", result.getName());
				countryDetails.put("alpha2_code", result.getAlpha2_code());
				countryDetails.put("alpha3_code", result.getAlpha3_code());
			}
		}
		return countryDetails;
	}
}
