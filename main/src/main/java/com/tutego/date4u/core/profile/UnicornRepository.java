package com.tutego.date4u.core.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UnicornRepository extends JpaRepository<Unicorn, Long> {

  @Query( "SELECT u FROM Unicorn u WHERE u.email = :emailAddress" )
  Optional<Unicorn> findUnicornByEmail( String emailAddress );
}
