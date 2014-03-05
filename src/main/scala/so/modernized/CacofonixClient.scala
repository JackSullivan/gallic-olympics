package so.modernized

import akka.actor.{Address, ActorSystem}
import com.typesafe.config.ConfigFactory
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.util.Timeout

/**
 * @author John Sullivan
 */
class CacofonixClient(olympics:Olympics) {

  implicit val timeout = Timeout(600.seconds)

  val system = ActorSystem("client", ConfigFactory.load("client"))
  val remote = Address("akka.tcp","olympics", "127.0.0.1",2552).toString
  //println(remote)

  def shutdown() {system.shutdown()}


  private val listener = Await.result(system.actorSelection(remote + "/user/cacofonix").resolveOne(), 600.seconds)

  def setScore(event:String, score:String) {
    listener ! EventMessage(event, SetEventScore(score))
  }

  def incrementMedalTally(team:String, medalType:MedalType) {
    listener ! TeamMessage(team, IncrementMedals(medalType))
  }
}