package com.sakuno.restaurantmanagesystem.utils;

public class AuthCode {


    public final String code;

    public AuthCode(String restaurantID, String type) {
        code = SakunoUtils.INSTANCE.toMD5Code(type, restaurantID, SakunoUtils.INSTANCE.getMillString());
    }

    public AuthCode(String authCode) {
        code = authCode;
    }

    @Override
    public String toString() {
        return code;
    }
}
