package com.example.authorization.Services;

import com.example.authorization.SpringMongoConfig;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import static com.mongodb.client.model.Filters.eq;

@Service
public class StorageService {
    String PATH_FOR_AVATARS = "C:/Users/mariia/workspace1/authorization/src/main/resources/static/images/";

    public void store(MultipartFile file, String token){
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
        UserService userService = new UserService();
        System.out.println(token.substring(1,token.length()-1));
        ObjectId userId = userService.getUserByToken(token.substring(1,token.length()-1));
        String pathToFile = fileUpload(file);
        BasicDBObject o = new BasicDBObject();
        o.append("userId", userId).append("photo", pathToFile);
        mongoOperation.save(o, "userAvatars");
    }

    private String fileUpload(MultipartFile file) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String fileName = file.getOriginalFilename();
        File newFile = new File(PATH_FOR_AVATARS + fileName);

        try {
            inputStream = file.getInputStream();

            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }

    public String givePathToAvatar(String token){
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("local");
        MongoCollection<Document> userAvatarsCollection = database.getCollection("userAvatars");
        UserService userService = new UserService();
        DeferredResult<Object> deferredResult = new DeferredResult<>();
        ObjectId userId = userService.getUserByToken(token);
        Iterable<Document> avatars = userAvatarsCollection.find(eq("userId", userId));
        avatars.forEach((Document avatar) -> {
            deferredResult.setResult(avatar.get("photo"));
        });
        return deferredResult.getResult().toString();
    }


}
