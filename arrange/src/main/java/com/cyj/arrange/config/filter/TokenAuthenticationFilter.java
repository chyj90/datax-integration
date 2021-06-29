package com.cyj.arrange.config.filter;

import com.cyj.arrange.util.JwtTokenUtil;
import com.cyj.arrange.util.ResponseUtil;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by chengyajie on 2021/6/9.
 */
//// TODO: 2021/6/9 增加token超期 
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {
    public TokenAuthenticationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //不需要鉴权
        String uri=request.getRequestURI();
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())||"/".equals(uri)||uri.contains("eureka")||uri.equals("/login")) {
            chain.doFilter(request, response);
        }else
        {
            UsernamePasswordAuthenticationToken authentication = null;
            try {
                authentication = getAuthentication(request);
            } catch (Exception e) {
                ResponseUtil.writeResult(response, HttpStatus.UNAUTHORIZED,e.getMessage());
                return;
            }
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                ResponseUtil.writeResult(response,HttpStatus.UNAUTHORIZED,"鉴权失败");
                return;
            }
            chain.doFilter(request, response);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // 获取Token字符串，token 置于 header 里
        String token = request.getHeader(JwtTokenUtil.TOKEN_KEY);
        if (!StringUtils.hasText(token)) {
            token = request.getParameter(JwtTokenUtil.TOKEN_KEY);
        }
        if (token != null && !"".equals(token.trim())) {
            // 从Token中解密获取用户名
            String userName = JwtTokenUtil.getUserNameFromToken(token);

            if (userName != null) {
                // 从Token中解密获取用户角色
                String role = JwtTokenUtil.getUserRoleFromToken(token);
                // 将ROLE_XXX,ROLE_YYY格式的角色字符串转换为数组
                String[] roles = role.split(",");
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for (String s : roles) {
                    authorities.add(new SimpleGrantedAuthority(s));
                }
                return new UsernamePasswordAuthenticationToken(userName, token, authorities);
            }
            return null;
        }
        return null;
    }
}
