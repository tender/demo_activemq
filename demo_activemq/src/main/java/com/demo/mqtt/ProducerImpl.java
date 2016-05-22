package com.demo.mqtt;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

public class ProducerImpl implements Producer{
	private String clientId;
	
	private MQTT mqtt;
	public ProducerImpl(String clientId){
		this.clientId=clientId;
	}
	public void send(String topic,String content) throws Exception {
		mqtt.setConnectAttemptsMax(0); 
		mqtt.setReconnectAttemptsMax(0); 
        BlockingConnection producerConnection = mqtt.blockingConnection(); 
        producerConnection.connect(); 

        String payload = content ; 
        producerConnection.publish(topic, payload.getBytes(), QoS.values()[0], true); 

        if(producerConnection.isConnected()){
        	producerConnection.disconnect();
        }
		
	}

	public void createConnection(String url,boolean clean) throws Exception {
		createConnection(getClientId(),url,clean);
		
	}


    public void disconnection() throws Exception {
		
		
	}
	private MQTT createConnection(String clientId,String url,boolean clean) throws Exception{
        mqtt=new MQTT();
        mqtt.setConnectAttemptsMax(1);
        mqtt.setReconnectAttemptsMax(0);
        mqtt.setUserName("username");
        mqtt.setPassword("password");
        if(clientId != null){
            mqtt.setClientId(clientId);

        }
        mqtt.setCleanSession(clean);
        mqtt.setHost(url);
        return mqtt;

    }
	
	
	public String provideClientId() {
		return clientId;
	}
	public String getClientId() {
		return clientId;
	}



}
