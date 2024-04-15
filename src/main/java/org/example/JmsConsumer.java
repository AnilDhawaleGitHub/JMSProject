package org.example;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsConsumer {
    public static void main(String[] args) {
        try {
            // Create connection factory
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            // Create connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create session
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            // Create the destination (queue)
            Destination destination = session.createQueue("myQueue");

            // Create consumer
            MessageConsumer consumer = session.createConsumer(destination);

            Message message;
            while ((message = consumer.receive()) != null) {
                // Process each message
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println("Received message: " + text);
                }
            }



            /*// Receive message
            Message message = consumer.receive();

            // Process message
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                System.out.println("Received message: " + textMessage.getText());
            }*/

            // Clean up
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

