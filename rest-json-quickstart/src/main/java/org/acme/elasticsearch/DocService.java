package org.acme.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

//import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class DocService {
    @Inject
    RestClient restClient; 

    public void index(Doc doc) throws IOException {
        Request request = new Request(
                "PUT",
                "/docs/_doc/" + doc.id); 
        request.setJsonEntity(JsonObject.mapFrom(doc).toString()); 
        restClient.performRequest(request); 
    }

    public Doc get(String id) throws IOException {
        Request request = new Request(
                "GET",
                "/docs/_doc/" + id);
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonObject json = new JsonObject(responseBody); 
        return json.getJsonObject("_source").mapTo(Doc.class);
    }
}