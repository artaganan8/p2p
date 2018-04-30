package com.xmg.p2p.service.impl;

import com.xmg.p2p.service.Calculator;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by machao on 2017/10/4.
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class CalculatorImpl implements Calculator {

    @Override
    public int add(int a, int b) {
        return a+b;
    }
}
