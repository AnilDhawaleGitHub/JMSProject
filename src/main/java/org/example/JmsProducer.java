package org.example;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

public class JmsProducer {
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



            // Create producer
            MessageProducer producer = session.createProducer(destination);

           /* for(int i=0;i<10;i++){

                TextMessage message = session.createTextMessage("Hello, this is a test message! = " +i);

                // Send message
                producer.send(message);
                System.out.println("Message sent successfully!");

            }*/
            TextMessage message;
            int count =1;
            while(true){
                System.out.println("Message sent :" + count);
                message = session.createTextMessage("Hello, this is a test message! = " + count++);
                producer.send(message);

                Thread.sleep(TimeUnit.SECONDS.toSeconds(1000)); // Adjust as needed
            }

            // Create message


            // Clean up
           // session.close();
           // connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


