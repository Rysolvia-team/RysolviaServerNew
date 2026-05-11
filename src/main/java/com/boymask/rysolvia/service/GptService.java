package com.boymask.rysolvia.service;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class GptService {
	@Value("${openai.secret.key}")
	private String openaiSecretKey;
	@Value("${gpt.model}")
	private String gptModel;
	@Autowired
	private StatusService statusService;
	@Autowired
	private UsersService usersService;

	public ResponseEntity<String> analyzepdf(Map<String, Object> payload) {

		try {
			String user =  (String) payload.get("user");
			List<String> pdf = (List<String>) payload.get("pdf");


			OpenAIUploader uploader = new OpenAIUploader(openaiSecretKey, this);

			HttpResponse<String> response = uploader.uploadFilesSequentially(new ArrayList<>(pdf), 0,
					new ArrayList<>(), user);

			if (response == null) {

				return ResponseEntity.status(500).body("Risposta nulla");
			}

			return ResponseEntity.status(response.statusCode()).body(response.body());

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(500).body("analyzepdf. Errore server");
		}

	}

	public ResponseEntity<String> analyzeImages(
			@org.springframework.web.bind.annotation.RequestBody Map<String, Object> payload) {
		System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
		try {
			String prompt = Prompt.PROMPT_ASK;
			String user =  (String) payload.get("user");
			List<String> images = (List<String>) payload.get("images");

			JSONObject requestJson = initrequestJson();

			JSONArray input = new JSONArray();
			JSONObject message = new JSONObject();
			message.put("role", "user");

			JSONArray content = new JSONArray();

			content.put(new JSONObject().put("type", "input_text").put("text", prompt));

			for (String base64 : images) {
				content.put(new JSONObject().put("type", "input_image").put("image_url",
						"data:image/jpeg;base64," + base64));
			}

			message.put("content", content);
			input.put(message);
			requestJson.put("input", input);

			OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
					.readTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).build();

			Request request = new Request.Builder().url("https://api.openai.com/v1/responses")
					.addHeader("Authorization", "Bearer " + openaiSecretKey)
					.post(RequestBody.create(requestJson.toString(), MediaType.parse("application/json"))).build();

			Response response = client.newCall(request).execute();
			if (!response.isSuccessful()) {
				String error = response.body() != null ? response.body().string() : "Errore";
				System.out.println("ERRORE OPENAI: " + error);
				return ResponseEntity.status(response.code()).body(error);
			}

			String body = response.body().string();

			getTokens(new JSONObject(body), user);

			return ResponseEntity.ok(body);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Errore server");
		}
	}

	public JSONObject initrequestJson() {
		JSONObject requestJson = new JSONObject();
		requestJson.put("model", gptModel);
		requestJson.put("temperature", 0);
		requestJson.put("top_p", 1);
		return requestJson;
	}

	public void getTokens(JSONObject jsonObject, String user) throws JSONException {
		long tokens = JsonReader.getTokens(jsonObject);

		statusService.updateTokens(tokens, true);
		usersService.decBollette(user);
	}
}
