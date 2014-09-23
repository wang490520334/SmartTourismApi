package iii.org.tw.util;

import java.io.Serializable;


public class SmartTourismException extends Exception implements Serializable
{
    private static final long serialVersionUID = 1L;
    public SmartTourismException() {
        super();
    }
    public SmartTourismException(String msg)   {
        super(msg);
    }
    public SmartTourismException(String msg, Exception e)  {
        super(msg, e);
    }
}