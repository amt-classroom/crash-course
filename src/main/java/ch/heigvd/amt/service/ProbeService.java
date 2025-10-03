package ch.heigvd.amt.service;

import ch.heigvd.amt.entities.Probe;
import ch.heigvd.amt.entities.Status;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.HttpHeaders;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class ProbeService {

    @Inject
    private EntityManager em;

    @Transactional
    public List<Probe> listProbes() {
        return em.createQuery(
                        "select p from Probe p",
                        Probe.class)
                .getResultList();
    }

    @Transactional
    public Probe createProbeIfNotExists(String url) {
        List<Probe> probes = em.createQuery(
                        "select p from Probe p where p.url = :url",
                        Probe.class)
                .setParameter("url", url)
                .getResultList();
        if (probes.isEmpty()) {
            Probe probe = new Probe(url);
            em.persist(probe);
            return probe;
        } else {
            return probes.get(0);
        }
    }

    @Transactional
    public Status executeProbe(Probe probe) {
        var status = new Status();

        try (HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofSeconds(10)).build()) {
            var request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(probe.getUrl()))
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                    .header(HttpHeaders.EXPIRES, "no-cache")
                    .timeout(Duration.ofSeconds(10)).build();

            var start = Instant.now();
            var response = client.send(request, java.net.http.HttpResponse.BodyHandlers.discarding());
            var end = Instant.now();

            status.setProbe(probe);
            status.setTimestamp(end);
            status.setDuration((int) Duration.between(start, end).toMillis());
            status.setUp(response.statusCode() >= 200 && response.statusCode() < 400);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(status);

        em.persist(status);

        return status;
    }

}
