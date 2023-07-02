package com.example.TransportCompany.model;

import com.example.TransportCompany.Mongo.DaoModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@JsonIgnoreProperties({"_id"})

@Document(collection = "invoice")
public class Invoice extends DaoModel {

    private String invoiceId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")

    private LocalDateTime dateOfService;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateOfIssue;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date dateOfPayment;
    @NotBlank
    @JsonProperty("service")
    private String service;
    @NotBlank
    @JsonProperty("clientId")
    private String clientId;
    private BigDecimal totalAmount;
    private int courseId;
    private String clientEmail;
    @NotBlank
    @JsonProperty("vat")
    private String vat;
    @NotBlank
    @JsonProperty("companyId")
    String companyId;
}
