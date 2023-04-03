package com.example.TransportCompany.model;

import com.example.TransportCompany.Mongo.DaoModel;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Date;

@Data
@Document(collection = "invoice")
public class Invoice extends DaoModel {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private String objectId;
    private Date dateOfService;
    private Date dateOfIssue;
    private Date dateOfPayment;

    private String value;
    private String vat;
    private String clientId;

    //todo ew string clientId

}
