package so.modernized

import akka.actor.{Address, Props, Actor, ActorSystem}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * @author John Sullivan
 */
class TabletClient(olympics:Olympics) {

  implicit val timeout = Timeout(600.seconds)

  val system = ActorSystem("client", ConfigFactory.load("client"))
  val remote = Address("akka.tcp","olympics", "127.0.0.1",2552).toString
  //println(remote)

  def shutdown() {system.shutdown()}

  private val router = Await.result(system.actorSelection(remote + "/user/router").resolveOne(), 600.seconds)
  private val subscriber = Await.result(system.actorSelection(remote + "/user/subscriberRoster").resolveOne(), 600.seconds)


  //private val router = olympics.router //= system.actorSelection("user/router")
  //private val subscriber = olympics.subscriber //system.actorSelection("user/subscriberRoster")

  private val printer = system.actorOf(Props[TabletPrinter])

  def getScore(event:String) {
    router.tell(EventMessage(event, GetEventScore), printer)
  }
  def getMedalTally(team:String) {
    router.tell(TeamMessage(team, GetMedalTally), printer)
  }
  def registerClient(event:String) = {
    subscriber.tell(Subscribe(event), printer)
  }
}

class TabletPrinter extends Actor {
  def receive: Actor.Receive = {
    case EventScore(event, score) => println(s"Event: $event, Score: $score")
    case MedalTally(team, gold, silver, bronze) => println(s"Team: $team, Gold: $gold, Silver: $silver, Bronze: $bronze")
  }
}

object Remote {
  def main(args:Array[String]) {
    val a = Address("akka","olympics", "127.0.0.1",2552)
    println(a.toString)
  }
}

object TabletClient {
  def main(args:Array[String]) {
    val olympics = new Olympics(Seq("Gaul", "Rome", "Carthage", "Pritannia", "Lacadaemon"), Seq("Curling", "Biathlon", "Piathlon"))

    val client = new TabletClient(olympics)

    val cacofonix = new CacofonixClient(olympics)
    client.registerClient("Curling")

    cacofonix.setScore("Curling", "Gaul 1, Rome 2, Carthage 0")
    cacofonix.incrementMedalTally("Lacadaemon", Gold)

    cacofonix.setScore("Curling", "Gaul 2, Rome 2, Carthage 0")
    cacofonix.setScore("Curling", "Gaul 3, Rome 2, Carthage 0")
    cacofonix.setScore("Curling", "Gaul 3, Rome 2, Carthage 1")

    println(client.getMedalTally("Gaul"))
    println(client.getMedalTally("Lacadaemon"))
    println(client.getScore("Curling"))

    //client.shutdown()
    //olympics.shutdown()
  }
}