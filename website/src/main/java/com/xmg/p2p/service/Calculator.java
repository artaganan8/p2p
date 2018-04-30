package com.xmg.p2p.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by machao on 2017/10/4.
 */
@WebService//标记这个借口为webserice接口
@SOAPBinding(style = SOAPBinding.Style.RPC)//标记使用简单的协议进行传输
public interface Calculator
{
    @WebMethod(operationName = "add")
    @WebResult(name = "int")
    int add(@WebParam(name = "a") int a, @WebParam(name = "b") int b);
}
