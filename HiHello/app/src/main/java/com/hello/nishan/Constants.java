package com.hello.nishan;


import android.os.ParcelUuid;

import java.util.UUID;

/**
 * Created by root on 2/14/17.
 */
public abstract class Constants {
    public static String samsung_mac = "94:35:0Ac5:AE:86";
    public static String SAMSUNG_GT_MAC = "14:1F:78:34:44:28";
    // Name for the SDP record when creating server socket
    public static final String NAME_SECURE = "Hands free AG";
    public static final String NAME_INSECURE = "BluetoothChatInsecure";
    // Unique UUID for this application
    public static final UUID UUID_HFP = UUID.fromString("0000111E-0000-1000-8000-00805F9B34FB"); // UUID for Hands free profile
    public static final UUID UUID_HFP_AG = UUID.fromString("0000111F-0000-1000-8000-00805F9B34FB");
    public static final UUID UUID_HSP = UUID.fromString("00001108-0000-1000-8000-00805F9B34FB"); // UUID for Hands free profile
    public static final UUID UUID_HSP_AG = UUID.fromString("00001112-0000-1000-8000-00805F9B34FB");

    public static final ParcelUuid HSP =
            ParcelUuid.fromString("00001108-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid HSP_AG =
            ParcelUuid.fromString("00001112-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid Handsfree =
            ParcelUuid.fromString("0000111E-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid Handsfree_AG =
            ParcelUuid.fromString("0000111F-0000-1000-8000-00805F9B34FB");

    public static final int  REQUEST_ENABLE      = 0x1;
    public static final int  REQUEST_DISCOVERABLE  = 0x2;
}
