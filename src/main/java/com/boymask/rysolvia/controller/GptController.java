package com.boymask.rysolvia.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@RestController
@RequestMapping("/api/gpt")
@CrossOrigin
public class GptController {
	@Value("${openai.secret.key}")
	private String openaiSecretKey;
	@Value("${gtp.model}")
	private String gptModel;
   

    @PostMapping("/analyze")
    public ResponseEntity<String> analyze(@org.springframework.web.bind.annotation.RequestBody Map<String, Object> payload) {
System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
        try {
            String prompt = (String) payload.get("prompt");
            List<String> images = (List<String>) payload.get("images");

            JSONObject requestJson = new JSONObject();
            requestJson.put("model", gptModel);

            JSONArray input = new JSONArray();
            JSONObject message = new JSONObject();
            message.put("role", "user");

            JSONArray content = new JSONArray();

            content.put(new JSONObject()
                    .put("type", "input_text")
                    .put("text", prompt));

            for (String base64 : images) {
                content.put(new JSONObject()
                        .put("type", "input_image")
                        .put("image_url", "data:image/jpeg;base64," + base64));
            }

            message.put("content", content);
            input.put(message);
            requestJson.put("input", input);

        
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/responses")
                    .addHeader("Authorization", "Bearer " + openaiSecretKey)
                    .post(RequestBody.create(
                            requestJson.toString(),
                            MediaType.parse("application/json")
                    ))
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                String error = response.body() != null ? response.body().string() : "Errore";
                System.out.println("ERRORE OPENAI: " + error);
                return ResponseEntity.status(response.code()).body(error);
            }

            String body = response.body().string();
System.out.println(body);
            return ResponseEntity.ok(body);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Errore server");
        }
    }
}