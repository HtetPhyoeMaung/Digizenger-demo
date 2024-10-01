package com.edusn.Digizenger.Demo.utilis;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlConverter {
    public static URL convertToUrl(String stringUrl) throws MalformedURLException {
        return new URL(stringUrl);
    }
}
