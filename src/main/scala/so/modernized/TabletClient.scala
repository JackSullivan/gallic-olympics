package so.modernized

import akka.actor.{Inbox, ActorRef, ActorSystem}
import scala.concurrent.duration._

/**
 * @author John Sullivan
 */
//class TabletClient(systemAddress:String, val id:String = java.util.UUID.randomUUID()) {
//  val address = "akka://olympics@localhost:12345/tabletrouter"

class TabletClient(system:ActorSystem, val id:String = java.util.UUID.randomUUID().toString) {
  private val router = system.actorSelection("user/router")
  private val subscriber = system.actorSelection("user/subscriberRoster")

  private val inbox = Inbox.create(system)

  def getScore(event:String):String = {
    router.tell(EventMessage(event, GetEventScore), inbox.getRef())

    val EventScore(eventName, score) = inbox.receive(5.seconds)

    s"Event: $eventName, Score: $score"
  }
  def getMedalTally(team:String):String = {
    router.tell(TeamMessage(team, GetMedalTally), inbox.getRef())

    val MedalTally(teamName, gold, silver, bronze) = inbox.receive(5.seconds)

    s"Team: $teamName, Gold: $gold, Silver: $silver, Bronze: $bronze"
  }
  def registerClient(event:String) = {
    subscriber.tell(Subscribe(event), inbox.getRef())
  }

  def reception = {
    (1 to 10).foreach{ _ =>
      println(inbox.receive(1.seconds))
    }
  }
}

object TabletClient {
  def main(args:Array[String]) {
    val olympics = new Olympics(Seq("Gaul", "Rome", "Carthage", "Pritannia", "Lacadaemon"), Seq("Curling", "Biathlon", "Piathlon"))

    val client = new TabletClient(olympics.system)

    val cacofonix = new CacofonixClient(olympics.system)
    client.registerClient("Curling")

    Thread.sleep(5000)

    cacofonix.setScore("Curling", "Gaul 1, Rome 2, Carthage 0")
    cacofonix.incrementMedalTally("Lacadaemon", Gold)

    Thread.sleep(5000)
    cacofonix.setScore("Curling", "Gaul 1, Rome 2, Carthage 0")
    cacofonix.setScore("Curling", "Gaul 2, Rome 2, Carthage 0")
    cacofonix.setScore("Curling", "Gaul 2, Rome 2, Carthage 1")

    client.reception

    println(client.getMedalTally("Gaul"))
    println(client.getMedalTally("Lacadaemon"))
    println(client.getScore("Curling"))

    olympics.system.shutdown()
  }
}