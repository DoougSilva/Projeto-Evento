package com.ms.emailservice.resources;

import com.ms.emailservice.dtos.EmailDto;
import com.ms.emailservice.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
public class EmailResource {

    private final EmailService emailService;

    @PostMapping("/sending-email")
    public ResponseEntity<EmailDto> sendingEmail(@RequestBody @Valid EmailDto emailDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emailService.sendEmail(emailDto));
    }
}
