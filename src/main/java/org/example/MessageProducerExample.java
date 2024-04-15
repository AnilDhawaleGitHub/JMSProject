package org.example;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class MessageProducerExample {
    private static final String QUEUE_NAME = "myQueue";

    public static void main(String[] args) {
        try {
            // Set up JNDI properties
            Properties jndiProperties = new Properties();
            jndiProperties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            jndiProperties.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            // Create JNDI context
            Context context = new InitialContext(jndiProperties);

            // Look up the existing queue
            Queue queue = (Queue) context.lookup(QUEUE_NAME);

            // Create a ConnectionFactory
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create a MessageProducer using the existing queue
            MessageProducer producer = session.createProducer(queue);

            // Create and send messages in a loop
            int count = 0;
            while (true) {
                // Create a text message
                TextMessage message = session.createTextMessage("Message " + count++);

                // Send the message
                producer.send(message);
                System.out.println("Sent message: " + message.getText());

                // Pause for a moment (optional)
                Thread.sleep(1000); // Adjust as needed
            }

            // Close resources (usually done in a finally block)
            // producer.close();
            // session.close();
            // connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

