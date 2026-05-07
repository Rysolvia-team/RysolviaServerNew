package com.boymask.rysolvia.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class OpenAIUploader {

	private String apiKey;
	private GptService gptService;

	public OpenAIUploader(String apiKey, GptService gptService) {
		this.apiKey = apiKey;
		this.gptService=gptService;
	}

	public HttpResponse<String> uploadFilesSequentially(List<String> base64Files, int index, ArrayList<String> fileIds) {

		if (index >= base64Files.size()) {

			System.out.println("Upload completato");

			HttpResponse<String> ret = analyzeMultipleFiles(fileIds);
System.out.println("FINITO ANALIZE");
System.out.println(ret);
			
			return ret;
		}

		try {

			String base64 = base64Files.get(index);

			// rimuove eventuale prefisso
			if (base64.contains(",")) {
				base64 = base64.substring(base64.indexOf(",") + 1);
			}

			byte[] fileBytes = Base64.getDecoder().decode(base64);

			String boundary = "----Boundary" + System.currentTimeMillis();

			byte[] body = buildMultipartBody(boundary, fileBytes, "file_" + index + ".pdf");

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.openai.com/v1/files"))
					.header("Authorization", "Bearer " + apiKey)
					.header("Content-Type", "multipart/form-data; boundary=" + boundary)
					.POST(HttpRequest.BodyPublishers.ofByteArray(body)).build();

			HttpClient client = HttpClient.newHttpClient();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			System.out.println(response.body());

			if (response.statusCode() >= 200 && response.statusCode() < 300) {

				JSONObject json = new JSONObject(response.body());

				String fileId = json.getString("id");

				fileIds.add(fileId);

				System.out.println("Upload OK -> " + fileId);

			} else {

				System.out.println("Errore upload: " + response.statusCode());
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		// prossimo file
		return uploadFilesSequentially(base64Files, index + 1, fileIds);
	
	}

	private byte[] buildMultipartBody(String boundary, byte[] fileBytes, String fileName) throws IOException {

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		String lineBreak = "\r\n";

		// purpose
		output.write(("--" + boundary + lineBreak).getBytes(StandardCharsets.UTF_8));

		output.write(("Content-Disposition: form-data; " + "name=\"purpose\"" + lineBreak + lineBreak + "assistants"
				+ lineBreak).getBytes(StandardCharsets.UTF_8));

		// file
		output.write(("--" + boundary + lineBreak).getBytes(StandardCharsets.UTF_8));

		output.write(
				("Content-Disposition: form-data; " + "name=\"file\"; " + "filename=\"" + fileName + "\"" + lineBreak)
						.getBytes(StandardCharsets.UTF_8));

		output.write(
				("Content-Type: application/octet-stream" + lineBreak + lineBreak).getBytes(StandardCharsets.UTF_8));

		output.write(fileBytes);

		output.write(lineBreak.getBytes(StandardCharsets.UTF_8));

		// end
		output.write(("--" + boundary + "--" + lineBreak).getBytes(StandardCharsets.UTF_8));

		return output.toByteArray();
	}

	private HttpResponse<String> analyzeMultipleFiles(ArrayList<String> fileIds) {

		System.out.println("Analizzo file:");

		try {

			JSONObject requestJson = gptService.initrequestJson();

			JSONArray inputArray = new JSONArray();

			JSONObject message = new JSONObject();

			message.put("role", "user");

			JSONArray contentArray = new JSONArray();

			// testo prompt
			contentArray.put(new JSONObject().put("type", "input_text").put("text", Prompt.PROMPT_ASK));

			// aggiunge tutti i file
			for (String fileId : fileIds) {

				contentArray.put(new JSONObject().put("type", "input_file").put("file_id", fileId));
			}

			message.put("content", contentArray);

			inputArray.put(message);

			requestJson.put("input", inputArray);

			String jsonBody = requestJson.toString();

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.openai.com/v1/responses"))
					.header("Authorization", "Bearer " + apiKey).header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(jsonBody)).build();

			HttpClient client = HttpClient.newHttpClient();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			System.out.println("HTTP CODE: " + response.statusCode());

			System.out.println("RESPONSE: " + response.body());
			
			gptService.getTokens(new JSONObject(response.body()));
			
			return response;

//			if (response.statusCode() >= 200 && response.statusCode() < 300) {
//
//				reportOutput(response.body(), false);
//
//			} else {
//
//				System.out.println("Errore API OpenAI");
//			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}
	
}