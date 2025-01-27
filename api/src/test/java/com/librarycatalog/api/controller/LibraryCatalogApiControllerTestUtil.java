package com.librarycatalog.api.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This contains utility methods used in integration testing.
 */
public interface LibraryCatalogApiControllerTestUtil {

	/**
	 * This method makes a POST call.
	 * 
	 * @param urlString       URL
	 * @param jsonRequestData request body in JSON format
	 * @param statusCode      Http Status code
	 * @return response string
	 */
	static String postResponseString(String urlString, String jsonRequestData, int statusCode) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeChars(jsonRequestData);
			outputStream.flush();

			int responseCode = connection.getResponseCode();

			if (responseCode == statusCode) {
				BufferedReader reader = null;

				if (HttpURLConnection.HTTP_OK == statusCode)
					reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				else
					reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

				String line;
				StringBuilder response = new StringBuilder();

				while ((line = reader.readLine()) != null) {
					response.append(line);
				}

				reader.close();

				return response.toString();
			}

			connection.disconnect();

		} catch (IOException | NullPointerException ioEx) {
			return null;
		}

		return null;
	}

	/**
	 * This method makes a GET call.
	 * 
	 * @param urlString  URL
	 * @param statusCode Http Status code
	 * @return response string
	 */
	static String getResponseString(String urlString, int statusCode) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();

			if (responseCode == statusCode) {
				BufferedReader reader = null;

				if (HttpURLConnection.HTTP_OK == statusCode)
					reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				else
					reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

				String line;
				StringBuilder response = new StringBuilder();

				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();

				return response.toString();
			}

			connection.disconnect();

		} catch (IOException ioEx) {
			return null;
		}

		return null;
	}

	/**
	 * This creates the success response.
	 * 
	 * @param jsonData message in JSON format
	 * @return ResponseSuccess instance
	 */
	static ResponseSuccess createResponseSuccess(String jsonData) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			ResponseSuccess response = objectMapper.readValue(jsonData, ResponseSuccess.class);
			return response;
		} catch (IOException ioEx) {
			return null;
		}
	}

	/**
	 * This creates the error response.
	 * 
	 * @param jsonData message in JSON format
	 * @return ResponseError instance
	 */
	static ResponseError createResponseError(String jsonData) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			ResponseError response = objectMapper.readValue(jsonData, ResponseError.class);
			return response;
		} catch (IOException ioEx) {
			return null;
		}
	}
}
