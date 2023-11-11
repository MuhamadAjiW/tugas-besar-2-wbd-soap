package wbdsoap.utils;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.simple.JSONObject;
import wbdsoap.enums.HTTPMethodEnum;
import wbdsoap.utils.responses.HTTPResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

// TODO: Implement base calls etc
public class RESTUtil {
    public static String restURL = "http://tugas-besar-2-wbd-rest-api-1:8011";

    public static HTTPResponse sendRequest(String endpoint, HTTPMethodEnum method, JSONObject data){
        HttpURLConnection http = null;
        try {
            URL url = new URL(restURL + endpoint);
            http = (HttpURLConnection) url.openConnection();
            switch (method){
                case GET: http.setRequestMethod("GET");break;
                case POST: http.setRequestMethod("POST");break;
                case PUT: http.setRequestMethod("PUT");break;
                case DELETE: http.setRequestMethod("DELETE");break;
                case PATCH: http.setRequestMethod("PATCH");break;
            }

            if(method == HTTPMethodEnum.POST || method == HTTPMethodEnum.PUT || method == HTTPMethodEnum.PATCH){
                http.setRequestProperty("Authorization", "Bearer " + Dotenv.load().get("API_KEY", "nyabun"));
                http.setRequestProperty("Content-Type", "application/json");
                String jsonData = data.toJSONString();
                System.out.println(jsonData);

                http.setDoOutput(true);
                OutputStream outs = http.getOutputStream();
                OutputStreamWriter outsw =  new OutputStreamWriter(outs, StandardCharsets.UTF_8);
                outsw.write(jsonData);
                outsw.flush();
                outsw.close();
                outs.close();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuilder response = new StringBuilder();
            String output = in.readLine();
            while (output != null){
                response.append(output);
                output = in.readLine();
            }

            http.disconnect();

            return new HTTPResponse(http.getResponseCode(), http.getResponseMessage(), response.toString());
        }
        catch (IOException e){
            System.out.println("Request returns an error");
            e.printStackTrace();
            try {
                assert http != null;
                return new HTTPResponse(http.getResponseCode(), http.getResponseMessage(), null);
            } catch (Exception e2){
                System.out.println("Request codes returns an error");
                e.printStackTrace();
                return new HTTPResponse();
            }
        }
        catch (Exception e){
            System.out.println("Request failed");
            e.printStackTrace();
            return new HTTPResponse();
        }
    }
}
