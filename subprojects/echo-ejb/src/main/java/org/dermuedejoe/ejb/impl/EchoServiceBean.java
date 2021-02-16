package org.dermuedejoe.ejb.impl;

import org.dermuedejoe.ejb.EchoServiceEjbApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(EchoServiceEjbApi.class)
public class EchoServiceBean implements EchoServiceEjbApi {

   private static final Logger LOG = LoggerFactory.getLogger(EchoServiceEjbApi.class);

   @Override
   public String ping() {
      LOG.info("ping called");
      return "pong";
   }
}
