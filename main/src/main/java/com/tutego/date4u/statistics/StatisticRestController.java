package com.tutego.date4u.statistics;

import com.tutego.date4u.core.profile.ProfileRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@RestController
public class StatisticRestController {

    private final ProfileRepository profiles;

    public StatisticRestController(ProfileRepository profiles) {
        this.profiles = profiles;
    }

    @RequestMapping("/api/stat/total")
    // long als Rückgabe ist auch in Ordnung
    public String totalNumberOfRegisteredUnicorns() {
        return String.valueOf(profiles.count());
    }

    @GetMapping("/api/stat/last-seen")
    public LastSeenStatistics lastSeenStatistics(
            @RequestParam Optional<String> start, @RequestParam Optional<String> end) {

        List<LastSeenStatistics.Data> data = profiles.findMonthlyProfileCount().stream()
                .filter(t ->
                        start.map(s -> YearMonth.of((int) t.get("y"), (int) t.get("m")).isAfter(YearMonth.parse(s)))
                                .orElse(true))
                .filter(t ->
                        end.map(s -> YearMonth.of((int) t.get("y"), (int) t.get("m")).isBefore(YearMonth.parse(s)))
                                .orElse(true))
                .map(
                        tuple -> new LastSeenStatistics.Data(YearMonth.of(Integer.parseInt(tuple.get("y").toString()),
                                Integer.parseInt(tuple.get("m").toString())),
                                Integer.parseInt(tuple.get("count").toString()))).toList();
        return new LastSeenStatistics(data);
    }


}
