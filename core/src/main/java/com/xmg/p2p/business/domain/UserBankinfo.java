package com.xmg.p2p.business.domain;

import com.xmg.p2p.base.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * Created by machao on 2017/9/11.
 */
@Setter@Getter
public class UserBankinfo extends BaseDomain {
    private Long userinfoId;

    private String bankName;    //银行名称
    private String accountNumber;  //银行账号
    private String bankForkName;    //支行名称
    private String accountName; //账户账户
    /**
     * 获取用户真实名字的隐藏字符串，只显示姓氏
     *
     * @param realName
     *            真实名字
     * @return
     */
    public String getAnonymousRealName() {
        if (StringUtils.hasLength(this.accountName)) {
            int len = accountName.length();
            String replace = "";
            replace += accountName.charAt(0);
            for (int i = 1; i < len; i++) {
                replace += "*";
            }
            return replace;
        }
        return accountName;
    }

    /**
     * 获取用户身份号码的隐藏字符串
     *
     * @param idNumber
     * @return
     */
    public String getAnonymousAccountNumber() {
        if (StringUtils.hasLength(accountNumber)) {
            int len = accountNumber.length();
            String replace = "";
            for (int i = 0; i < len; i++) {
                if ((i > 5 && i < 10) || (i > len - 5)) {
                    replace += "*";
                } else {
                    replace += accountNumber.charAt(i);
                }
            }
            return replace;
        }
        return accountNumber;
    }

}
