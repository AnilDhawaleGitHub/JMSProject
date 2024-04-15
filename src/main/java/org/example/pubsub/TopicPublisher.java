package org.example.pubsub;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicPublisher {

    public static void main(String[] args) throws JMSException {
        // Create a ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        // Create a Connection
        Connection connection = connectionFactory.createConnection();

        // Start the Connection
        connection.start();

        // Create a Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create a Topic (if it does not exist)
        // Note: Replace "MyTopic" with your desired topic name
        // session.createTopic("MyTopic");

        // Create a MessageProducer from the Session to the Topic
        MessageProducer producer = session.createProducer(session.createTopic("MyTopicForPubSub"));
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        // Create a TextMessage




        TextMessage message = session.createTextMessage("Hello, this is a JMS message!");

        // Set Message Header
        message.setJMSMessageID("ID:123456789");
        message.setJMSTimestamp(System.currentTimeMillis());

        // Set Message Properties
        message.setStringProperty("MessageType", "Greeting");
        message.setIntProperty("Priority", 1);



        // Send the Message
        producer.send(message);

        System.out.println("Sent message: " + message.getText());

        // Close the resources
        producer.close();
        session.close();
        connection.close();
    }
}

