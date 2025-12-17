package com.devjsmh.icea.content_manager_service.content;

public enum ContentStatus {

    DRAFT("draft"),
    PUBLISHED("published");

    private final String value;

    private ContentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean exists(String statusName) {

        for (ContentStatus s : values()) {
            
            if(statusName.equals(s.value)){
                return true;
            }
        }

        return false;

    }

}
