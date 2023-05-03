package com.example.TransportCompany.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrintInvoice extends Invoice{
    private Client client;
}
