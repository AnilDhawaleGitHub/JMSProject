package org.example;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class AcknowledgmentModesExample {
    private static final String QUEUE_NAME = "exampleQueue";
    private static final String BROKER_URL = "tcp://localhost:61616";

    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Create a ConnectionFactory
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

            // Create a Connection
            connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session with AUTO_ACKNOWLEDGE mode
            Session autoAckSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            testAcknowledgmentMode(autoAckSession, "AUTO_ACKNOWLEDGE");

            // Create a Session with CLIENT_ACKNOWLEDGE mode
            Session clientAckSession = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            testAcknowledgmentMode(clientAckSession, "CLIENT_ACKNOWLEDGE");

            // Create a Session with DUPS_OK_ACKNOWLEDGE mode
            Session dupsOkAckSession = connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
            testAcknowledgmentMode(dupsOkAckSession, "DUPS_OK_ACKNOWLEDGE");


        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            // Close connection
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    private static void testAcknowledgmentMode(Session session, String modeName) throws JMSException {
        // Look up the queue
        Queue queue = session.createQueue(QUEUE_NAME);

        // Create a MessageProducer
        MessageProducer producer = session.createProducer(queue);

        // Create a MessageConsumer
        MessageConsumer consumer = session.createConsumer(queue);


        // Send a message
        TextMessage message = session.createTextMessage("Test message with Acknowledgment Mode: " + modeName);
        producer.send(message);
        System.out.println("Sent message: " + message.getText());

        // Receive and acknowledge the message
        Message receivedMessage = consumer.receive();
        System.out.println("Received message: " + ((TextMessage) receivedMessage).getText());
        receivedMessage.acknowledge();


        // Depending on the acknowledgment mode, acknowledge the message
        if (session.getAcknowledgeMode() == Session.CLIENT_ACKNOWLEDGE) {
            //receivedMessage.acknowledge(); // Acknowledge the message explicitly in CLIENT_ACKNOWLEDGE mode
           // System.out.println("Acknowledged message in CLIENT_ACKNOWLEDGE mode");
        }

        // Close resources
        producer.close();
        consumer.close();
        session.close();
    }
}

