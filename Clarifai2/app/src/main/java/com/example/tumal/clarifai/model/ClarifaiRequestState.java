package com.example.tumal.clarifai.model;

/**
 * Created by TumaL on 1/15/2018.
 */

public class ClarifaiRequestState {
   public static int New= 1;
   public static int Proceesing= 2;
   public static int Done= 3;
   public static String toString(int state) {
      switch (state) {
         case 1:
            return "New";
         case 2:
            return "Processing";
         case 3:
            return "Done";
         default:
            return "Unknown";
      }
   }

}
