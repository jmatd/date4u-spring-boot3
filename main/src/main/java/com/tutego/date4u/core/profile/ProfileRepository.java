package com.tutego.date4u.core.profile;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query(nativeQuery = true, value = """
            SELECT YEAR(lastseen) AS y, MONTH(lastseen) AS m, COUNT(*) AS count
            FROM profile
            GROUP BY YEAR(lastseen), MONTH(lastseen)""")
    List<Tuple> findMonthlyProfileCount();
}
