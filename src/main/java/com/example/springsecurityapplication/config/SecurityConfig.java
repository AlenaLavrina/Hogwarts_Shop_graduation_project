package com.example.springsecurityapplication.config;

import com.example.springsecurityapplication.services.PersonDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    private final AuthenticationProvider authenticationProvider;
//
//    public SecurityConfig(AuthenticationProvider authenticationProvider) {
//        this.authenticationProvider = authenticationProvider;
//    }

    private final PersonDetailsService personDetailsService;

    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    //Конфигурация Spring Security
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        //Указываем на какой url адрес фильтр Spring Security будет отправлять неаутентифицированного пользователя при заходе на защищенную страницу
        httpSecurity
//        .csrf().disable() //отключаем защиту от межсайтовой подделки запросов
        //Указываем, что все страницы будут защищены процессом аутентификации
        .authorizeRequests()
        //Указываем, что страница /admin доступна пользователям с ролью ADMIN
        .antMatchers("/admin").hasRole("ADMIN")
        //указываем, что данные страницы доступны всем пользователям
        .antMatchers("/authentication/login", "/authentication/registration", "/error").permitAll()
        //Указыааем, что все остальные страницы доступны поользователям с ролями user и admin
        .anyRequest().hasAnyRole("USER", "ADMIN")
        //Указываем, что для всех остальных страниц необходимо вызывать метод authenticated(), который открывает форму аутентификации
//        .anyRequest().authenticated()
        .and()
        .formLogin().loginPage("/authentication/login")
        //Указываем на какой url будут отправляться данные с формы аутентификации
        .loginProcessingUrl("/process_login")
        //Указываем на какой url необходимо направлять пользователя после успешной аутентификации
        .defaultSuccessUrl("/index", true)
        //Указываем на какой url необходимо перейти при неуспешной аутентификации
        .failureUrl("/authentication/login?error")
        .and()
        .logout().logoutUrl("/logout").logoutSuccessUrl("/authentication/login");

    }

    //Данный метод позволяет настроить аутентификацию
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
//        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        authenticationManagerBuilder.userDetailsService(personDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
