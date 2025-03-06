package com.example.hyuntrace.config;



import com.example.hyuntrace.security.exception.Custom403Handler;
import com.example.hyuntrace.security.handler.APILoginSuccessHandler;
import com.example.hyuntrace.security.APIUserDetailsService;
import com.example.hyuntrace.security.handler.LoginFailHandler;
import com.example.hyuntrace.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final DataSource dataSource;
    private final APIUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {

        //AuthenticationManger 설정
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
        log.info("-----------------------configure-----------------------");

        //커스텀 로그인 페이지
        http.formLogin(form -> form.loginPage("/member/login")
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler()));

        http.logout(form -> form.logoutUrl("/logout") // 로그아웃 URL
                        .logoutSuccessUrl("/member/login?logout=true") // 로그아웃 성공 후 리디렉션 URL true를 넣은 이유 파라미터 인식
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID")); // 쿠키 삭제

        http.authorizeHttpRequests(form -> form.requestMatchers("/member/login","/member/join"
                        ,"/api/member/join","/board/list","/api/board/listAll","/api/upload/view/**"
                        ,"/error","/ws-recommend","/api/recommend/**").permitAll()
                .requestMatchers("/board/**","/api/board/**","api/reply/**"
                        ,"/member/modinfo","/member/info","/api/member/info","/api/member/modify/**"
                        ,"/api/member/deleteMember/**","/api/memberInfoAuth","api/upload/**").hasRole("USER"));
//                .requestMatchers().authenticated()); //본인 정보 인증
        //CSRF 토큰 비활성화
        http.csrf(form -> form.disable());

        //자동 로그인
        http.rememberMe(form -> form.key("12345678")
                .userDetailsService(userDetailsService)
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60 * 60 * 24 * 30));

        //http.exceptionHandling(form -> form.accessDeniedHandler(accessDeniedHandler()));
        http.oauth2Login(form -> form.loginPage("/member/login")
                .successHandler(authenticationSuccessHandler()));
        http.sessionManagement(form -> form.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){

        log.info("-----------------------web configure-----------------------");

        return (web) -> web.ignoring().requestMatchers(PathRequest
                .toStaticResources().atCommonLocations());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){

        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);

        return repo;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new Custom403Handler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new APILoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new LoginFailHandler();
    }
}
