package com.tutego.date4u.core.profile;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query(nativeQuery = true, value = """
            SELECT YEAR(lastseen) AS y, MONTH(lastseen) AS m, COUNT(*) AS count
            FROM profile
            GROUP BY YEAR(lastseen), MONTH(lastseen)""")
    List<Tuple> findMonthlyProfileCount();

    @Query("""
            SELECT p.gender
            FROM Profile p
            WHERE p.id = :id
            """)
    int getGenderById(Long id);


    @Query("""
            SELECT p
            FROM   Profile p
            WHERE  (p.attractedToGender=:myGender OR p.attractedToGender IS NULL)
               AND (p.gender = :attractedToGender OR :attractedToGender IS NULL)
               AND (p.hornlength BETWEEN :minHornlength AND :maxHornlength)
               AND (p.birthdate  <= :minBirthdate  )
                AND(p.birthdate >= :maxBirthdate)""")
    List<Profile> search(byte myGender, Byte attractedToGender,
                         LocalDate minBirthdate, LocalDate maxBirthdate,
                         short minHornlength, short maxHornlength);



}
