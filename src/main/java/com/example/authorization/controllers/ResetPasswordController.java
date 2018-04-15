package com.example.authorization.controllers;

import com.example.authorization.Responses.ResetPasswordResponse;
import com.example.authorization.Responses.Response;
import com.example.authorization.Services.EmailSender;
import com.example.authorization.Services.FileReader;
import com.example.authorization.Services.UserService;
import com.example.authorization.TokenObjects.ResetPasswordTokenObject;
import com.example.authorization.token.JwtSettings;
import com.example.authorization.token.JwtTokenFactory;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping
public class ResetPasswordController {
    private UserService userService = new UserService();
    private EmailSender emailSender = new EmailSender();
    private JwtTokenFactory tokenFactory;
    private final JwtSettings jwtSettings = new JwtSettings();

    @CrossOrigin(origins = "*")
    @PostMapping("/forgot")
    public ResponseEntity<ResetPasswordResponse> forgotPassword(@RequestBody String email){
        ObjectId userId = userService.isUser(email);
        String resetPasswordToken = "";
        ResetPasswordResponse response = new ResetPasswordResponse();
        Date date;

        String emailBody = null;
        try {
            emailBody = FileReader.usingBufferedReader("C:/Users/mariia/site/src/main/resources/static/htmls/email-pattern.html");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        if(userId == null){
            response.setResetPasswordToken(resetPasswordToken);
            response.setStatus(false);
            response.setResponse("Извините, но пользователя с таким емайлом не существует");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            ResetPasswordTokenObject rptObject = new ResetPasswordTokenObject();
            rptObject.setToken(resetPasswordToken);
            date = new Date();
            c.setTime(date);
            c.add(Calendar.DATE, 1);
            date = c.getTime();
            rptObject.setExpiredTime(dateFormat.format(date));
            rptObject.setUserId(null);
            userService.addResetPasswordTokenObject(rptObject, email);
//            emailSender.setSubject("Восстановление пароля");
//            emailSender.setMsg("<p>Катя еще не сделала</p>");
//            emailSender.setAlternativeMsg("Катя еще не сделала");
            try {
                emailSender.sendEmail(email, "восстановление пароля", emailBody);
            } catch (MessagingException exception){
                exception.printStackTrace();
            }
            tokenFactory = new JwtTokenFactory(jwtSettings);
            resetPasswordToken = tokenFactory.createChangePasswordToken(email, userId).getToken();
            response.setResetPasswordToken(resetPasswordToken);
            response.setStatus(true);
            response.setResponse("Письмо с ссылкой для восстановления пароля отправлено вам на почту");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/change_password")
    public ResponseEntity<Response> changePassword(@RequestBody String password, @RequestHeader String resetPasswordToken){
        userService.changePassword(password, resetPasswordToken);
        Response response = new Response();
        response.setStatus(true);
        response.setResponse("Ваш пароль изменен");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
