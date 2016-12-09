/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.logger;

import java.io.Closeable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

/**
 *
 * @author Marc
 */
public abstract class AbstractLogger implements Closeable, AutoCloseable
{
    protected boolean __date = true;
    
    public AbstractLogger()
    {
        
    }
    
    protected static final String
            INFO_TAG = "Info",
            WARNING_TAG = "Warning",
            ERROR_TAG = "ERROR",
            EXCEPTION_TAG = "EXCEPTION";
    
    public final void setShowDate(boolean flag) { __date = flag; }
    
    protected static String printDate()
    {
        Calendar c = Calendar.getInstance();
        StringBuilder sb = new StringBuilder(8);
        sb.append(c.get(Calendar.HOUR_OF_DAY))
                .append(':').append(c.get(Calendar.MINUTE))
                .append(':').append(c.get(Calendar.SECOND));
        return sb.toString();
    }
    
    protected abstract void log0(String className, String tag, String message);
    
    public final void log(String tag, String message)
    {
        log0(null,tag,message);
    }
    
    public final void log(String message)
    {
        log0(null,INFO_TAG,message);
    }
    
    public final void warning(String message)
    {
        log0(null,WARNING_TAG,message);
    }
    
    public final void error(String message)
    {
        log0(null,ERROR_TAG,message);
    }
    
    public final void exception(Throwable th, String message)
    {
        StringWriter w = new StringWriter();
        th.printStackTrace(new PrintWriter(w));
        log0(null,EXCEPTION_TAG,"An exception has been thrown: " +
                message + ".\n" + w.toString());
    }
    
    public final void exception(Throwable th)
    {
        exception(th,"");
    }
}
