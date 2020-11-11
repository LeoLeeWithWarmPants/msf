package com.leolee.msf.entity.pay;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author HelloWood
 */
@Data
@Builder
public class Account {

    private Long id;

    private Integer balance;

    private Date lastUpdateTime;
}
