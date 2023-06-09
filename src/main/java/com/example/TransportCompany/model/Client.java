package com.example.TransportCompany.model;

import com.example.TransportCompany.sql.BaseEntity;
import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Entity(name = "clients")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "clientId")

public class Client extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "client_id")
    private int clientId;
    @NotBlank(message = "Name must be not blank")
    @Size(min = 2, max = 100, message = "Name should be min 2 symbols and less than 100")
    private String name;
    @NotBlank(message = "Surname must be not blank")
    @Size(min = 2, max = 100, message = "Surname should be min 2 symbols and less than 100")
    private String surname;
    @NotBlank(message = "Email must not be blank")
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
            "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
            "|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\" +
            "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?" +
            ":[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", message = "Please provide a valid email address")
    String email;
    @NotBlank(message = "Confirm email must not be blank")
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
            "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
            "|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\" +
            "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?" +
            ":[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", message = "Please provide a valid email address")
    @Transient
    @JsonProperty("confirmEmail")
    private String confirmEmail;
    @NotBlank(message = "NIP must not be blank")
    @Column(unique = true)
    @Pattern(regexp = "(^$|[0-9]{9})", message = "NIP must be 9 digits")
    private String NIP;
    @NotBlank(message = "Address must not be blank")
    @NotNull
    private String address;
    @NotBlank(message = "Company name must be not blank")
    @Size(min = 2, max = 100, message = "Company name  should be min 2 symbols and less than 100")
    private String company;
    @OneToMany(mappedBy = "clientsId", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity = Course.class)
    @JsonIgnoreProperties(value = {"clientsId", "hibernateLazyInitializer"}) //to avoid json LOOP

    private Set<Course> courses;
}
