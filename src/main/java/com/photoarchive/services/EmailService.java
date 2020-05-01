package com.photoarchive.services;

import com.photoarchive.domain.User;
import com.photoarchive.messageCreators.MessageCreator;
import com.photoarchive.messageCreators.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private BeanFactory beanFactory;
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, BeanFactory beanFactory) {
        this.javaMailSender = javaMailSender;
        this.beanFactory = beanFactory;
    }

    public void sendEmail(User user, MessageType messageType) {
        final MessageCreator messageCreator = chooseMessageCreator(messageType);
        SimpleMailMessage message = messageCreator.createMessage(user);
        javaMailSender.send(message);
        log.info("Email send to " + user.getEmail());
    }

    private MessageCreator chooseMessageCreator(MessageType messageType) {
        return beanFactory.getBean(messageType.getComponentName(), messageType.getComponentClass());
    }


}
