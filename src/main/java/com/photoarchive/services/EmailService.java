package com.photoarchive.services;

import com.photoarchive.messageCreation.MessageCreator;
import com.photoarchive.messageCreation.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EmailService {

    private BeanFactory beanFactory;
    private JavaMailSender javaMailSender;

    @Bean
    private ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(3, 3, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    @Autowired
    public EmailService(JavaMailSender javaMailSender, BeanFactory beanFactory) {
        this.javaMailSender = javaMailSender;
        this.beanFactory = beanFactory;
    }

    public void sendEmail(SimpleMailMessage message) {
        threadPoolExecutor().execute(() -> {
            javaMailSender.send(message);
            log.info("Email send to " + Objects.requireNonNull(message.getTo())[0]);
        });
    }

    public SimpleMailMessage createMessage(String email, String tokenValue, MessageType messageType) {
        final MessageCreator messageCreator = chooseMessageCreator(messageType);
        return messageCreator.create(email, tokenValue);
    }

    private MessageCreator chooseMessageCreator(MessageType messageType) {
        return beanFactory.getBean(messageType.getComponentName(), messageType.getComponentClass());
    }


}
