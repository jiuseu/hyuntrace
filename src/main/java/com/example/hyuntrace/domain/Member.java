package com.example.hyuntrace.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
public class Member{

    @Id
    private String mid;

    private String mpw;

    private String email;

    private boolean social;

    private boolean del;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void changePassword(String mpw){
        this.mpw = mpw;
    }

    public void changeEmail(String email){
        this.email = email;
    }

    public void changeDel(boolean del){
        this.del = del;
    }

    public void changeSocial(boolean social){
        this.social = social;
    }

    public void addRole(MemberRole memberRole){
        this.roleSet.add(memberRole);
    }

    public void clearRole(){
        this.roleSet.clear();
    }
}
