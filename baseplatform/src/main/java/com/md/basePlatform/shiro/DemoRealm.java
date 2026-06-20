package com.md.basePlatform.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 演示用 Realm：内存账号 admin / admin123。
 */
public class DemoRealm extends AuthorizingRealm {

    private static final String DEMO_USER = "admin";
    private static final String DEMO_PASSWORD = "admin123";

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole("operator");
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        UsernamePasswordToken up = (UsernamePasswordToken) token;
        String username = up.getUsername();
        if (!DEMO_USER.equals(username)) {
            throw new UnknownAccountException("unknown user");
        }
        String password = new String(up.getPassword());
        if (!DEMO_PASSWORD.equals(password)) {
            throw new IncorrectCredentialsException("bad password");
        }
        return new SimpleAuthenticationInfo(username, DEMO_PASSWORD, getName());
    }
}
