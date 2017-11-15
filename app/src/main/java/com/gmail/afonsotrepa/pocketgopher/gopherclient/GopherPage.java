package com.gmail.afonsotrepa.pocketgopher.gopherclient;

import android.app.Activity;
import android.util.Log;

import java.io.Serializable;

/**
 *
 */

public class GopherPage implements Serializable {
    public String server;
    public Integer port;
    public Character type;
    public String selector;
    public  String url;

    public Class activity;

    public GopherPage(String server, Integer port, Character type, String selector) {
        this.server = server;
        this.port = port;
        this.type = type;
        this.selector = selector;

        this.url = server+":"+String.valueOf(port)+"/"+type.toString()+selector;
    }

    public GopherPage(String url) {
        //remove "gopher://" from the beginning of the url if it's present there
        if (url.indexOf("gopher://") == 0) {
            url = url.replaceFirst("gopher://", "");
        }

        //get the host and the selector
        String host;
        String path;
        if (url.matches("(.*)/(.*)")) {
            host = url.substring(0, url.indexOf("/"));
            path = url.substring(url.indexOf("/") + 1);
        } else {
            host = url;
            path = null;
        }

        //get the server and the port (if it's explicit)
        if (host.contains(":")) {
            this.server = host.substring(0, host.indexOf(":"));
            this.port = Integer.parseInt(host.substring(host.indexOf(":")+1));
        } else {
            this.server = host;
            this.port = 70; //default port
        }

        //get the type and selector
        if (path != null) {
            this.type = path.charAt(0);
            this.selector = path.substring(1);
        } else {
            this.type = '1';
            this.selector = "";
        }


        this.activity = activityToCall(this.type);
    }


    public static Class activityToCall(Character type){
        //determine which activity to call
        switch (type) {
            case '0':
                return TextFileActivity.class;

            case '1':
                return MenuActivity.class;

            case '7':
                return SearchActivity.class;

            case 'g': //gif
            case 'I':
                return ImageActivity.class;

            case 'h': //html
                return HtmlActivity.class;

            case ';': //video
                return VideoActivity.class;

            default:
                throw new RuntimeException("Invalid type");
        }
    }

}