package so.modernized.runners

import so.modernized.Olympics

/**
 * @author John Sullivan
 */
object OlympicServer {

  def main(args: Array[String]) {
    val teams = args(0).split('|')
    val events = args(1).split('|')
    val olympics = new Olympics(teams, events)
    println("Let the games begin!")
  }
}
