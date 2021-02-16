package org.dermuedejoe.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wildfly.naming.client.WildFlyInitialContextFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class Remoting {

   private static final Logger LOG = LoggerFactory.getLogger(Remoting.class);

   private static final String serverAddress = "jboss-service:8080";

   private static final String SECURITY_PRINCIPAL = "user";
   private static final String SECURITY_CREDENTIALS = "password";

   static <T> T remoteWithHttpLookup(StatelessRemoteBeanDefinition<T> remoteBeanDefinition) throws NamingException {
      final Hashtable<String, String> jndiProperties = new Hashtable<>();
      jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, WildFlyInitialContextFactory.class.getName());
      jndiProperties.put(Context.PROVIDER_URL, "http://" + serverAddress + "/wildfly-services");
      jndiProperties.put(Context.SECURITY_PRINCIPAL, SECURITY_PRINCIPAL);
      jndiProperties.put(Context.SECURITY_CREDENTIALS, SECURITY_CREDENTIALS);

      final Context context = new InitialContext(jndiProperties);
      try {
         return remoteBeanDefinition.lookup(context);
      } finally {
         context.close();
      }
   }

   static <T> T remoteWithHttpUpgradeLookup(StatelessRemoteBeanDefinition<T> remoteBeanDefinition) throws NamingException {
      final Hashtable<String, String> jndiProperties = new Hashtable<>();
      jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, WildFlyInitialContextFactory.class.getName());
      jndiProperties.put(Context.PROVIDER_URL, "remote+http://" + serverAddress);
      jndiProperties.put(Context.SECURITY_PRINCIPAL, SECURITY_PRINCIPAL);
      jndiProperties.put(Context.SECURITY_CREDENTIALS, SECURITY_CREDENTIALS);

      final Context context = new InitialContext(jndiProperties);
      try {
         return remoteBeanDefinition.lookup(context);
      } finally {
         context.close();
      }
   }

   static class ApplicationDefinition {
      /**
       * The app name is the application name of the deployed EJBs. This is typically the ear name without suffix.
       */
      private final String appName;
      /**
       * The module name of the deployed EJBs on the server. This is typically the jar name of the EJB deployment, without the .jar suffix
       */
      private final String moduleName;
      /**
       * AS7 allows each deployment to have an (optional) distinct name.
       */
      private final String distinctName;

      public ApplicationDefinition(String appName, String moduleName, String distinctName) {
         this.appName = appName;
         this.moduleName = moduleName;
         this.distinctName = distinctName;
      }
   }


   static class StatelessRemoteBeanDefinition<T> {

      /**
       * The app name is the application name of the deployed EJBs. This is typically the ear name without suffix.
       */
      private final ApplicationDefinition applicationDefinition;
      /**
       * The EJB name which by default is the simple class name of the bean implementation class
       */
      private final String beanName;
      /**
       * the remote view fully qualified class name
       */
      private final String viewClassName;

      StatelessRemoteBeanDefinition(ApplicationDefinition applicationDefinition, String beanName, String viewClassName) {
         this.applicationDefinition = applicationDefinition;
         this.beanName = beanName;
         this.viewClassName = viewClassName;
      }

      static <T> StatelessRemoteBeanDefinition<T> build(ApplicationDefinition applicationDefinition, String beanName, Class<T> viewClass) {
         String viewClassName = viewClass.getName();

         return new StatelessRemoteBeanDefinition<>(applicationDefinition, beanName, viewClassName);
      }

      private T lookup(Context context) throws NamingException {
         String lookupKey = buildLookupKey();
         LOG.info("Lookup: " + lookupKey);
         return (T) context.lookup(lookupKey);
      }

      private String buildLookupKey() {
         return "ejb:" +
               applicationDefinition.appName + "/" +
               applicationDefinition.moduleName + "/" +
               applicationDefinition.distinctName + "/" +
               beanName + "!" + viewClassName;
      }
   }
}
