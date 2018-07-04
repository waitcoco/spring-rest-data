package com.mycompany.knowledge.miami.publish.repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mycompany.knowledge.miami.publish.model.gongan.Person;
import okhttp3.Response;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;

@Data
@Repository
public class PersonInfoGetter {
    private String baseUrl;
    private final Gson gson  = new Gson();
    private HttpClient httpClient = HttpClients.createDefault();
    private JsonParser jsonParser = new JsonParser();
    public PersonInfoGetter(@Value("${person.personurl}") String baseUrl){
        this.baseUrl = baseUrl;
    }

    public Person getPersonByIdentity(String identity) throws Exception {
        //String identity = person.getIdentity();
        URI uri = new URIBuilder(baseUrl + identity).build();
        HttpGet httpGet = new HttpGet(uri);
        httpGet.addHeader("Authorization", "df620992-d943-4684-924b-b83c9605c47a");
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        Person basicInfo = new Person();
        if (entity != null) {
            JsonObject basicInfoJO = jsonParser.parse(EntityUtils.toString(entity, "UTF-8")).getAsJsonObject().get("basicInfo").getAsJsonObject();
            basicInfo = gson.fromJson(basicInfoJO.toString(), Person.class);
        }
        return basicInfo;
    }
}
