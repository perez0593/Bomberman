/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 * @author Marc
 */
public final class ExternLogger extends AbstractLogger
{
    private BufferedWriter __log;
    
    public ExternLogger(OutputStream out)
    {
        __log = new BufferedWriter(new OutputStreamWriter(out));
    }
    
    public ExternLogger(Writer w)
    {
        __log = new BufferedWriter(w);
    }
    
    public ExternLogger(File file) throws FileNotFoundException
    {
        __log = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
    }
    
    @Override
    protected void log0(String className, String tag, String message)
    {
        if(__log == null) return;
        try
        {
            StringBuilder sb = new StringBuilder();
            sb.append('[').append(className==null?"ROOT":className).append(']')
                    .append('[').append(tag).append(']');
            if(__date)
                sb.append('[').append(printDate()).append(']');
            sb.append(' ').append(message).append('\n');
            __log.write(sb.toString(),0,sb.length());
            __log.flush();
        }
        catch(IOException ex)
        {
            ex.printStackTrace(System.err);
        }
    }
    
    @Override
    public final void close() throws IOException
    {
        if(__log == null)
            return;
        __log.close();
        __log = null;
    }
}
