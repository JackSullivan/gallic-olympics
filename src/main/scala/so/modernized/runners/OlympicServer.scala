package so.modernized.runners

import so.modernized.Olympics

/**
 * @author John Sullivan
 */
object OlympicServer {
  def main(args: Array[String]) {
    val olympics = new Olympics(Seq("Gaul", "Rome", "Carthage", "Pritannia", "Lacadaemon"), Seq("Curling", "Biathlon", "Piathlon"))
    println("Let the games begin!")
  }
}
