/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package md.games.bomberman.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 *
 * @author Marc
 */
public final class Version implements Comparable<Version>
{
    public enum VersionFase
    {
        INDEV, ALPHA, BETA, OFICIAL;

        public static final VersionFase decode(byte id)
        {
            return VersionFase.values()[id];
        }

        public final byte encode()
        {
            return (byte) ordinal();
        }
    }
    private final VersionFase fase;
    private final short[] values;

    public Version(VersionFase fase, short s1, short s2, short s3, short s4)
    {
        this.fase = fase;
        values = new short[4];
        values[0] = s1;
        values[1] = s2;
        values[2] = s3;
        values[3] = s4;
    }
    
    public final VersionFase getFase() { return fase; }
    
    @Override
    public final int compareTo(Version o)
    {
        int flag = fase.compareTo(o.fase);
        if(flag != 0) return flag;
        for(int i=values.length-1;i>=0;i--)
        {
            if((flag = values[i] - o.values[i]) != 0)
                return flag;
        }
        return 0;
    }
    
    private String toCapitalLetter(String str)
    {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
    }
    
    @Override
    public final String toString()
    {
        StringBuilder sb = new StringBuilder();
        if(fase != VersionFase.OFICIAL)
        {
            sb.append(toCapitalLetter(fase.name()))
                    .append("::");
        }
        for(int i=0;i<values.length;i++)
        {
            if(i > 1 && values[i] == 0)
            {
                if(i == 2)
                {
                    if(fase == VersionFase.OFICIAL ||
                            fase == VersionFase.ALPHA)
                        continue;
                }
                if(i == 3)
                {
                    if(fase == VersionFase.OFICIAL ||
                            fase == VersionFase.ALPHA ||
                            fase == VersionFase.BETA)
                        continue;
                }
            }
            sb.append(values[i]).append(".");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
    
    private static byte[] encodeBits(byte[] data, int bits, int offset, int depth)
    {
        for(int i=0;i<depth;i++)
            data[i+offset] = (byte) ((bits >> (i * 8)) & 0xFF);
        return data;
    }
    
    private static int decodeBits(byte[] data, int offset, int length)
    {
        int bits = 0;
        for(int i=0;i<length;i++)
            bits += (data[i+offset] & 0xFF) << (i * 8);
        return bits;
    }
    
    public static final byte[] encode(Version v)
    {
        Objects.requireNonNull(v);
        byte[] data = new byte[9];
        data[0] = v.fase.encode();
        for(int i=0;i<v.values.length;i++)
            encodeBits(data,v.values[i],i*2+1,Short.BYTES);
        return data;
    }
    
    public static final void encode(Version v, OutputStream out) throws IOException
    {
        Objects.requireNonNull(out);
        out.write(encode(v));
    }
    
    public static final Version decode(byte[] data)
    {
        Objects.requireNonNull(data);
        return new Version(VersionFase.decode(data[0]),
            (short) decodeBits(data,1,Short.BYTES),
            (short) decodeBits(data,3,Short.BYTES),
            (short) decodeBits(data,5,Short.BYTES),
            (short) decodeBits(data,7,Short.BYTES));
    }
    
    public static final Version decode(InputStream in) throws IOException
    {
        Objects.requireNonNull(in);
        byte[] data = new byte[9];
        in.read(data);
        return decode(data);
    }
    
    public static final Version valueOf(String str)
            throws IllegalArgumentException
    {
        Objects.requireNonNull(str);
        VersionFase fs = VersionFase.OFICIAL;
        boolean faseSel = false;
        String[] blocks = str.split("::");
        if(blocks.length == 2)
        {
            switch(blocks[0].toLowerCase())
            {
                case "indev":
                    fs = VersionFase.INDEV;
                    faseSel = true;
                    break;
                case "alpha":
                    fs = VersionFase.ALPHA;
                    faseSel = true;
                    break;
                case "beta":
                    fs = VersionFase.BETA;
                    faseSel = true;
                    break;
                case "oficial":
                    fs = VersionFase.OFICIAL;
                    faseSel = true;
                    break;
            }
        }
        else if(blocks.length == 1) {}
        else throw new IllegalArgumentException(str);
        blocks = blocks[blocks.length == 1 ? 0 : 1].split("\\.");
        if(blocks.length < 1 || blocks.length > 4) throw new IllegalArgumentException(str);
        short s1, s2 = 0, s3 = 0, s4 = 0;
        s1 = Short.parseShort(blocks[0]);
        if(blocks.length > 1) s2 = Short.parseShort(blocks[1]);
        if(blocks.length > 2) s3 = Short.parseShort(blocks[2]);
        if(blocks.length > 3) s4 = Short.parseShort(blocks[3]);
        
        if(s1 < 0 || s2 < 0 || s3 < 0 || s4 < 0)
            throw new IllegalArgumentException("Not support negative values.\n" + str);
        if(s1 == 0 && s2 == 0 && s3 == 0)
        {
            if(faseSel && fs != VersionFase.INDEV)
                throw new IllegalArgumentException("Malformed fase verion");
            fs = VersionFase.INDEV;
        }
        else if(s1 == 0 && s2 == 0)
        {
            if(faseSel && fs != VersionFase.ALPHA)
                throw new IllegalArgumentException("Malformed fase verion");
            fs = VersionFase.ALPHA;
        }
        else if(s1 == 0)
        {
            if(faseSel && fs != VersionFase.BETA)
                throw new IllegalArgumentException("Malformed fase verion");
            fs = VersionFase.BETA;
        }
        return new Version(fs,s1,s2,s3,s4);
    }
}
