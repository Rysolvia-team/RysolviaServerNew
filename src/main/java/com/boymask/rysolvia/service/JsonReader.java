package com.boymask.rysolvia.service;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {
    public static long getTokens(JSONObject jsonObject) throws JSONException {
    	long text = jsonObject
                .getJSONObject("usage")
                //    .getJSONObject(0)

                .getLong("total_tokens");
//        text = text.replace("```json", "")
//                .replace("```", "")
//                .trim();
        return text;
    }
}
