package com.xmg.p2p.restful;

import com.xmg.p2p.base.domain.Logininfo;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by machao on 2017/9/14.
 */
@Service
public class TokenServiceImpl implements ITokenService{

    private static Map<String,Long> tokens  = new ConcurrentHashMap<>();
    @Override
    public String createToken(Logininfo current) {
        String token = UUID.randomUUID().toString();
        tokens.put(token,current.getId());
        return token;
    }
}
