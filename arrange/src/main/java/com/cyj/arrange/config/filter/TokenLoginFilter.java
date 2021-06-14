package com.cyj.arrange.config.filter;

import com.cyj.arrange.bean.Result;
import com.cyj.arrange.util.JwtTokenUtil;
import com.cyj.arrange.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chengyajie on 2021/6/9.
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter{
    private AuthenticationManager authenticationManager;

    public TokenLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.setPostOnly(false);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            StringBuffer data = new StringBuffer();
            String line = null;
            BufferedReader reader = null;
            try {
                reader = req.getReader();
                while (null != (line = reader.readLine()))
                    data.append(line);
            } catch (IOException e) {
            } finally {
            }
            String jsonValue=data.toString();
            Map<String,String> json=JwtTokenUtil.gson().fromJson(jsonValue,new TypeToken<Map<String,String>>(){}.getType());
            String username = json.get("username");
            String password = json.get("password");
            username = username != null ? username.trim() : "";
            password = password != null ? password : "";
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 登录失败
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        ResponseUtil.writeResult(response,HttpStatus.FORBIDDEN,"登陆失败");
    }

    /**
     * 登陆成功
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        User user = (User)auth.getPrincipal();
        String token = JwtTokenUtil.createToken(user.getUsername(),user.getAuthorities().toString());
        HashMap<Object, Object> map = new HashMap<>();
        map.put(JwtTokenUtil.TOKEN_KEY,token);
        map.put("user",user);
        ResponseUtil.writeResult(response, HttpStatus.OK,map);
    }
}
