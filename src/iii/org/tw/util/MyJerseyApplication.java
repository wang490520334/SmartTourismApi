package iii.org.tw.util;
 
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.message.DeflateEncoder;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.EncodingFilter;
 

@ApplicationPath("api/v1")
public class MyJerseyApplication extends ResourceConfig{
    public MyJerseyApplication(){
        packages("iii.org.tw.controller").register(MultiPartFeature.class);
        packages("iii.org.tw.auth");
        packages("iii.org.tw.util");
        packages("com.wordnik.swagger.jersey.listing");
        registerClasses(EncodingFilter.class,
                GZipEncoder.class,
                DeflateEncoder.class);
    }
}