package com.cyj.arrange.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static Authentication UD()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String userName()
    {
        String username = null;
        Authentication ud = UD();
        if (ud!=null)
        {
            username = (String) ud.getPrincipal();
        }
        return username;
    }
}
