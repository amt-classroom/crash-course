package ch.heigvd.amt.messaging;

import ch.heigvd.amt.service.ProbeService;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.ObjectMessage;
import jakarta.jms.TextMessage;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class ProbeProducer {

    @Inject
    ProbeService probeService;

    @Inject
    ConnectionFactory connectionFactory;

    @Scheduled(every="1s")
    public void checkProbes() {
        try(var jmsContext = connectionFactory.createContext()) {
            var queue = jmsContext.createQueue("probes");
            var producer = jmsContext.createProducer();

            var probes = probeService.listProbes();
            for(var probe : probes) {
                ObjectMessage message = jmsContext.createObjectMessage();
                try {
                    message.setObject(probe);
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
                producer.send(queue, message);
            }
        }
    }
}
