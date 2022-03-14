package org.acme.tika;

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

@Path("/files")
public class TikaResource {
    @Inject
    ElasticService tikaService;

    @POST
    public Response index(OcrDocumentRaw eFile) throws IOException {
        
        tikaService.index(eFile);
        return Response.created(URI.create("/files/" + eFile.getSha())).build();
    }

    @GET
    @Path("/{id}")
    public OcrDocumentRaw get(@PathParam("id") String id) throws IOException {
        return tikaService.get(id);
    }

    @GET
    @Path("/search")
    public List<OcrDocument> search(@QueryParam("name") String name, @QueryParam("text") String text) throws IOException {
        if (name != null) {
            return tikaService.searchByName(name);
        } else if (text != null) {
            return tikaService.searchByText(text);
        } else {
            throw new BadRequestException("Should provide name or text query parameter");
        }
    }
    
    @GET
    @Path("/searchLatest")
    public List<OcrDocument> search() throws IOException {
            return tikaService.searchLatest();
    }

}