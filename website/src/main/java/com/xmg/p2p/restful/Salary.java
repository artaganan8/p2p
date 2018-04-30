package com.xmg.p2p.restful;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by machao on 2017/9/14.
 */
@Setter@Getter
public class Salary {
    @JsonFormat(pattern = "yyyy-MM" ,timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM" )
    private Date month;
    private Long empId;
    private BigDecimal salary;

    public Salary(Date month, Long empId, BigDecimal salary) {
        this.month = month;
        this.empId = empId;
        this.salary = salary;
    }

    public Salary() {
    }
}
