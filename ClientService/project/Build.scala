import java.io.File
import com.typesafe.config._

object BuildConfig {
  private val conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()
  val appName: String = conf.getString("app.name")
  val appVersion: String = conf.getString("app.version")
}