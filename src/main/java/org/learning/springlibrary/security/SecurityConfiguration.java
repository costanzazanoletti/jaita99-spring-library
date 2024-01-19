package org.learning.springlibrary.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

  // metodo che restituisce un DatabaseUserDetailsService
  @Bean
  public DatabaseUserDetailsService userDetailsService() {
    return new DatabaseUserDetailsService();
  }

  // metodo che restituisce un PasswordEncoder
  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  // metodo che restituisce l'AuthenticationProvider
  @Bean
  public DaoAuthenticationProvider authProvider() {
    // creo un provider vuoto
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    // gli passo lo UserDetailsService
    authenticationProvider.setUserDetailsService(userDetailsService());
    // gli passo il PasswordEncoder
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  // metodo che crea la SecurityFilterChain
  /*
   * rotta / pubblica
   * rotta /books/create, /books/update, /books/delete solo ADMIN
   * rotta /books e /books/show sia USER che ADMIN
   * rotta /categories solo ADMIN
   * rotta /borrowings/** solo ADMIN
   */

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests()
        .requestMatchers("/books/create", "/books/edit/**", "/books/delete/**")
        .hasAuthority("ADMIN")
        .requestMatchers("/books", "/books/show/**").hasAnyAuthority("ADMIN", "USER")
        .requestMatchers("/categories", "/categories/**").hasAuthority("ADMIN")
        .requestMatchers("/borrowings/**").hasAuthority("ADMIN")
        .requestMatchers(HttpMethod.POST).hasAuthority("ADMIN")
        .requestMatchers("/", "/**").permitAll()
        .and().formLogin()
        .and().logout()
        .and().exceptionHandling();

    return http.build();
  }
}
