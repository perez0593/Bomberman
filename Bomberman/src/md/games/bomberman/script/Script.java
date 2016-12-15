/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.script;

import nt.lpl.types.LPLFunction;
import nt.lpl.types.LPLObject;
import nt.lpl.types.LPLValue;

/**
 *
 * @author Asus
 */
public class Script extends LPLObject
{
    private String name;
    private LPLFunction closure;
    private String code;
    private boolean compiled;
    
    Script(String name)
    {
        this.name = name;
        closure = null;
        code = "";
        compiled = false;
    }
    
    final void setName(String name) { this.name = name; }
    public final String getName() { return name; }
    
    public final void setCode(String code)
    {
        if(code == null)
            throw new NullPointerException();
        this.code = code;
    }
    public final String getCode() { return code; }
    
    final void setClosure(LPLFunction closure) { this.closure = closure; }
    
    final void setCompiled(boolean compiled) { this.compiled = compiled; }
    final boolean isCompiled() { return compiled; }
    
    public final LPLValue execute(LPLObject executor)
    {
        if(executor == null)
            throw new NullPointerException();
        return closure.call(this,executor).arg0();
    }
}
