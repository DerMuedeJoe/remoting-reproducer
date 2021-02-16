package org.dermuedejoe.spring;

import static org.dermuedejoe.spring.Remoting.*;

import org.dermuedejoe.ejb.EchoServiceEjbApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NamingException;

@RestController
public class EchoController {

   private static Logger LOG = LoggerFactory.getLogger(EchoController.class);

   private static final String applicationName = "ear";
   private static final String distinctName = "";
   private static final String moduleName = "echo-ejb";


   private static final StatelessRemoteBeanDefinition<EchoServiceEjbApi> BEAN_DEFINITION = StatelessRemoteBeanDefinition.build(
         new ApplicationDefinition(
               applicationName,
               moduleName,
               distinctName),
         "EchoServiceBean",
         EchoServiceEjbApi.class
   );


   @GetMapping("/ping-http")
   public String pingHttp() {
      LOG.info("Got request for ping.");

      try {
         LOG.info("Lookup remote service");
         EchoServiceEjbApi echoService= remoteWithHttpLookup(BEAN_DEFINITION);
         LOG.info("Call remote service");
         String ping = echoService.ping();
         LOG.info("Got response from remote service: {}", ping);
         return ping;
      } catch (NamingException e) {
         LOG.error("Failed to call remote service", e);
      }

      return "FAILURE";
   }

   @GetMapping("/ping-remote-http")
   public String pingRemotingUpgrade() {
      LOG.info("Got request for ping.");

      try {
         LOG.info("Lookup remote service");
         EchoServiceEjbApi echoService= remoteWithHttpUpgradeLookup(BEAN_DEFINITION);
         LOG.info("Call remote service");
         String ping = echoService.ping();
         LOG.info("Got response from remote service: {}", ping);
         return ping;
      } catch (NamingException e) {
         LOG.error("Failed to call remote service", e);
      }

      return "FAILURE";
   }
}
