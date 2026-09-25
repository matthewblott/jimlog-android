package com.matthewblott.jimlog

object Settings {
  val current: Environment = Environment.Local

  enum class Environment(val url: String) {
    Remote("https://jimlog.coderscoffeehouse.com"),
    Local("http://10.0.2.2:3000")
  }

}