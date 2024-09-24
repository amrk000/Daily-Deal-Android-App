
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.TimeZone

object DateTimeHelper {
    fun utcToLocal(dateTimeUTC: String): String {

        return Instant.parse(dateTimeUTC).atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime().toString()
    }

    fun utcToLocalFormatted(dateTimeUTC: String): String {
        val pattern = "dd/MM/yyyy hh:mm a"
        val formatter = DateTimeFormatter.ofPattern(pattern)

        return Instant.parse(dateTimeUTC).atZone(TimeZone.getDefault().toZoneId()).format(formatter)
    }
}