package org.learning.springlibrary.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Role {

  @Id
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
