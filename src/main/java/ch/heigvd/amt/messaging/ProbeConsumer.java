package ch.heigvd.amt.messaging;

import ch.heigvd.amt.entities.Probe;
import ch.heigvd.amt.service.ProbeService;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.ObjectMessage;

@ApplicationScoped
public class ProbeConsumer {

    @Inject
    ProbeService probeService;

    @Inject
    ConnectionFactory connectionFactory;

    JMSContext jmsContext;

    void onStart(@Observes StartupEvent ev) {
        jmsContext = connectionFactory.createContext();
        var queue = jmsContext.createQueue("probes");
        var consumer = jmsContext.createConsumer(queue);
        consumer.setMessageListener(message -> {
            if (message instanceof ObjectMessage) {
                var objectMessage = (ObjectMessage) message;
                try {
                    var probe = (Probe) objectMessage.getObject();
                    if (probe != null) {
                        probeService.executeProbe(probe);
                    }
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    void onStop(@Observes ShutdownEvent ev) {
        if (jmsContext != null) {
            jmsContext.close();
        }
    }

}
