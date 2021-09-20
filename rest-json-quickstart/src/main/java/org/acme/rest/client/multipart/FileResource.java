package org.acme.rest.client.multipart;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.tika.Tika;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

@Path("/client")
public class FileResource {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String sendFile(@QueryParam("id") String id, @MultipartForm MultipartBody data) throws Exception {
    	String content = "";
    	try {
            content = new Tika().parseToString(data.file);
            System.out.println("The Content: " + content);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}