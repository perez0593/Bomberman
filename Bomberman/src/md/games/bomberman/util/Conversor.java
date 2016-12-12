/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.util;

import java.util.Map;
import java.util.stream.Collectors;
import nt.dal.data.DALBlock;
import nt.dal.data.DALData;
import nt.dal.data.DALReference;
import nt.lpl.types.LPLNumber;
import nt.lpl.types.LPLObject;
import nt.lpl.types.LPLValue;
import nt.lpl.types.LPLVarargs;
import nt.lpl.types.objects.LPLMap;
import nt.lpl.types.objects.LPLObjectInstance;

/**
 *
 * @author Asus
 */
public class Conversor
{
    private Conversor() {}
    
    public static final DALData valueOf(LPLValue value)
    {
        switch(value.getType())
        {
            default: return DALData.valueOf(value);
            case UNDEFINED: throw new RuntimeException("Undefined LPL value is invalid");
            case NULL: return DALData.ZERO;
            case NUMBER:
                if(((LPLNumber)value).isDecimal())
                    return DALData.valueOf(value.toJavaDouble());
                return DALData.valueOf(value.toJavaLong());
            case BOOLEAN: return DALData.valueOf(value.toJavaBoolean());
            case STRING: return DALData.valueOf(value.toJavaString());
            case ARRAY: return DALData.valueOf(value.toJavaList().stream()
                        .map(lpl -> valueOf(lpl))
                        .collect(Collectors.toList()));
            case STRUCT: {
                DALBlock block = DALBlock.asObject();
                for(LPLVarargs att : value.createIterable())
                    block.setAttribute(DALData.valueOf(att.arg0().toJavaString()),valueOf(att.arg(1)));
                return block;
            }
            case OBJECT: {
                if(value instanceof LPLObjectInstance)
                {
                    DALBlock block = DALBlock.asObject();
                    for(Map.Entry<LPLValue, LPLValue> e : value.<LPLObjectInstance>toLPLObject())
                        block.setAttribute(valueOf(e.getKey()),valueOf(e.getValue()));
                    return block;
                }
                else if(value instanceof LPLMap)
                {
                    DALBlock block = DALBlock.asObject();
                    for(Map.Entry<LPLValue, LPLValue> e : value.toJavaMap().entrySet())
                        block.setAttribute(valueOf(e.getKey()),valueOf(e.getValue()));
                    return block;
                }
                else if(value instanceof LPLDALReference)
                    return value.<LPLDALReference>toLPLObject().getReference();
                else if(value instanceof LPLDALUserdata)
                    return DALData.valueOf(value.<LPLDALUserdata>toLPLObject().getObject());
                else return DALData.valueOf(value);
            }
        }
    }
    
    public static final LPLValue valueOf(DALData data)
    {
        switch(data.getType())
        {
            case INTEGER: return LPLValue.valueOf(data.toJavaLong());
            case DECIMAL: return LPLValue.valueOf(data.toJavaDouble());
            case BOOLEAN: return LPLValue.valueOf(data.toJavaBoolean());
            case STRING: return LPLValue.valueOf(data.toJavaString());
            case REFERENCE: return new LPLDALReference(data.asDALReference());
            case ARRAY: return LPLValue.valueOf(data.toJavaList().stream()
                    .map(dal -> valueOf(dal))
                    .collect(Collectors.toList()));
            case BLOCK: {
                LPLObjectInstance obj = new LPLObjectInstance();
                for(Map.Entry<DALData, DALData> e : data.toJavaMap().entrySet())
                    obj.setAttribute(valueOf(e.getKey()),valueOf(e.getValue()));
                return obj;
            }
            case USERDATA: {
                Object obj = data.asDALUserdata().get();
                if(obj instanceof LPLValue)
                    return (LPLValue) obj;
                else if(obj instanceof DALData)
                    return valueOf((DALData)obj);
                else return new LPLDALUserdata(obj);
            }
            default: throw new IllegalStateException();
        }
    }
    
    public static class LPLDALReference extends LPLObject
    {
        private final DALReference ref;

        public LPLDALReference(DALReference ref)
        {
            if(ref == null)
                throw new NullPointerException();
            this.ref = ref;
        }

        public final DALReference getReference() { return ref; }
    }
    
    public static class LPLDALUserdata extends LPLObject
    {
        private final Object obj;

        public LPLDALUserdata(Object obj)
        {
            if(obj == null)
                throw new NullPointerException();
            this.obj = obj;
        }

        public final Object getObject() { return obj; }
    }
}
