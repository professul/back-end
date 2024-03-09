package com.professul.professul.config;

import com.professul.professul.jwt.JWTFilter;
import com.professul.professul.jwt.JWTUtil;
import com.professul.professul.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil=jwtUtil;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
    @Bean //암호화
    public BCryptPasswordEncoder passwordEncode(){
        return new BCryptPasswordEncoder();
    }
@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    http.cors((cors)->cors.configurationSource(new CorsConfigurationSource() {
        @Override
        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
            CorsConfiguration configuration=new CorsConfiguration();
            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
            configuration.setAllowedMethods(Collections.singletonList("*"));
            configuration.setAllowCredentials(true);
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            configuration.setMaxAge(3600L);

            configuration.setExposedHeaders(Collections.singletonList("Authorization"));



            return configuration;
        }
    }));
    http.csrf(AbstractHttpConfigurer::disable); //csrf disable
    http.formLogin(AbstractHttpConfigurer::disable); //form login disable
    http.httpBasic(AbstractHttpConfigurer::disable); //http basic 인증 방식 disable
    http.authorizeHttpRequests((auth) -> auth //경로별 인가 작업
            .requestMatchers("/", "/login", "/join").permitAll()
            .anyRequest().authenticated()
    );
    //필터 추가 LoginFilter()는 인자를 받음(AuhenticationManager()메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
    http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
    http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

//세션 설정
    http.sessionManagement((session)-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
return http.build();
}

}
