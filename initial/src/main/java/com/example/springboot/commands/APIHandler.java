package com.example.springboot.commands;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

//import io.github.cdimascio.dotenv.Dotenv;

public class APIHandler {

    // dotenv.get("MERRIAM_WEBSTER_KEY") REPLACE THE SYSTEM.GETENV WITH THIS DURING LOCAL TESTING
    public static String callMerriamWebsterApi(String word) {
        String encodedWord = URLEncoder.encode(word, StandardCharsets.UTF_8);
        //Dotenv dotenv = Dotenv.configure().load();
        String url = "https://www.dictionaryapi.com/api/v3/references/collegiate/json/" + encodedWord + "?key=" 
                + System.getenv("MERRIAM_WEBSTER_KEY");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Process the response body to extract the definition
            // This depends on the structure of the response JSON
            System.out.println(response.body());
            String responseBody = parseDefinitionFromResponse(response.body(), word);
            if (responseBody.equals("Error parsing the definition.")) {
                return "Come back tomorrow for a better word";
            }
            return responseBody;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error fetching definition.";
        }
    }

    private static String parseDefinitionFromResponse(String body, String word) {
        // try {
        //     JSONArray obj = new JSONArray(body);
        //     String regex = "\\{[^}]*\\}";
        //     if (obj.length() > 0) {
        //         try {
        //             JSONObject firstItem = obj.getJSONObject(0);
        //             JSONArray defArray = firstItem.getJSONArray("def");
        //             if (defArray.length() > 0) {
        //                 JSONObject firstDef = defArray.getJSONObject(0);
        //                 JSONArray sseqArray = firstDef.getJSONArray("sseq");
        //                 if (sseqArray.length() > 0) {
        //                     JSONArray firstSseqEntry = sseqArray.getJSONArray(0);
        //                     if (firstSseqEntry.length() > 0) {
        //                         JSONArray firstSenseEntry = firstSseqEntry.getJSONArray(0);
        //                         if (firstSenseEntry.length() > 1) {
        //                             JSONObject senseObject = firstSenseEntry.getJSONObject(1);
        //                             JSONArray dtArray = senseObject.getJSONArray("dt");
        //                             JSONArray firstEntry = dtArray.getJSONArray(0);
        //                             String definition = firstEntry.getString(1);
        //                             definition = definition.replaceAll(regex, "").trim();                            
        //                             return definition;
        //                         }
        //                     }
        //                 }
        //             }
        //         } catch (Exception e) {
        //             // First call sends list of possible words; take first word
        //             callMerriamWebsterApi(obj.getString(0));
        //         }
        //     }
        //     return "Error parsing in try block";
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     return "Error parsing the definition.";
        // }

        try {
            JSONArray obj = new JSONArray(body);
            //String regex = "\\{[^}]*\\}";

            if (obj.length() > 0) {
                try {
                    JSONObject firstItem = obj.getJSONObject(0);
                    JSONArray defArray = firstItem.getJSONArray("shortdef");

                    if (defArray.length() > 0) {
                        String def = defArray.getString(0);

                        return word + ": " + def;
                    }
                } catch (Exception e) {
                    // First call sends list of possible words; take first word
                    return callMerriamWebsterApi(obj.getString(0));
                }
            }
            return "Error parsing in try block";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing the definition.";
        }

    }
}
