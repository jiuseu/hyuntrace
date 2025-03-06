package com.example.hyuntrace.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.beans.ConstructorProperties;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class MemberDTO extends User implements OAuth2User {

    private String mid;
    private String mpw;
    private String email;
    private Boolean del;
    private Boolean social;
    private List<String> roles;
    private Map<String,Object> props; //소셜 로그인 정보

    public MemberDTO(String username, String password,String email,Boolean del,
            Boolean social,Collection<? extends GrantedAuthority>authorities){

        super(username,password,authorities);
        this.mid = username;
        this.mpw = password;
        this.email = email;
        this.del = del;
        this.social = social;
        this.roles = authorities.stream().map(role -> role.getAuthority()).collect(Collectors.toList());

    }

    @Override
    public Map<String,Object> getAttributes(){
        return this.getProps();
    }

    @Override
    public String getName(){
        return this.mid;
    }
}
