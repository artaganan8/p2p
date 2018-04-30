package com.xmg.p2p.business.service;

import com.xmg.p2p.business.domain.ExpAccount;
import lombok.Getter;
import org.apache.commons.lang.time.DateUtils;

import java.math.BigDecimal;
import java.util.Date;

public interface IExpAccountService {
    int save(ExpAccount expAccount);
    int update(ExpAccount expAccount);
    ExpAccount get(Long id);

    void grantExpMoney(Long id, LastTime lastTime, BigDecimal registerGrantExpmoney, int expmoneyTypeRegister);


    @Getter
    class  LastTime{
        private int amount;
        private LastTimeUnit unit;

        public LastTime(int amount,LastTimeUnit unit){
            this.amount = amount;
            this.unit = unit;
        }

        /**
         * 获取体验金的最晚还款时间
         *
         * @param date
         * @return
         */
        public Date getReturnDate(Date date) {
            switch (this.unit){
                case DAY:
                    return DateUtils.addDays(date,this.amount);
                case MONTH:
                    return DateUtils.addDays(date,this.amount);
                case YEAR:
                    return DateUtils.addDays(date,this.amount);
                default:
                    return date;
            }
        }
    }

    /**
     * 持续时间单位
     */
    enum LastTimeUnit{
        DAY,MONTH,YEAR
    }
}
