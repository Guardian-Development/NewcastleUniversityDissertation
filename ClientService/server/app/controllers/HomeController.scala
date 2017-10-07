package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class HomeController @Inject() extends Controller {

  def index = Action {
    //TODO: get app name + version to pass through.
    Ok(views.html.index("Client Service"))
  }

}
