package org.example.pubsub;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicSubscriber {

    public static void main(String[] args) throws JMSException {
        System.out.println("111");
        // Create a ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        // Create a Connection
        Connection connection = connectionFactory.createConnection();

        // Start the Connection
        connection.start();

        // Create a Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create a MessageConsumer from the Session to the Topic
        MessageConsumer consumer = session.createConsumer(session.createTopic("MyTopicForPubSub"));


        // Receive and Process Messages
        Message message = consumer.receive();

        System.out.println(message);

        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("Received message: " + textMessage.getText());
        } else {
            System.out.println("Received message: " + message);
        }

        // Close the resources
        consumer.close();
        session.close();
        connection.close();
    }
}

