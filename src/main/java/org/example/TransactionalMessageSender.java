package org.example;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class TransactionalMessageSender {
    private static final String QUEUE_NAME = "exampleQueue1";
    private static final String BROKER_URL = "tcp://localhost:61616";

    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Create a ConnectionFactory
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

            // Create a Connection
            connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session with transactions enabled
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);

            // Look up the queue
            Queue queue = session.createQueue(QUEUE_NAME);

            // Create a MessageProducer
            MessageProducer producer = session.createProducer(queue);

            // Send messages within the transaction
            for (int i = 0; i < 10; i++) {
                TextMessage message = session.createTextMessage("Transactional Message " + i);
                producer.send(message);
                System.out.println("Sent message: " + message.getText());
                // Simulate failure condition
               /* if (i == 5) {
                    // Throw an exception to simulate message failure
                    throw new RuntimeException("Simulated message sending failure");
                }*/
            }

            // Commit the transaction to ensure messages are delivered
            session.commit();

            // Close resources
            session.close();
        } catch (JMSException | RuntimeException e) {
            e.printStackTrace();
            try {
                // Roll back the transaction in case of an exception
                if (connection != null) {
                    connection.close();
                }
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
        }
    }
}

