/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.script;

import nt.lpl.LPLGlobals;
import nt.lpl.types.LPLFunction;
import nt.lpl.types.LPLObject;
import nt.lpl.types.LPLValue;
import static nt.lpl.types.LPLValue.UNDEFINED;

/**
 *
 * @author Asus
 */
public class Script
{
    private final ScriptManager manager;
    private String name;
    private CompiledData compiledCode;
    private String code;
    private boolean compiled;
    
    Script(ScriptManager manager, String name)
    {
        this.manager = manager;
        this.name = name;
        compiledCode = null;
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
    
    final void setCompiledCode(LPLFunction script, LPLGlobals globals)
    {
        this.compiledCode = script != null && globals != null
                ? new CompiledData(script,globals)
                : null;
    }
    
    final void initiateCompiledCode()
    {
        if(compiledCode != null)
            compiledCode.initiate();
    }
    
    final void setCompiled(boolean compiled) { this.compiled = compiled; }
    final boolean isCompiled() { return compiled; }
    
    public final LPLValue execute(LPLObject executor)
    {
        if(executor == null)
            throw new NullPointerException();
        return manager.executeScript(executor,this);
    }
    public final LPLValue execute(LPLObject executor, LPLValue arg0)
    {
        if(executor == null)
            throw new NullPointerException();
        return manager.executeScript(executor,this,arg0);
    }
    public final LPLValue execute(LPLObject executor, LPLValue arg0, LPLValue arg1)
    {
        if(executor == null)
            throw new NullPointerException();
        return manager.executeScript(executor,this,arg0,arg1);
    }
    public final LPLValue execute(LPLObject executor, LPLValue arg0, LPLValue arg1, LPLValue arg2)
    {
        if(executor == null)
            throw new NullPointerException();
        return manager.executeScript(executor,this,arg0,arg1,arg2);
    }
    public final LPLValue execute(LPLObject executor, LPLValue arg0, LPLValue arg1, LPLValue arg2, LPLValue arg3)
    {
        if(executor == null)
            throw new NullPointerException();
        return manager.executeScript(executor,this,arg0,arg1,arg2,arg3);
    }
    public final LPLValue execute(LPLObject executor, LPLValue[] args)
    {
        if(executor == null)
            throw new NullPointerException();
        return manager.executeScript(executor,this,args);
    }
    
    final LPLValue execute() { return compiledCode.execute(); }
    final LPLValue execute(LPLValue arg0) { return compiledCode.execute(arg0); }
    final LPLValue execute(LPLValue arg0, LPLValue arg1) { return compiledCode.execute(arg0,arg1); }
    final LPLValue execute(LPLValue arg0, LPLValue arg1, LPLValue arg2) { return compiledCode.execute(arg0,arg1,arg2); }
    final LPLValue execute(LPLValue arg0, LPLValue arg1, LPLValue arg2, LPLValue arg3) { return compiledCode.execute(arg0,arg1,arg2,arg3); }
    final LPLValue execute(LPLValue[] args) { return compiledCode.execute(args); }
    
    public static final LPLObject OBJECT_INVALID = new LPLObject();
    
    static final class CompiledData
    {
        private final LPLFunction script;
        private final LPLGlobals selfGlobals;
        private LPLFunction mainFunction = null;
        private boolean initiated = false;
        
        private CompiledData(LPLFunction script, LPLGlobals selfGlobals)
        {
            this.script = script;
            this.selfGlobals = selfGlobals;
        }
        
        public final void initiate()
        {
            if(initiated)
                return;
            script.call();
            LPLValue mainValue = selfGlobals.getGlobalValue("main");
            mainFunction = mainValue != null && mainValue.isFunction() ? mainValue.toLPLFunction() : null;
        }
        
        public final LPLValue execute() { return mainFunction != null ? mainFunction.callLimited() : UNDEFINED; }
        public final LPLValue execute(LPLValue arg0) { return mainFunction != null ? mainFunction.callLimited(arg0) : UNDEFINED; }
        public final LPLValue execute(LPLValue arg0, LPLValue arg1) { return mainFunction != null ? mainFunction.callLimited(arg0,arg1) : UNDEFINED; }
        public final LPLValue execute(LPLValue arg0, LPLValue arg1, LPLValue arg2) { return mainFunction != null ? mainFunction.callLimited(arg0,arg1,arg2) : UNDEFINED; }
        public final LPLValue execute(LPLValue arg0, LPLValue arg1, LPLValue arg2,
                LPLValue arg3) { return mainFunction != null ? mainFunction.callLimited(LPLValue.varargsOf(arg0,arg1,arg2,arg3)) : UNDEFINED; }
        public final LPLValue execute(LPLValue[] args) { return mainFunction != null ? mainFunction.callLimited(LPLValue.varargsOf(args)) : UNDEFINED; }
    }
}
