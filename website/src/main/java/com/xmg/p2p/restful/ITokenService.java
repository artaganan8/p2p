package com.xmg.p2p.restful;

import com.xmg.p2p.base.domain.Logininfo;

/**
 * Created by machao on 2017/9/14.
 */
public interface ITokenService {
    String createToken(Logininfo current);
}
