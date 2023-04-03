package com.example.TransportCompany.Mongo;

public enum CollectionEnum {
    INVOICES("invoice");

    private String name;

    CollectionEnum(String invoice) {
        this.name=name;
    }
    public String getName()
    {
        return name;
    }
}
