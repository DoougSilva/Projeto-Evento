package com.ms.emailservice.services;

import com.ms.emailservice.dtos.EmailDto;
import com.ms.emailservice.entities.EmailEntity;
import com.ms.emailservice.enums.StatusEmail;
import com.ms.emailservice.repositories.EmailRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class EmailService {

    private final EmailRepository emailRepository;

    private final JavaMailSender emailSender;

    public EmailDto sendEmail(EmailDto emailDto) {
        EmailEntity emailEntity = new EmailEntity();
        BeanUtils.copyProperties(emailDto, emailEntity);
        emailEntity.setSendDateEmail(LocalDateTime.now());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailEntity.getEmailFrom());
            message.setTo(emailEntity.getEmailTo());
            message.setSubject(emailEntity.getSubject());
            message.setText(emailDto.getText());
            emailSender.send(message);

            emailEntity.setStatusEmail(StatusEmail.SENT);
        } catch (MailException e) {
            emailEntity.setStatusEmail(StatusEmail.ERROR);
        } finally {
            emailRepository.save(emailEntity);
            emailDto.setStatusEmail(emailEntity.getStatusEmail());
            return emailDto;
        }
    }
}
