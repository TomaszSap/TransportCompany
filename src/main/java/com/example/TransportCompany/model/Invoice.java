package com.example.TransportCompany.model;

import com.example.TransportCompany.Mongo.DaoModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Document(collection = "invoice")
public class Invoice extends DaoModel {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    @JsonIgnore
    private String objectId;
    private LocalDateTime dateOfService;
    private Date dateOfIssue;
    private Date dateOfPayment;
    @NotBlank
    private String service;
    @NotBlank
    private int clientId;
    private BigDecimal totalAmount;
    @NotBlank
    private int courseId;
    @NotBlank
    private int vat;
    @NotBlank
    int companyId;
}
