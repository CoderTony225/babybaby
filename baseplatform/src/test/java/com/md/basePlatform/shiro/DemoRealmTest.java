package com.md.basePlatform.shiro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.junit.jupiter.api.Test;

class DemoRealmTest {

    private final DemoRealm realm = new DemoRealm();

    @Test
    void should_authenticate_demo_user() {
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "admin123");
        assertEquals("admin", realm.getAuthenticationInfo(token).getPrincipals().getPrimaryPrincipal());
    }

    @Test
    void should_reject_unknown_user() {
        UsernamePasswordToken token = new UsernamePasswordToken("nobody", "x");
        assertThrows(UnknownAccountException.class, () -> realm.getAuthenticationInfo(token));
    }

    @Test
    void should_reject_bad_password() {
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "wrong");
        assertThrows(IncorrectCredentialsException.class, () -> realm.getAuthenticationInfo(token));
    }
}
