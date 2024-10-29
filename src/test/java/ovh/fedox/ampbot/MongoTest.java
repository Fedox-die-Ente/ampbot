package ovh.fedox.ampbot;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/29/2024 7:32 AM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public class MongoTest {

    public static void main(String[] args) {
        String uri = "";
        MongoClient mongoClient = MongoClients.create(uri);

        MongoDatabase database = mongoClient.getDatabase("ampbot");
        System.out.println("Connected to the database");

        System.out.println(database.getCollection("test"));
    }

}
