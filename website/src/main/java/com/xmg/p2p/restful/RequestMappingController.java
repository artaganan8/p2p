package com.xmg.p2p.restful;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by machao on 2017/9/14.
 */
@RestController
@RequestMapping("employees")
public class RequestMappingController {
    /**
     * 需求:将两个请求映射到一个方法上
     *
     * @RequestMapping({"emp","emps"})
     *
     * @RequestMapping(value == path)
     *
     */


    /**
     * 需求:有两个方法都要映射/emps地址,针对json的请求格式返回json格式的数据
     *                              针对xml的请求格式返回xml格式的数据
     *
     */
    @RequestMapping(headers = "accept=application/xml")
    public void getEmpXml(){
        System.out.println("获取xml格式数据");
    }
    //二者具有异曲同工个之妙
    //@RequestMapping(headers = "accept=application/json")
    @RequestMapping(produces = "application/json")
    public void getEmpJSON(){
        System.out.println("获取json格式数据");
    }
    /**
     * 需求使用表单内容创建一个employee对象
     */
    @RequestMapping(method = RequestMethod.POST)
    public Employee createEmployeeByForm(Employee e){
        System.out.println("使用表单创建employee对象");
        return e;
    }

}
