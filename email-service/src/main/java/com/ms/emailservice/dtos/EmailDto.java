package com.ms.emailservice.dtos;

import com.ms.emailservice.entities.EmailEntity;
import com.ms.emailservice.enums.StatusEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class EmailDto {

    @NotBlank
    private String ownerRef;
    @NotBlank
    @Email
    private String emailFrom;
    @NotBlank
    @Email
    private String emailTo;
    @NotBlank
    private String subject;
    @NotBlank
    private String text;
    private StatusEmail statusEmail;

}
