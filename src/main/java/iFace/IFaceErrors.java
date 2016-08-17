package iFace;

public class IFaceErrors {

    public static String response(String message) {
        String string;

        string = "{ \"message\": \"" + message + "\"}";

        return string;
    }
}
