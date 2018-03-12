package com.myproject.events;

import java.util.Arrays;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolverFactory ;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

/**
 * Just a simple DS Component
 */
@Component(immediate=true)
@Service
public class NodeAddedEventListener implements EventListener {

    private static final Logger LOG = LoggerFactory.getLogger(NodeAddedEventListener.class);

    private BundleContext bundleContext;

    @Reference
    private SlingRepository repository;

    @Reference
    private ResourceResolverFactory resolverFactory;

    private Session session;
    private ObservationManager observationManager;
    
    @Reference
    private MessageGatewayService messageGatewayService;

    public void run() {
        LOG.debug("Running...");
    }

    protected void activate(ComponentContext ctx) {

        this.bundleContext = ctx.getBundleContext();
        try {

            //Invoke the adaptTo method to create a Session 
            //ResourceResolver resourceResolver = resolverFactory.getAdministrativeResourceResolver(null);
            session = repository.loginAdministrative(null);

            // Setup the event handler to respond to a new claim under /content/my-project 
            observationManager = session.getWorkspace().getObservationManager();
            final String[] types = { "nt:unstructured","sling:Folder" };
            final String path = "/content/my-project";
            observationManager.addEventListener(this, Event.NODE_ADDED, path, true, null, null, false);
            LOG.info("Observing property changes to {} nodes under {}", Arrays.asList(types), path);

        } catch(Exception e) {
            LOG.debug("Exception {} ", e.getMessage());
        }
    }

    protected void deactivate(ComponentContext componentContext) throws RepositoryException {

        if(observationManager != null) {
            observationManager.removeEventListener(this);
        }
        if (session != null) {
            session.logout();
            session = null;
        }
    }

    //Email is fired when the event occurs. 
    public void onEvent(EventIterator itr) {

        try {           

            //Declare a MessageGateway service
            MessageGateway<Email> messageGateway; 

            //Set up the Email message
            Email email = new SimpleEmail();

            //Set the mail values
            String emailToRecipients = "abc@gmail.com"; 
            String emailCcRecipients = "xyz@gmail.com"; 

            email.addTo(emailToRecipients);
            email.addCc(emailCcRecipients);
            email.setSubject("AEM Eventlistener");
            email.setFrom("hello@gmail.com"); 
            email.setMsg("This message is to inform you that content under /content/my-project has been added");

            //Inject a MessageGateway Service and send the message
            messageGateway = messageGatewayService.getGateway(Email.class);

            // Check the logs to see that messageGateway is not null
            messageGateway.send((Email) email);
        } catch (EmailException ex) {
            LOG.debug("Exception {} ", ex.getMessage());
        }

    }
}
