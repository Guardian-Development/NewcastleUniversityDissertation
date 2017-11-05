import java.io.File
import com.typesafe.config._

object BuildConfig {
    val conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()
    val appName = conf.getString("app.name")
    val appVersion = conf.getString("app.version")
}