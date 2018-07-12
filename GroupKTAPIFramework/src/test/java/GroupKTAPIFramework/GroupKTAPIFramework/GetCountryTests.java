package GroupKTAPIFramework.GroupKTAPIFramework;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;

import io.restassured.response.Response;

public class GetCountryTests extends APIExecutor {

	@Test
	public void getAllCountries() {
		Log.startTestCase("Get All Countries");
		//Send getAll request
		Response response = getAllRequest(httpRequest, "all");
		ResponseData responseData = ResponseData.createInstance(response);
		Log.info("response data of getall request is parsed");

		// Assert the status code and messages that is returned
		Assert.assertEquals(responseData.getStatusCode(), 200, "Status code is correct");
		Assert.assertEquals(responseData.getMessages(), "[Total [249] records found.]");
		Log.info("Status code and messages are verified");
		
		ArrayList<HashMap> results = responseData.getResults();
		int count = 0;
		for (HashMap result : results) {
			if (result.get("alpha2_code").equals("US")) {
				Assert.assertTrue(true, "US is present");
				Log.info("US is found in all countries list");
				Reporter.log("US is found in all countries list\n");
				count++;
			} else if (result.get("alpha2_code").equals("DE")) {
				Assert.assertTrue(true, "DE is present");
				Log.info("DE is found in all countries list");
				Reporter.log("DE is found in all countries list\n");
				count++;
			} else if (result.get("alpha2_code").equals("GB")) {
				Assert.assertTrue(true, "GB is present");
				Log.info("GB is found in all countries list");
				Reporter.log("GB is found in all countries list\n");
				count++;
			}
		}
		if(count != 3) {
			Assert.fail("Either all countries (US, DE & GB) or any one of them is not present");
		}
		Log.endTestCase("Get All Countries");
	}

	@Test(dataProvider = "CountryDetails")
	public void getCountry(String alpha2_code) {
		Log.startTestCase("Get Country details by code: "+alpha2_code);
		// Send a request to the server by specifying the method Type and the method
		// URL. This will return the Response from the server.
		Response response = getCountryRequest(httpRequest, alpha2_code);
		//Parsing the response data
		ResponseData responseData = ResponseData.createInstance(response);
		Log.info("response data of get request is parsed");
		
		//Get expected data
		HashMap<String, String> country = getExpectedCountryByCode(alpha2_code);
		
		// Assert the status code and messages that is returned
		Assert.assertEquals(responseData.getStatusCode(), 200, "Status code is correct");
		Assert.assertEquals(responseData.getMessages(), "[Country found matching code [" + alpha2_code + "].]",
				"Status code is correct");
		Log.info("Status code and messages are verified");

		// Validate the response
		Assert.assertEquals(responseData.getName(), country.get("name"), "Country name is correct");
		Assert.assertEquals(responseData.getAlpha2_code(), country.get("alpha2_code"), "alpha2_code is correct");
		Assert.assertEquals(responseData.getAlpha3_code(), country.get("alpha3_code"), "alpha3_code is correct");
		Log.info("Country details are verified");
		Log.endTestCase("Get Country details by code: "+alpha2_code);
	}

	@Test
	public void getCountryWithInvalidCode() {
		Log.startTestCase("Get Country details by invalid code");
		//Executing get request with invalid code
		Response response = getCountryRequest(httpRequest, "invalidcode");
		Log.info("###get request is executed with invalid conutry code");
		ResponseData responseData = ResponseData.createInstance(response);
		Log.info("response data of get request is parsed");

		// Assert the status code that is returned.;
		Assert.assertEquals(responseData.getStatusCode(), 200, "Status code is correct");
		Assert.assertEquals(responseData.getMessages(), "[No matching country found for requested code [invalidcode].]",
				"Message is correct");
		Log.info("Status code and messages are verified");
		Log.endTestCase("Get Country details by invalid code");
	}
	@Test
	public void postCountry() {
		Log.startTestCase("Post request to add country details");
		//Executing post request
		Response response = postRequest();
		Log.info(
				"###post request is executed. It is executed successfully though it doesn't have post implementation as it is returning web page with status 200");
		// This post request is returning the web page and status code is 200
		// System.out.println(response.getStatusCode());
		Assert.assertEquals(response.getStatusCode(), 200, "Status code is correct");
		Log.endTestCase("Post request to add country details");
	}

	@DataProvider(name = "CountryDetails")
	public static Object[][] countryDetails() {

		return new Object[][] { { "US" }, { "DE" }, { "GB" } };

	}
}
