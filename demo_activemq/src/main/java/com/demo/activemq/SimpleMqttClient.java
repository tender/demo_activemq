package com.demo.activemq;


import static org.fusesource.hawtbuf.UTF8Buffer.utf8;

import java.util.concurrent.TimeUnit;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.junit.Test; 
import static org.junit.Assert.assertEquals; 
import static org.junit.Assert.assertNotNull; 

public class SimpleMqttClient {
	private static String URL="tcp://127.0.0.1:1883";
	private static String TOPIC_NAME="TESTMQ";
	private static String CLIENT_ID="M2MIO_THING";


	@Test
	public void testPublicMessage() throws Exception{

		MQTT consumerClient = createMQTTTcpConnection("consumer", true);
	    consumerClient.setConnectAttemptsMax(0);
	    consumerClient.setReconnectAttemptsMax(0);
	    BlockingConnection consumeConnection = consumerClient.blockingConnection();
	    consumeConnection.connect();

	    String topic = "foo";

	    Topic[] topics = {new Topic(utf8(topic), QoS.values()[1])};
	    consumeConnection.subscribe(topics);

	    MQTT producerClient = createMQTTTcpConnection("producer", true);

	    producerClient.setConnectAttemptsMax(0);
	    producerClient.setReconnectAttemptsMax(0);
	    BlockingConnection producerConnection = producerClient.blockingConnection();
	    producerConnection.connect();

	    for (int i = 0; i < 100; i++) {
	        String payload = "Test Message: " + i;
	        producerConnection.publish(topic, payload.getBytes(), QoS.values()[1], false);
	        Message message = consumeConnection.receive(10, TimeUnit.SECONDS);
	        System.out.println(new String(message.getPayload()));
	        assertNotNull("Should get a message", message);
	        assertEquals(payload, new String(message.getPayload()));
	    }
	    consumeConnection.disconnect();
	    producerConnection.disconnect();
	}

	
	private MQTT createMQTTTcpConnection(String clientId, boolean clean) throws Exception { 
		MQTT mqtt = new MQTT(); 
		mqtt.setConnectAttemptsMax(1); 
		mqtt.setReconnectAttemptsMax(0); 
		if (clientId != null) { 
			mqtt.setClientId(clientId); 
		} 
		mqtt.setCleanSession(clean); 
		mqtt.setHost(URL); 
		return mqtt; 
	} 


}