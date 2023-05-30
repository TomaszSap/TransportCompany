package com.example.TransportCompany.Mongo;

public enum CollectionEnum {
    INVOICES("invoice");

    private final String name;

    CollectionEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
