package com.example.hyuntrace.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.beans.ConstructorProperties;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class MemberDTO extends User {

    private String mid;
    private String mpw;
    private String email;
    private Boolean del;
    private Boolean social;

    private List<String> roles;

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

}
