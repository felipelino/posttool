/*
 * Created on 28/08/2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.posttool.engine;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.jms.core.MessagePostProcessor;

/**
 * Classe para setar o JmsCorrelationId
 * @author Felipe Lino
 */
public class CustomMessagePostProcessor implements MessagePostProcessor
{
	/** JMS Correlation ID */
	private String jmsCorrelationId;
	
	/**
	 * Construtor
	 * @param jmsCorrelationId
	 */
	public CustomMessagePostProcessor(String jmsCorrelationId)
	{
		super();
		this.jmsCorrelationId = jmsCorrelationId;
	}

	/**
	 * @see org.springframework.jms.core.MessagePostProcessor#postProcessMessage(javax.jms.Message)
	 */
	public Message postProcessMessage(Message message) throws JMSException
	{
		if(this.jmsCorrelationId != null)
		{
			message.setJMSCorrelationID(jmsCorrelationId);
		}
		return message;
	}

}
