package com.xmg.p2p.business.event;

import com.xmg.p2p.business.domain.BidRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 满标二审成功事件
 *
 * Created by machao on 2017/9/24.
 */
@Getter
public class BidrequestAudit2SuccessEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    private BidRequest bidRequest;

    public BidrequestAudit2SuccessEvent(Object source,BidRequest bidRequest) {
        super(source);
        this.bidRequest = bidRequest;
    }
}
