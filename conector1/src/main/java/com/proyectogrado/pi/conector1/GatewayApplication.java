package com.proyectogrado.pi.conector1;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.HeaderEnricherSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

@EnableIntegration
@IntegrationComponentScan
@Configuration
@EnableBinding({GatewayApplication.GatewayChannels.class})
public class GatewayApplication {
	
	interface GatewayChannels {

	    String TO_UPPERCASE_REPLY = "replayChannel";
	    String TO_UPPERCASE_REQUEST = "conector1MessagesChannel";

	    @Input(TO_UPPERCASE_REPLY)
	    SubscribableChannel toUppercaseReply();

	    @Output(TO_UPPERCASE_REQUEST)
	    MessageChannel toUppercaseRequest();
	  }

	  @MessagingGateway
	  public interface StreamGateway {
	    @Gateway(requestChannel = ENRICH, replyChannel = GatewayChannels.TO_UPPERCASE_REPLY, requestTimeout=1000, replyTimeout = 10000)
	    String process(Message<String> messsage);
	  }

	  private static final String ENRICH = "enrich";


	  @Bean
	  public IntegrationFlow headerEnricherFlow() {
	    return IntegrationFlows.from(ENRICH).enrichHeaders(HeaderEnricherSpec::headerChannelsToString)
	        .channel(GatewayChannels.TO_UPPERCASE_REQUEST).get();
	  }
	  
	  
	  @Transformer(inputChannel = GatewayChannels.TO_UPPERCASE_REPLY)
	  public String transform(byte[] bytes) {
		  System.out.println("TRANSFORMA??::::::::::::::::"+new String(bytes));
		  return new String(bytes);
	  }
	  
	  @Bean
	  public MessageReply messageReply() {
	      return new MessageReply();
	  }

	  @MessageEndpoint
	  public class MessageReply {

		  @ServiceActivator(inputChannel = GatewayChannels.TO_UPPERCASE_REPLY)
		  public String getReply(Message<String> myMessage) {
			  System.out.println("LLEGO ESTOOOOOO::::::::::::::::"+myMessage.getPayload());
		      return myMessage.getPayload().toString();
		  }

	  } 	  
	  

}
