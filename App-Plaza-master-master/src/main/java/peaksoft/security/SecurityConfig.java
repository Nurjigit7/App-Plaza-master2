package peaksoft.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return new  UserDetailsServiceImpl();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http.authorizeHttpRequests(authorize ->{
            try {
            authorize.requestMatchers("/","/add","/save").permitAll()
                    .requestMatchers("/find-all").hasAuthority("ADMIN")
                    .requestMatchers("/update{id}").hasAuthority("ADMIN")
                    .requestMatchers("/{id}").hasAuthority("ADMIN")
                    .requestMatchers("/applications/add").hasAnyAuthority("ADMIN","USER")
                    .requestMatchers("/applications/save").hasAnyAuthority("ADMIN","USER")
                    .requestMatchers("/applications/update{id}/").hasAuthority("ADMIN")
                    .requestMatchers("/applications/search").hasAnyAuthority("ADMIN","USER")
                    .requestMatchers("/mailing**").hasAuthority("ADMIN")
                    .requestMatchers("/genres**").hasAuthority("ADMIN")
                    .anyRequest().authenticated()
                    .and()
                    .formLogin(Customizer.withDefaults())
                    .httpBasic(Customizer.withDefaults());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    });
        return http.build();
    }
}
