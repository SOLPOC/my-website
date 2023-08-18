package ind.xyz.mywebsite.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Optional;

public class TimezoneUtil {
        public static LocalDateTime toTimezone(LocalDateTime localDateTime,String timezone){
            // 将UTC时间转换为指定时区的时间
            if(timezone.contains("/")) {
                Collection<String> values = ZoneId.SHORT_IDS.keySet();
                Optional<String> first = values.stream().filter(x->ZoneId.SHORT_IDS.get(x).equals(timezone)).findFirst();
                ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of( ZoneId.SHORT_IDS.get(first.get())));
                return zonedDateTime.toLocalDateTime();
            }
            return null;
        }
}
