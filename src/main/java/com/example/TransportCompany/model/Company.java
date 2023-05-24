package com.example.TransportCompany.model;

import com.example.TransportCompany.sql.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "company")
@Data
public class Company extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "company_id")
    int companyId;
    @NotBlank
   String name;
    @NotBlank
   String address;
    @NotBlank
    @Pattern(regexp = "(^$|[0-9]{9})",message = "NIP must be 9 digits")
    String nip;
    @NotBlank
   String phoneNumber;
}
