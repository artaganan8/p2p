package com.xmg.p2p.restful;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by machao on 2017/9/14.
 */
@Setter@Getter
public class Employee {
    private Long id;
    private String username;

    public Employee(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Employee() {
    }
}
