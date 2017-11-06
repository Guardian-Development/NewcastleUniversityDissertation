package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class HomeController @Inject() (components: ControllerComponents, configuration: Configuration) extends AbstractController(components) {

  def index = Action {
    val appName = configuration.underlying.getString("app.name")
    val appVersion = configuration.underlying.getString("app.version")

    Ok(views.html.index(s"$appName:$appVersion"))
  }

}
