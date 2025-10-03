package ch.heigvd.amt.resources;

import ch.heigvd.amt.service.ProbeService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class ProbeResource {

    @Inject
    EntityManager em;

    @Inject
    ProbeService probeService;

    @Inject
    Template indexPage;

    @Inject
    Template listPage;

    @Inject
    Template createPage;

    @Inject
    Template statusPage;

    @Inject
    Template statusContent;

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

    @GET
    @Path("/status")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance status(@QueryParam("url") String url) {
        var probe = probeService.createProbeIfNotExists(url);
        var lastStatus = probeService.getLastStatus(probe);
        var statusList = probeService.getStatusList(probe);
        return statusPage.data("probe", probe).data("lastStatus", lastStatus).data("statusList", statusList);
    }

    @GET
    @Path("/status/content")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance statusContent(@QueryParam("url") String url) {
        var probe = probeService.createProbeIfNotExists(url);
        var lastStatus = probeService.getLastStatus(probe);
        var statusList = probeService.getStatusList(probe);
        return statusContent.data("probe", probe).data("lastStatus", lastStatus).data("statusList", statusList);
    }
}
