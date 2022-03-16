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

    public void index(OcrDocumentRaw eFile) throws IOException {
        Request request = new Request(
                "PUT",
                "/files/_doc/" + eFile.getSha()); 
        request.setJsonEntity(JsonObject.mapFrom(eFile).toString()); 
        restClient.performRequest(request); 
    }

    public OcrDocumentRaw get(String id) throws IOException {
        Request request = new Request(
                "GET",
                "/files/_doc/" + id);
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonObject json = new JsonObject(responseBody); 
        return json.getJsonObject("_source").mapTo(OcrDocumentRaw.class);
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
        JsonObject textJson = new JsonObject().put("text", new JsonObject());
        JsonObject fieldsJson = new JsonObject().put("fields", textJson).put("pre_tags",  "<b>").put("post_tags", "</b>");
        //JsonObject highlightJson = new JsonObject().put("highlight", fieldsJson);
        
        JsonObject queryJson = new JsonObject().put("query", matchJson).put("highlight", fieldsJson);
        request.setJsonEntity(queryJson.encode());
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        JsonObject json = new JsonObject(responseBody);
        JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");
        List<OcrDocument> results = new ArrayList<>(hits.size());
        for (int i = 0; i < hits.size(); i++) {
            JsonObject hit = hits.getJsonObject(i);
            OcrDocument eFile = new OcrDocument();
            eFile.setHighlight(hit.getJsonObject("highlight").mapTo(OcrDocumentHighlighted.class));
            eFile.set_source(hit.getJsonObject("_source").mapTo(OcrDocumentRaw.class));
            results.add(eFile);
        }
        return results;
    }
    
    public List<OcrDocument> searchLatest() throws IOException {
        Request request = new Request(
                "GET",
                "/files/_search");
        
        String JsonRequest = "{\r\n" + 
        		"  \"query\": {\r\n" + 
        		"    \"match_all\": {}\r\n" + 
        		"  },\r\n" + 
        		"  \"size\": 10,\r\n" + 
        		"  \"sort\": [\r\n" + 
        		"    {\r\n" + 
        		"      \"indexDate\": {\r\n" + 
        		"        \"order\": \"desc\"\r\n" + 
        		"      }\r\n" + 
        		"    }\r\n" + 
        		"  ]\r\n" + 
        		"}";
        
        request.setJsonEntity(JsonRequest);
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        JsonObject json = new JsonObject(responseBody);
        JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");
        List<OcrDocument> results = new ArrayList<>(hits.size());
        for (int i = 0; i < hits.size(); i++) {
            JsonObject hit = hits.getJsonObject(i);
            OcrDocument eFile = new OcrDocument();
            eFile.set_source(hit.getJsonObject("_source").mapTo(OcrDocumentRaw.class));
            results.add(eFile);
        }
        return results;
    }
}