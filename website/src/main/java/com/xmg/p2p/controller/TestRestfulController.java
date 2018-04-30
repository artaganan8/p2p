package com.xmg.p2p.controller;

import com.xmg.p2p.restful.Employee;
import com.xmg.p2p.restful.Salary;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by machao on 2017/9/14.
 */
@RestController//相当于Controller+ResponseBody
@RequestMapping("emps")
public class TestRestfulController {
    /**
     * 需求1,:获得所有员工信息,使用get 返回员工信息的json列表
     *
     * 需求2,限定只能使用get方法来请求该方法.
     * 使用method参数规定请求方法所需要的Http请求方式
     *
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Employee> list(){
        List<Employee> list = new ArrayList<>();
        list.add(new Employee(1L,"will"));
        list.add(new Employee(2L,"lucy"));
        return  list;
    }

    /**
     * 需求1,获得某一个指定员工的信息,使用get emps/1 放回一个json对象
     *
     * 使用url变量+@pathVariable来完成url地址中可变内容的映射
     *
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Employee getEmp(@PathVariable("id") Long id){
        return new Employee(id,"emp");
    }
    /**
     * 需求:1,得到某一个指定员工的下面的某一个月的工资
     *
     * get emps/1/salarys/2017-04 返回一个工资对象
     */
    @RequestMapping("/{empId}/salarys/{month}")
    public Salary getSalary(@PathVariable("empId") Long empId,
                            @PathVariable("month")@DateTimeFormat(pattern = "yyyy-MM") Date month){
        return  new Salary(month,empId, BigDecimal.TEN);
    }
    /**
     * 需求:1,给一个员工添加一条工资记录
     *
     * post emps/1/salarys
     */
    @RequestMapping(value = "/{empId}/salarys",method = RequestMethod.POST)
    public Salary saveSalary(@PathVariable("empId") Long empId,Salary salary){
        salary.setEmpId(empId);
        return salary;
    }
    /**
     * 需求:1,删除一个员工
     *
     * delete emps/1  删除之后状态码更改为204
     */
    @RequestMapping(value = "/{empId}", method=RequestMethod.DELETE)
    public void deleteEmp(@PathVariable("empId") Long empId, HttpServletResponse response){
        System.out.println("删除员工:"+empId);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
