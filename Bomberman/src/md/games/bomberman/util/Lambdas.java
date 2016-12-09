/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package md.games.bomberman.util;

/**
 *
 * @author Marc
 */
public final class Lambdas
{
    private Lambdas() {}
    
    @FunctionalInterface
    public static interface EmptyFunction
    {
        public void invoke();
    }
    
    @FunctionalInterface
    public static interface VoidFunction<T>
    {
        public void invoke(T arg);
    }
    
    @FunctionalInterface
    public static interface FunctionVoid<R>
    {
        public R invoke();
    }
    
    @FunctionalInterface
    public static interface Function<T, R>
    {
        public R invoke(T arg);
    }
    
    @FunctionalInterface
    public static interface BiVoidFunction<T1, T2>
    {
        public void invoke(T1 arg1, T2 arg2);
    }
    
    @FunctionalInterface
    public static interface BiFunction<T1, T2, R>
    {
        public R invoke(T1 arg1, T2 arg2);
    }
    
    @FunctionalInterface
    public static interface TriVoidFunction<T1, T2, T3>
    {
        public void invoke(T1 arg1, T2 arg2, T3 arg3);
    }
    
    @FunctionalInterface
    public static interface TriFunction<T1, T2, T3, R>
    {
        public R invoke(T1 arg1, T2 arg2, T3 arg3);
    }
    
}
