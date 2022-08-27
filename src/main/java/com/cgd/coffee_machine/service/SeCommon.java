package com.cgd.coffee_machine.service;

import com.cgd.coffee_machine.config.JwtTokenUtil;
import com.cgd.coffee_machine.model.*;
import com.cgd.coffee_machine.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service
public class SeCommon {

    private final JwtTokenUtil jwtTokenUtil;
    private final ReUser reUser;
    private final ReRole reRole;
    private final RePrivilege rePrivilege;

    @Autowired
    public SeCommon(JwtTokenUtil jwtTokenUtil, ReUser reUser, ReRole reRole, RePrivilege rePrivilege)
    {
        this.jwtTokenUtil = jwtTokenUtil;
        this.reUser = reUser;
        this.reRole = reRole;
        this.rePrivilege = rePrivilege;
    }

    public String getTokenFromCookie(HttpServletRequest request){
        if(request.getCookies()!=null){
            for (Cookie c:request.getCookies()) {
                if (c.getName().equals("Authorization")){
                    return "Bearer "+c.getValue();
                }
            }
        }
        return null;
    }

    public String getTokenFromHeader(HttpServletRequest request){
        return request.getHeader("Authorization");
    }

    public String getToken(HttpServletRequest request){
        String tokenHeader = getTokenFromHeader(request);
        if(tokenHeader==null) tokenHeader = getTokenFromCookie(request);

        if(tokenHeader==null) return null;
        else if(tokenHeader.startsWith("Bearer")) return tokenHeader.substring(7);
        return null;
    }

    public User getUser(HttpServletRequest request){
        try {
            String token = getToken(request);
            String userName = jwtTokenUtil.getUsernameFromToken(token);
            return reUser.findByUsername(userName);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public User getUser(String userName){
        try {
            return reUser.findByUsername(userName);
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    public boolean isAuthorizedRequest(Long userId,String api){
        Privilege privilege = rePrivilege.findByApi(api).orElse(null);
        if(privilege==null) return false;
        User user = reUser.getUserByPrivilege(userId, privilege.getId()).orElse(null);
        return user!=null;
    }

    public User getUser(Long id){
        return reUser.findById(id).orElse(null);
    }


}
