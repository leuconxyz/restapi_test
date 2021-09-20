package org.acme.elasticsearch;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/docs")
public class DocResource {
    @Inject
    DocService docService;

    @POST
    public Response index(Doc doc) throws IOException {
        if (doc.id == null) {
            doc.id = UUID.randomUUID().toString();
        }
        docService.index(doc);
        return Response.created(URI.create("/docs/" + doc.id)).build();
    }

    @GET
    @Path("/{id}")
    public Doc get(@PathParam("id") String id) throws IOException {
        return docService.get(id);
    }

}