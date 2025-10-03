package ch.heigvd.amt.resources;

import ch.heigvd.amt.service.ProbeService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class ProbeResource {

    @Inject
    ProbeService probeService;

    @Inject
    Template indexPage;

    @Inject
    Template listPage;

    @Inject
    Template createPage;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {
        return indexPage.instance();
    }

    @GET
    @Path("/probes")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance list() {
        return listPage.data("probes", probeService.listProbes());
    }

    @GET
    @Path("/probes/create")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance create() {
        return createPage.instance();
    }

    @POST
    @Path("/probes")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance create(@FormParam("url") String url) {
        probeService.createProbeIfNotExists(url);
        return listPage.data("probes", probeService.listProbes());
    }
}
