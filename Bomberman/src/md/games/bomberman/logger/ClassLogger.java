/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author Marc
 */
public final class ClassLogger
{
    private final String className;
    
    public ClassLogger(final Class clazz)
    {
        className = clazz.getSimpleName();
    }
    
    public final void log(String tag, String message)
    {
        Logger.log0(className,tag,message);
    }
    
    public final void log(String message)
    {
        Logger.log0(className,"Info",message);
    }
    
    public final void warning(String message)
    {
        Logger.log0(className,"Warning",message);
    }
    
    public final void error(String message)
    {
        Logger.log0(className,"ERROR",message);
    }
    
    public final void exception(Throwable th, String message)
    {
        StringWriter w = new StringWriter();
        th.printStackTrace(new PrintWriter(w));
        Logger.log0(className,"EXCEPTION","An exception has been thrown: " +
                message + ".\n" + w.toString());
    }
    
    public final void exception(Throwable th)
    {
        exception(th,"");
    }
}
