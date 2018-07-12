package GroupKTAPIFramework.GroupKTAPIFramework;

import java.util.ArrayList;
import java.util.HashMap;

import org.hamcrest.core.IsInstanceOf;
import org.testng.Assert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ResponseData {
	private int statusCode;
	private String messages;
	private String name;
	private String alpha2_code;
	private String alpha3_code;
	private ArrayList<HashMap> results;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlpha2_code() {
		return alpha2_code;
	}

	public void setAlpha2_code(String alpha2_code) {
		this.alpha2_code = alpha2_code;
	}

	public String getAlpha3_code() {
		return alpha3_code;
	}

	public void setAlpha3_code(String alpha3_code) {
		this.alpha3_code = alpha3_code;
	}

	public ArrayList<HashMap> getResults() {
		return results;
	}

	public void setResults(ArrayList<HashMap> results) {
		this.results = results;
	}

	public static ResponseData createInstance(Response response) {
		ResponseData responseData = new ResponseData();
		// Getting the body of the message to see what response we have received from
		// the server
		String responseBody = response.getBody().asString();
		// System.out.println("Response Body is => " + responseBody);

		// Get the status code from the Response
		responseData.statusCode = response.getStatusCode();

		// Get the JsonPath object instance from the Response interface
		JsonPath jsonPathEvaluator = response.jsonPath();
		// Query the JsonPath object to get the value of a node called RestResponse
		HashMap restResponse = jsonPathEvaluator.get("RestResponse");

		//System.out.println("messages: " + restResponse.get("messages"));
		ArrayList<String> aList = (ArrayList<String>) restResponse.get("messages");
		responseData.messages = aList.toString();

		//System.out.println("result: " + restResponse.get("result"));

		// Get the result node details
		Object resultObj = restResponse.get("result");
		if (resultObj instanceof HashMap) {
			HashMap result = (HashMap) resultObj;
			if (result != null) {
				responseData.name = (String) result.get("name");
				responseData.alpha2_code = (String) result.get("alpha2_code");
				responseData.alpha3_code = (String) result.get("alpha3_code");
			}
		} else {
			responseData.results = (ArrayList<HashMap>) resultObj;
		}
		return responseData;
	}
}
