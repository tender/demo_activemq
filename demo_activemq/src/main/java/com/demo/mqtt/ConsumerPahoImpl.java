package com.demo.mqtt;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.fusesource.mqtt.client.QoS;

public class ConsumerPahoImpl implements Consumer,MqttCallback{
	
	private int qos = 0;
	private Queue<String> messages;
	private boolean isStopReceive=false;
	private String clientId;
	
	private MqttClient mqtt;
	
	final CountDownLatch latch = new CountDownLatch(1);
	
	public ConsumerPahoImpl(String clientId,Queue<String> messages){
		this.clientId=clientId;
		this.messages=messages;
	}
	
	
	public void createConnection(String url, boolean clean) throws Exception {
		MemoryPersistence persistence = new MemoryPersistence();
		mqtt = new MqttClient(url, clientId, persistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(clean);
		System.out.println("Connecting to broker: "+url);
		mqtt.connect(connOpts);

		
		System.out.println("Connected");		
	}
	
	


	public void disconnection() throws Exception {
		// TODO Auto-generated method stub
		if(mqtt.isConnected()){
			mqtt.close();
			mqtt.disconnect();
		}
	}


	public void connectionLost(Throwable cause) {
		System.out.println("Connection to Solace broker lost!" + cause.getMessage());
        latch.countDown();
		
	}


	public void messageArrived(String topic, MqttMessage message) throws Exception {
		String time = new Timestamp(System.currentTimeMillis()).toString();
        System.out.println("\nReceived a Message!" +
            "\n\tTime:    " + time +
            "\n\tTopic:   " + topic +
            "\n\tMessage: " + new String(message.getPayload()) +
            "\n\tQoS:     " + message.getQos() + "\n");

        messages.offer(new String(message.getPayload()));
        //latch.countDown();
	}


	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}


	public void receive(String topic) throws Exception {
		mqtt.setCallback(this);
		mqtt.subscribe(new String[]{topic},new int[]{qos});

        // Wait for the message to be received
        try {
            latch.await(); // block here until message received, and latch will flip
        } catch (InterruptedException e) {
            System.out.println("I was awoken while waiting");
        }
        
        // Disconnect the client
        mqtt.disconnect();
        System.out.println("Exiting");

		
	}
	
	

	public List<String> getMessages() {
		List<String> result=new ArrayList<String>();
//		while(messages.peek() != null) {
//            Message message = messages.poll();
//            System.out.println(new String(message.getPayload()));
//            result.add(message);
//        }
		return (List<String>)result;
	}


	public String provideClientId() {
		return clientId;
	}
	
	

	public boolean isStopReceive() {
		return isStopReceive;
	}

	public void setStopReceive(boolean isStopReceive) {
		this.isStopReceive = isStopReceive;
	}

	public void setMessages(Queue<String> messages) {
		this.messages = messages;
	}

	public String getClientId() {
		return clientId;
	}

}
