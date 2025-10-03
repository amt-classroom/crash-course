package ch.heigvd.amt.service;

import ch.heigvd.amt.entities.Probe;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

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

}
