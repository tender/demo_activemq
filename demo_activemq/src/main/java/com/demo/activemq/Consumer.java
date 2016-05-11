package com.demo.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer {
	
	private static String URL=ActiveMQConnection.DEFAULT_BROKER_URL;
	private static String SUBJECT="TESTMQ";
	public	static void main(String[] args) throws JMSException{
		ConnectionFactory connectionFactory=new ActiveMQConnectionFactory(URL);
		Connection connection=connectionFactory.createConnection();
		connection.start();
		
		Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
		
		
		Destination destination=session.createQueue(SUBJECT);
		
		MessageConsumer consumer=session.createConsumer(destination);
		
		Message message;
		try{
			while(true){
				message=consumer.receive();
				if(message instanceof TextMessage){
					TextMessage textMessage=(TextMessage) message;
			
					System.out.println("Receivedage '"+textMessage.getText()+"'");
				}
			Thread.sleep(3000);
			}
		}
		catch(Exception e){
			
		}
		connection.stop();
		connection.close();
		
		
	}
}
