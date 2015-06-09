package henny.wirelesshouse;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Henny on 2015-04-21.
 * Web service connector
 */

public class Downloader extends AsyncTask<String, Void, String> {


    public Downloader() {

    }

    public String downloadJSON(String url){
        RestTemplate restTemplate = new RestTemplate();
        String result;
        try {
            result = restTemplate.getForObject(url, String.class);
        }catch (RestClientException e) {
            result = "Rest Error";
        }
        return result;
    }

    public void sendJSON(String url, String obj){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<String>(obj,headers);
        RestTemplate restTemplate = new RestTemplate();
        /*
        HttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        HttpMessageConverter stringHttpMessageConverternew = new StringHttpMessageConverter();
        List<HttpMessageConverter<?>> convList = new ArrayList<HttpMessageConverter<?>>();
        convList.add(formHttpMessageConverter);
        //convList.add(stringHttpMessageConverternew);
        //HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //convList.add(jsonHttpMessageConverter);
        restTemplate.setMessageConverters(convList);
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        */
        restTemplate.put(url, entity);
    }

    /**
     * Making service call
     * @urlString - url string to make request
     * */
    public String getCall(String urlString) {
        String text = null;
        InputStream is = null;
        int len = 100000;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            is = conn.getInputStream();
            if (is == null) {
                text = "IS is Null";
            } else {
                text = readIt(is, len);
            }
        } catch (IOException e) {
            text = "IO Exception while getting stream";
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    text = "IO Exception while closing IS";
                }
            }
            if (text == null) text = "Null String from IS";
            return text;
        }
    }

        @Override
        protected String doInBackground(String... strings) {
            return downloadJSON(strings[0]);
        }

        @Override
        protected void onPreExecute() {
        }

        public String readIt(InputStream stream, int len) throws IOException{
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

    }