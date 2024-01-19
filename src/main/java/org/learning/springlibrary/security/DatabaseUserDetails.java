package org.learning.springlibrary.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.learning.springlibrary.model.LibraryUser;
import org.learning.springlibrary.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DatabaseUserDetails implements UserDetails {

  private Integer id;
  private String username;
  private String password;

  private Set<GrantedAuthority> authorities;

  // costruttore che copia i dati da LibraryUser e valorizza gli attributi che servono a Spring
  public DatabaseUserDetails(LibraryUser libraryUser) {
    this.id = libraryUser.getId();
    this.username = libraryUser.getEmail();
    this.password = libraryUser.getPassword();
    // itero sui roles del LibraryUser e li trasformo in GrantedAuthority
    authorities = new HashSet<>();
    for (Role role : libraryUser.getRoleSet()) {
      authorities.add(new SimpleGrantedAuthority(role.getName()));
    }
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
