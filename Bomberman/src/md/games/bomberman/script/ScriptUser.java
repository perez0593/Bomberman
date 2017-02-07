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
        return s != null ? s.execute(this,id) : UNDEFINED;
    }
    public final LPLValue executeScript(ScriptId id, LPLValue... args)
    {
        Script s = getScript(id);
        return s != null ? s.execute(this,id,args) : UNDEFINED;
    }
}
