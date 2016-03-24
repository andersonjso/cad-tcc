package br.edu.ufal.util;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

/**
 * Created by andersonjso on 2/23/16.
 */
public class MongoUtils {
    //TODO: Add this line for use in remote, changing the url
   // MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));

    private static MongoClient mongoClient = new MongoClient();
    public static DB databaseTest = mongoClient.getDB("test");
    public static DB databaseMyNodules = mongoClient.getDB("mynodules");
    private static Jongo jongo = new Jongo(databaseTest);
    private static Jongo myNodulesJongo = new Jongo(databaseMyNodules);

    public static MongoCollection exams (){
        return jongo.getCollection("exams");
    }
    public static MongoCollection imagesFiles (){
        return jongo.getCollection("images.files");
    }
    public static MongoCollection nodules () {return myNodulesJongo.getCollection("nodules"); }

}
