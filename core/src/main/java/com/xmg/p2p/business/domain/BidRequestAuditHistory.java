
package com.xmg.p2p.business.domain;

import com.xmg.p2p.base.domain.BaseAuthDomain;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by machao on 2017/9/8.
 */
@Setter@Getter
public class BidRequestAuditHistory extends BaseAuthDomain{
    public static final int PUBLISH_AUDIT = 0;//投标前审核
    public static final int PULL_AUDIT1 = 1;//满标一审
    public static final int PULL_AUDIT2 = 2;//满标二审
    public Long bidRequestId;
    private int auditType;
    public String getAuditTypeDisplay(){
        switch (this.auditType) {
            case PUBLISH_AUDIT: return "投标前审核";
            case PULL_AUDIT1: return "满标一审";
            case PULL_AUDIT2: return "满标二审";
            default : return "";
        }
    }
}
