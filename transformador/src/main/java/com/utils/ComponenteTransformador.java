package com.utils;

import org.mule.api.MuleEventContext;

import org.mule.DefaultMuleEvent;
import org.mule.api.MuleEvent;
import org.mule.api.lifecycle.Callable;

public class ComponenteTransformador implements Callable {

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		String expressionStr = "#[dw(\" {'root':{'myId':payload.id,'myName':payload.name,'mydescription':payload.description}}\",\"application/xml\")]";
        MuleEvent muleEvent = new DefaultMuleEvent(eventContext.getMessage(), eventContext.getExchangePattern(),eventContext.getFlowConstruct());
        Object response = eventContext.getMuleContext().getExpressionManager().evaluate(expressionStr, muleEvent);
        return response;
	}

}
