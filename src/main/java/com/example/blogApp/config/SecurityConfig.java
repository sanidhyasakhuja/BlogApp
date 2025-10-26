package com.example.blogApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.blogApp.security.JWTAuthenticationEntryPoint;
import com.example.blogApp.security.JWTFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Value("${security.enabled}")
	private boolean securityEnabled;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JWTFilter jwtFilter;

	@Autowired
	private JWTAuthenticationEntryPoint authenticationEntryPoint;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
		if (securityEnabled) {
		
		return httpSecurity.cors(Customizer.withDefaults())
				.csrf(customizer->customizer.disable())
		            .authorizeHttpRequests((authz)->authz.requestMatchers("/api/auth/**","/api/posts/image/**").permitAll()
		            		.anyRequest().authenticated())
		            .exceptionHandling((customizer)->customizer.authenticationEntryPoint(this.authenticationEntryPoint))
		            .httpBasic(Customizer.withDefaults())
		            .sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		            .addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class)
		            .build();
		}
		else {
			return httpSecurity.csrf(customizer->customizer.disable())
		            .authorizeHttpRequests((authz)->authz.anyRequest().permitAll())
		            .httpBasic(Customizer.withDefaults())
		            .build();
		   
		            		
		}
	}
	@Bean
	public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(encoder());
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		return daoAuthenticationProvider;

	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	@Configuration
	public class MultipartConfig {
	    @Bean
	    public MultipartResolver multipartResolver() {
	        return new StandardServletMultipartResolver();
	    }
	}
}
