package com.example.TransportCompany.constant;

public enum RoleType {
    ADMIN("ADMIN")
    ,DRIVER("DRIVER"),
ACCOUNTANT("ACCOUNTANT"),
    FORWARDER("FORWARDER") ;
    private String name;

    RoleType(String role) {
        this.name=name;
    }
    public String getName()
    {
        return name;
    }
    public static boolean isValid(String role) {
        for (RoleType kolor : RoleType.values()) {
            if (kolor.name().equals(role) || kolor.getName().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
