package so.modernized

import akka.actor.{Address, Props, Actor, ActorSystem}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Random

/**
 * @author John Sullivan
 */
class TabletClient(remoteAddress: Address) {

  def this(olympics: Olympics) = {
    this(Address("akka.tcp","olympics", "127.0.0.1",2552))
  }

  implicit val timeout = Timeout(600.seconds)

  val system = ActorSystem("client", ConfigFactory.load("client"))
  val remote = remoteAddress.toString
  //println(remote)

  def shutdown() {system.shutdown()}

  println(s"Connecting to remote server at $remote")

  private val router = Await.result(system.actorSelection(remote + "/user/blouter").resolveOne(), 600.seconds)
  private val subscriber = Await.result(system.actorSelection(remote + "/user/subscriberRoster").resolveOne(), 600.seconds)
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
    case UnknownEvent(eventName) => println(s"There are not $eventName competitions at these olympics.")
  }
}

object Remote {
  def main(args:Array[String]) {
    val a = Address("akka","olympics", "127.0.0.1",2552)
    println(a.toString)
  }
}

object OlympicServer {
  def main(args: Array[String]) {
    val olympics = new Olympics(Seq("Gaul", "Rome", "Carthage", "Pritannia", "Lacadaemon"), Seq("Curling", "Biathlon", "Piathlon"))
    println("Let the games begin!")

//    olympics.shutdown()
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

    client.getMedalTally("Gaul")
    client.getMedalTally("Lacadaemon")
    client.getScore("Curling")

//    Thread.sleep(5000)
//    client.shutdown()
//    olympics.shutdown()
  }

  def randomTabletClient(teams:IndexedSeq[String], events:IndexedSeq[String], address:Address, freq:Long)(implicit rand:Random) {
    def sample(strs:IndexedSeq[String]):String = strs(rand.nextInt(strs.size))

    val tablet = new TabletClient(address)
    tablet.registerClient(sample(events))
    (0 to rand.nextInt(20)).foreach { _ =>
      Thread.sleep(freq)
      rand.nextInt(2) match {
        case 0 => tablet.getScore(sample(events))
        case 1 => tablet.getMedalTally(sample(events))
      }
    }
  }
}