package org.acme.tika;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class ElasticService {
    @Inject
    RestClient restClient; 

    public void index(OcrDocument eFile) throws IOException {
        Request request = new Request(
                "PUT",
                "/files/_doc/" + eFile.getId()); 
        request.setJsonEntity(JsonObject.mapFrom(eFile).toString()); 
        restClient.performRequest(request); 
    }

    public OcrDocument get(String id) throws IOException {
        Request request = new Request(
                "GET",
                "/files/_doc/" + id);
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonObject json = new JsonObject(responseBody); 
        return json.getJsonObject("_source").mapTo(OcrDocument.class);
    }

    public List<OcrDocument> searchByText(String text) throws IOException {
        return search("text", text);
    }

    public List<OcrDocument> searchByName(String name) throws IOException {
        return search("name", name);
    }

    private List<OcrDocument> search(String term, String match) throws IOException {
        Request request = new Request(
                "GET",
                "/files/_search");
        //construct a JSON query like {"query": {"match": {"<term>": "<match"}}
        JsonObject termJson = new JsonObject().put(term, match);
        JsonObject matchJson = new JsonObject().put("match", termJson);
        JsonObject queryJson = new JsonObject().put("query", matchJson);
        request.setJsonEntity(queryJson.encode());
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        JsonObject json = new JsonObject(responseBody);
        JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");
        List<OcrDocument> results = new ArrayList<>(hits.size());
        for (int i = 0; i < hits.size(); i++) {
            JsonObject hit = hits.getJsonObject(i);
            OcrDocument eFile = hit.getJsonObject("_source").mapTo(OcrDocument.class);
            results.add(eFile);
        }
        return results;
    }
}