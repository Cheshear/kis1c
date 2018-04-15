package com.example.authorization.Services;

import com.example.authorization.LoginRequest;
import com.example.authorization.Responses.AuthResponse;
import com.example.authorization.SpringMongoConfig;
import com.example.authorization.TokenObjects.TokenObj;
import com.example.authorization.TokenObjects.ResetPasswordTokenObject;
import com.example.authorization.UserDatabase.User;
import com.example.authorization.UserDatabase.UserCodecProvider;
import com.example.authorization.XMLResourceBundleControl;
import com.example.authorization.token.JwtSettings;
import com.example.authorization.token.JwtTokenFactory;
import com.example.authorization.token.RawAccessJwtToken;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import sun.security.util.AuthResources;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.ne;

@Service
public class UserService {

    private ApplicationContext ctx;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<User> usersCollection;
    private MongoCollection<Document> tokensCollection;
    private MongoOperations mongoOperation;
    private PasswordEncoder passwordEncoder ;
    private MongoCollection<Document> resetPasswordTokensCollection;
    private JwtTokenFactory tokenFactory;
    @Autowired private JwtSettings jwtSettings;
//    ResourceBundle bundle;

    public UserService(){
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromProviders(
                        new UserCodecProvider()),
                MongoClient.getDefaultCodecRegistry());
        MongoClientOptions options = MongoClientOptions.builder()
                .codecRegistry(codecRegistry).build();
        mongoClient = new MongoClient(new ServerAddress(), options);
        database  = mongoClient.getDatabase("local");
        usersCollection = database.getCollection("users", User.class);
        ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
        mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
        tokensCollection = database.getCollection("tokens");
        resetPasswordTokensCollection = database.getCollection("reset_password_tokens_collection");
        passwordEncoder = new BCryptPasswordEncoder();
        tokenFactory = new JwtTokenFactory(jwtSettings);
//        bundle = ResourceBundle.getBundle("collections.xml",
//                new XMLResourceBundleControl());
    }

    public void changePassword(String password, String resetPasswordToken){
        ObjectId userId = getUserByToken(resetPasswordToken);
        Iterable<User> userIterable = usersCollection.find(eq("userId", userId));
        for(User user: userIterable) {
            String pwd = passwordEncoder.encode(password);
            usersCollection.deleteOne(user);
            user.setPassword(pwd);
            System.out.println(user.getEmail());
            usersCollection.insertOne(user);
        }
    }

    public DeferredResult<User> getUser(String token){
        ObjectId userId = getUserByToken(token);
        DeferredResult<User> deferredResult = new DeferredResult<>();
        if(userId != null) {
            Block<User> setUser = new Block<User>() {
                @Override
                public void apply(final User person) {
                    deferredResult.setResult(person);
                }
            };
            usersCollection.find(eq("_id", userId)).forEach(setUser);
            return deferredResult;
        }
        return null;
    }

    public boolean addUser(User user){
        if(isUser(user.getEmail()) == null){
            String pwd = passwordEncoder.encode(user.getPassword());
            user.setPassword(pwd);
            mongoOperation.save(user, "users");
            return true;
        } else {
            return false;
        }
    }

    public void addResetPasswordTokenObject(ResetPasswordTokenObject resetPasswordTokenObject, String email){
        ObjectId userId = isUser(email);
        resetPasswordTokenObject.setUserId(userId);
        mongoOperation.save(resetPasswordTokenObject, "reset_password_tokens_collection");
    }

    public AuthResponse loginUser(LoginRequest request){
        String jwtToken = "";
        String refreshToken = "";
        System.out.println(request.getEmail() + request.getPassword());
        ObjectId userId = isUser(request.getEmail());
        if(userId!=null){
            System.out.println(userId.toString());
            Iterable<User> users = usersCollection.find(eq("_id", userId));
            for(User user: users){
                System.out.println(user.getPassword());
                if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
                    jwtToken = tokenFactory.createAccessJwtToken(user).getToken();
                    refreshToken = tokenFactory.createRefreshToken(user).getToken();
                    return new AuthResponse(true, jwtToken, refreshToken);
                }
            }
        }
        return new AuthResponse(false, jwtToken, refreshToken);
    }

    public ObjectId isUser(String email){
        System.out.println(email);
        Iterable<User> users = usersCollection.find(eq("email", email));
        for(User user: users){
            return user.get_id();
        }
        return null;
    }

    public ObjectId getUserByToken(String token){
        RawAccessJwtToken rawAccessJwtToken = new RawAccessJwtToken(token);
        ObjectId userId = rawAccessJwtToken.parseClaims(jwtSettings.getTokenSigningKey())
                .getBody()
                .get("id", ObjectId.class);
        return userId;
    }
}
