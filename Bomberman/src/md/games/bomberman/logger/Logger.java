/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 *
 * @author Marc
 */
public final class Logger
{
    private Logger() {}
    
    private static ExternLogger __log = null;
    
    
    public static final void createLogger(OutputStream out)
    {
        if(__log != null)
            throw new IllegalStateException("Logger has already created");
        __log = new ExternLogger(out);
    }
    
    public static final void createLogger(Writer w)
    {
        if(__log != null)
            throw new IllegalStateException("Logger has already created");
        __log = new ExternLogger(w);
    }
    
    public static final void createLogger(File file)
    {
        if(__log != null)
            throw new IllegalStateException("Logger has already created");
        try
        {
            __log = new ExternLogger(file);
        }
        catch(FileNotFoundException ex)
        {
            ex.printStackTrace(System.err);
            __log = null;
        }
    }
    
    public static final void destroyLogger()
    {
        if(__log == null) return;
        try { __log.close(); }
        catch(IOException ex)
        {
            ex.printStackTrace(System.err);
        }
        __log = null;
    }
    
    public static final void setShowDate(boolean flag)
    {
        if(__log == null)
            return;
        __log.setShowDate(flag);
    }
    
    static void log0(String className, String tag, String message)
    {
        if(__log == null) return;
        __log.log0(className,tag,message);
    }
    
    public static final void log(String tag, String message)
    {
        if(__log == null) return;
        __log.log(tag,message);
    }
    
    public static final void log(String message)
    {
        if(__log == null) return;
        __log.log(message);
    }
    
    public static final void warning(String message)
    {
        if(__log == null) return;
        __log.warning(message);
    }
    
    public static final void error(String message)
    {
        if(__log == null) return;
        __log.error(message);
    }
    
    public static final void exception(Throwable th, String message)
    {
        if(__log == null) return;
        __log.exception(th,message);
    }
    
    public static final void exception(Throwable th)
    {
        if(__log == null) return;
        __log.exception(th);
    }
}
