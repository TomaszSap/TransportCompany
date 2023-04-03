package com.example.TransportCompany.constant;

public enum CourseType {
        HANDLED("HANDLED"),
        CLOSED("CLOSED"),
        OPEN("OPEN");
        private String name;
        CourseType(String name) {
                this.name=name;
        }
        public String getName()
        {
                return name;
        }
        public static boolean isValid(String type) {
                for (CourseType types : CourseType.values()) {
                        if (types.name().equals(type) || types.getName().equals(type)) {
                                return true;
                        }
                }
                return false;
        }
}
