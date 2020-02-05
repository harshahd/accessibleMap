package com.hackathon.accessiblemap;

import java.util.Random;

public class OtpManager {
    public static String experimentValue = "";
    public static boolean hasOtp;
    private int otp = 0;

    public int generateOtp() {
        this.otp = new Random().nextInt(9999);
        if (this.otp < 1000) {
            this.otp = generateOtp();
        }
        return this.otp;
    }
}
