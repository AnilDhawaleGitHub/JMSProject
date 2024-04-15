package org.example;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.Hashtable;

public class AcknowledgmentModesExample1 {
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

            // Create a Session with AUTO_ACKNOWLEDGE mode for producer
            Session producerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create a Session with CLIENT_ACKNOWLEDGE mode for consumer
            Session consumerSession = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            // Look up the queue
            Queue queue = producerSession.createQueue(QUEUE_NAME);

            // Create a MessageProducer
            MessageProducer producer = producerSession.createProducer(queue);

            // Create a MessageConsumer
            MessageConsumer consumer = consumerSession.createConsumer(queue);

            // Send a message
            TextMessage message = producerSession.createTextMessage("Test message with CLIENT_ACKNOWLEDGE mode");
            producer.send(message);
            System.out.println("Sent message: " + message.getText());

            int count =1;
            while (true) {
                // Receive the message
                Message receivedMessage = consumer.receive();
                if (receivedMessage != null) {
                    System.out.println("Received message: " + ((TextMessage) receivedMessage).getText());
                    // Do not acknowledge the message, let it remain unacknowledged
                    //receivedMessage.acknowledge();
                    System.out.println("Message not acknowledged, will be redelivered : "+ count++);
                    // Uncomment the next line to simulate not acknowledging the message
                    // continue;
                }
            }

            // Close resources (unreachable in this example due to the infinite loop)
            // producer.close();
            // consumer.close();
            // producerSession.close();
            // consumerSession.close();
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
}
