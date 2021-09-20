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
import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

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
    @Produces(MediaType.TEXT_PLAIN)
    public Response get(@PathParam("id") String id) throws IOException {
        OcrDocument occ = elasticService.get(id);
        return Response.ok(occ).build();
    }

    @POST
    @Path("/text")
    @Consumes({ "application/pdf", "application/vnd.oasis.opendocument.text",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "text/plain", "text/csv",
            "application/json", "text/yaml", "application/xml", "application/msword", "application/vnd.ms-excel", "application/vnd.ms-powerpoint" })
    @Produces(MediaType.TEXT_PLAIN)
    public String extractText(InputStream stream) throws IOException, NoSuchAlgorithmException {
        Instant start = Instant.now();
        byte[] bytes = IOUtils.toByteArray(stream);
        
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] sha256Hash = digest.digest(bytes);
        BigInteger shaInt = new BigInteger(sha256Hash);

        stream = new ByteArrayInputStream(bytes);
        TikaContent tcont = parser.parse(stream);
        
        Map<String, String> extractMetadata = new HashMap<String, String>();

        Set<String> names = tcont.getMetadata().getNames();
        for (String name : names) {
        	extractMetadata.put(name, String.join(",", tcont.getMetadata().getValues(name)));
        }
        
        OcrDocument ocrDocument = new OcrDocument(shaInt, null, tcont.getText(),extractMetadata, LocalDateTime.now(), null, null);
        log.info(ocrDocument);
        elasticService.index(ocrDocument);

        Instant finish = Instant.now();

        log.info(Duration.between(start, finish).toMillis() + " mls have passed");

        return tcont.getText();
    }
}