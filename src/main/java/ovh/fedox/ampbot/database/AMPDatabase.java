package ovh.fedox.ampbot.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ovh.fedox.ampbot.AMPConfig;
import ovh.fedox.ampbot.core.AMPClient;
import ovh.fedox.ampbot.database.models.Guild;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/29/2024 7:42 AM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public class AMPDatabase {

    private final Logger logger = LoggerFactory.getLogger(AMPDatabase.class);

    private final AMPClient client;

    private final String databaseName;

    public AMPDatabase(AMPClient client) {
        this.client = client;
        databaseName = client.getConfig().getString(AMPConfig.DATABASE_NAME);
    }

    public void init() {
        logger.info("Establishing connection to the database..");

        String uri = client.getConfig().getString(AMPConfig.MONGODB_URI);

        MongoClient mongoClient = MongoClients.create(uri);

        try {
            mongoClient.listDatabaseNames().first();
            logger.info("Connected to the database");
            client.setMongoClient(mongoClient);
        } catch (Exception e) {
            mongoClient.close();
            logger.error("Failed to connect to the database", e);
            System.exit(1);
        }
    }

    public boolean collectionExists(String collectionName) {
        return client.getMongoClient().getDatabase(databaseName).listCollectionNames().into(new ArrayList<>()).contains(collectionName);
    }

    public <T> void insertModel(T model, String collectionName) {
        MongoCollection<Document> collection = client.getMongoClient()
                .getDatabase(databaseName)
                .getCollection(collectionName);

        Document doc = new Document();
        doc.put("_id", new ObjectId());

        Arrays.stream(model.getClass().getDeclaredMethods())
                .filter(method -> method.getName().startsWith("get") && method.getParameterCount() == 0)
                .forEach(method -> {
                    String fieldName = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                    try {
                        Object value = method.invoke(model);
                        doc.put(fieldName, value);
                    } catch (Exception e) {
                        logger.error("Error getting field value", e);
                    }
                });

        ReplaceOptions options = new ReplaceOptions().upsert(true);

        collection.replaceOne(Filters.eq("_id", doc.get("_id")), doc, options);

        logger.info("Inserted/Updated {} with _id: {} in collection: {}",
                model.getClass().getSimpleName(), doc.get("_id"), collectionName);
    }

    private <T> T findBy(String collectionName, String key, Object value, Class<T> modelClass) {
        MongoCollection<Document> collection = client.getMongoClient()
                .getDatabase(databaseName)
                .getCollection(collectionName);

        Document doc = collection.find(Filters.eq(key, value)).first();

        if (doc == null) {
            return null;
        }

        try {
            Constructor<T> constructor = modelClass.getDeclaredConstructor();
            T model = constructor.newInstance();

            for (Field field : modelClass.getDeclaredFields()) {
                String fieldName = field.getName();
                Class<?> fieldType = field.getType();

                try {
                    field.setAccessible(true);

                    if (fieldType == String.class) {
                        field.set(model, doc.getString(fieldName));
                    } else if (fieldType == long.class || fieldType == Long.class) {
                        field.set(model, doc.getLong(fieldName));
                    } else if (fieldType == int.class || fieldType == Integer.class) {
                        field.set(model, doc.getInteger(fieldName));
                    } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                        field.set(model, doc.getBoolean(fieldName));
                    } else if (fieldType == ObjectId.class) {
                        field.set(model, doc.getObjectId(fieldName));
                    }
                } catch (IllegalAccessException e) {
                    logger.error("Error setting field {}", fieldName, e);
                }
            }

            return model;
        } catch (Exception e) {
            logger.error("Error creating model instance", e);
            return null;
        }
    }

    public Guild findGuild(String key, Object value) {
        return findBy("guilds", key, value, Guild.class);
    }


}
