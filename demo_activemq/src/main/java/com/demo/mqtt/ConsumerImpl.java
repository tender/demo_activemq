package com.demo.mqtt;

import static org.fusesource.hawtbuf.UTF8Buffer.utf8;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;


public class ConsumerImpl implements Consumer{
	private Queue<Message> messages=new LinkedList<Message>();
	private boolean isStopReceive=false;
	private String clientId;
	private MQTT mqtt;
	public ConsumerImpl(String clientId){
		this.clientId=clientId;
	}
	
	

	public void createConnection(String url, boolean clean) throws Exception {
		
		
	}



	public void disconnection() throws Exception {
		// TODO Auto-generated method stub
		
	}



	public void receive(String topic) throws Exception {
		mqtt.setConnectAttemptsMax(0);
		mqtt.setReconnectAttemptsMax(0);
		BlockingConnection consumerConnection=mqtt.blockingConnection();
		consumerConnection.connect();
		Topic[] topics={new Topic(utf8(topic),QoS.values()[2])};
		byte[] sub=consumerConnection.subscribe(topics);

		while(!isStopReceive){
			
			//Message message=consumerConnection.receive(2, TimeUnit.SECONDS);
			Message message=consumerConnection.receive();
			
			System.out.println("message1:"+new String(message.getPayload()));
//			if(message==null){
//				break;
//			}else{
				System.out.println("TestXXXX");
				messages.offer(message);
//			}
			
			
			//message=null;
			
		}
		
		if(consumerConnection.isConnected()){
			consumerConnection.disconnect();	
		}
		
	}

	public List<Message> getMessages() {
		List<Message> result=new ArrayList<Message>();
		while(messages.peek() != null) {
            Message message = messages.poll();
            System.out.println(new String(message.getPayload()));
            result.add(message);
        }
		return result;
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

	public void setMessages(Queue<Message> messages) {
		this.messages = messages;
	}

	public String getClientId() {
		return clientId;
	}


}
