package com.xmg.p2p.base.util;

/**
 * Created by machao on 2017/9/1.
 */

public class Person {
    private Person(){}
    private final static Person p = new Person();
    public static Person getPerson(){
        return p;
    }
}
