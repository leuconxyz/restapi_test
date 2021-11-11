package org.acme.tika;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.acme.elasticsearch.Fruit;
import org.acme.rest.client.multipart.MultipartBody;
import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import io.quarkus.tika.TikaContent;
import io.quarkus.tika.TikaParser;

@Path("/parse")
public class TikaParserResource {

    private static final Logger log = Logger.getLogger(TikaParserResource.class);

    @Inject
    TikaParser parser;
    @Inject
    ElasticService elasticService;
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") String id) throws IOException {
        OcrDocumentRaw occ = elasticService.get(id);
        return Response.ok(occ).build();
    }

    @POST
    @Path("/text")
//    @Consumes({ "application/pdf", "application/vnd.oasis.opendocument.text",
//            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
//            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
//            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "text/plain", "text/csv",
//            "application/json", "text/yaml", "application/xml", "application/msword", "application/vnd.ms-excel", "application/vnd.ms-powerpoint" })
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String extractText(@MultipartForm MultipartBody data) throws IOException, NoSuchAlgorithmException {
        Instant start = Instant.now();
        byte[] bytes = IOUtils.toByteArray(data.file);
        String shaInt = org.apache.commons.codec.digest.DigestUtils.sha256Hex(bytes);

        data.file = new ByteArrayInputStream(bytes);
        TikaContent tcont = parser.parse(data.file);
        
        Map<String, String> extractMetadata = new HashMap<String, String>();

        Set<String> names = tcont.getMetadata().getNames();
        for (String name : names) {
        	extractMetadata.put(name, String.join(",", tcont.getMetadata().getValues(name)));
        }
        
        OcrDocumentRaw ocrDocument = new OcrDocumentRaw(shaInt, data.fileName, tcont.getText(),extractMetadata, LocalDateTime.now(), null, null);
        log.info(ocrDocument);
        elasticService.index(ocrDocument);

        Instant finish = Instant.now();

        log.info(Duration.between(start, finish).toMillis() + " mls have passed");

        return tcont.getText();
    }
}