/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.script;

import nt.lpl.types.LPLObject;
import nt.lpl.types.LPLValue;

/**
 *
 * @author Asus
 */
public abstract class ScriptUser extends LPLObject
{
    public abstract void setScript(ScriptId id, Script script);
    public abstract Script getScript(ScriptId id);
    public abstract void removeScript(ScriptId id);
    public final LPLValue executeScript(ScriptId id)
    {
        Script s = getScript(id);
        return s != null ? s.execute(this) : UNDEFINED;
    }
    public final LPLValue executeScript(ScriptId id, LPLValue arg0)
    {
        Script s = getScript(id);
        return s != null ? s.execute(this,arg0) : UNDEFINED;
    }
    public final LPLValue executeScript(ScriptId id, LPLValue arg0, LPLValue arg1)
    {
        Script s = getScript(id);
        return s != null ? s.execute(this,arg0,arg1) : UNDEFINED;
    }
    public final LPLValue executeScript(ScriptId id, LPLValue arg0, LPLValue arg1, LPLValue arg2)
    {
        Script s = getScript(id);
        return s != null ? s.execute(this,arg0,arg1,arg2) : UNDEFINED;
    }
    public final LPLValue executeScript(ScriptId id, LPLValue arg0, LPLValue arg1, LPLValue arg2, LPLValue arg3)
    {
        Script s = getScript(id);
        return s != null ? s.execute(this,arg0,arg1,arg2,arg3) : UNDEFINED;
    }
    public final LPLValue executeScript(ScriptId id, LPLValue... args)
    {
        Script s = getScript(id);
        return s != null ? s.execute(this,args) : UNDEFINED;
    }
}
