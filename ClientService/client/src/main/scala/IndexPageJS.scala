package client

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

object main extends js.JSApp {
  def main(): Unit = {}
}

@JSExport
object dashboardJS {

  @JSExport
  def main(): Unit = {
    dom.document.getElementById("page-wrapper").appendChild(
      div(`class`:= "row",
        div(`class` := "col-lg-12",
          h1("Dashboard", `class`:= "page-header"))).render
    )
  }
}