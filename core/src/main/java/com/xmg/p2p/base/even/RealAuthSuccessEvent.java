package com.xmg.p2p.base.even;

import com.xmg.p2p.base.domain.RealAuth;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 实名认证审核通过的消息事件
 *
 * Created by machao on 2017/9/23.
 */
@Getter
public class RealAuthSuccessEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *  事件要继承applicationEven创建一个事件的具体对象,事件关联事件源和事件对象
     *  在这里将事件源定义为applicationEvent,事件对象定义为Realauth
     * @param source the component that published the event (never {@code null})
     */
    private RealAuth realAuth;
    public RealAuthSuccessEvent(Object source,RealAuth realAuth) {
        super(source);
        this.realAuth = realAuth;
    }
}
