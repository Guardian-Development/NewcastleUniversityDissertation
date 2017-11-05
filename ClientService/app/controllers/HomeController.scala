package controllers

import javax.inject._
import play.api.mvc._
import play.api.Configuration

@Singleton
class HomeController @Inject()(configuration: Configuration) extends Controller {

  def index = Action {
    val appName = configuration.underlying.getString("app.name")
    val appVersion = configuration.underlying.getString("app.version")

    Ok(views.html.index(s"$appName:$appVersion"))
  }

}
