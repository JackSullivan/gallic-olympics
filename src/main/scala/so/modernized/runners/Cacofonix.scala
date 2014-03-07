package so.modernized.runners

import so.modernized.CacofonixClient
import akka.actor.Address
import scala.util.Random
import scala.collection.mutable

/**
 * @author John Sullivan
 */
object Cacofonix {
  def main(args: Array[String]) {
    val host = args(0)
    val port = args(1).toInt
    val events = args(2).split('|')
    val teams = args(3).split('|')

    val cacofonix = new CacofonixClient(Address("akka.tcp","olympics", host, port))
    val eventToScore = new mutable.HashMap[String, mutable.HashMap[String, Int]]

    events.foreach(event => eventToScore.update(event, {
      val scores = new mutable.HashMap[String, Int]
      teams.foreach(team => scores.update(team, 0))
      scores
    })
    )

    println("Cacofonix is here and ready to report on the games!")
    (0 until 100).foreach(_ => {
      val event = events(Random.nextInt(events.length))
      val team = teams(Random.nextInt(teams.length))
      val scores = eventToScore(event)

      val score = scores(team)
      scores.update(team, score + 1)

      cacofonix.setScore(event, scores.toSeq.map({ case (t,s) => t + " " + s }).mkString(", "))
      Thread.sleep(1000)
    })
  }
}