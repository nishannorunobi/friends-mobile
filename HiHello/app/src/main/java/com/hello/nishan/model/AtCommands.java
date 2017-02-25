package com.hello.nishan.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nishan on 2/19/17.
 */
public class AtCommands {
    public static Map<String,String> requestToAgInHF = new HashMap<>();
    public static Map<String,String> responseFromAGInHF = new HashMap<>();

   static {
       requestToAgInHF.put(AtTypeEnum.SERVICE_LABEL_CONNECTION_INIT.name(),
                "1111111100000000000000000000000000000000");
   }
}
