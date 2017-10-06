package example

import org.scalajs.jquery.{jQuery=>$}

import scala.scalajs.js
import scala.scalajs.js.Dynamic.global
import shared.SharedMessages

object ScalaJSExample extends js.JSApp {
  def main(): Unit = {
    if (global.console != null) {
      global.console.log(s"Welcome to your Play application with Scala JS, your shared message is: ${SharedMessages.itWorks}")
    }
    val elm = $(".wrapper").not(".doc").find("a")
    elm.text(s"${elm.text()} ${SharedMessages.itWorks}")
  }
}