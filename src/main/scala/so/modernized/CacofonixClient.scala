package so.modernized

import akka.actor.{Deploy, Props, Address, ActorSystem}
import com.typesafe.config.ConfigFactory
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.util.Timeout
import akka.remote.RemoteScope

/**
 * This client takes an address that corresponds to the
 * location of an olympics server and allows a cacofonix
 * user to send write messages to the teams and events
 * on that server.
 */
class CacofonixClient(olympicsAddress: Address) {

  def this(olympics: Olympics) = this(Address("akka.tcp","olympics", "127.0.0.1",2552))

  implicit val timeout = Timeout(600.seconds)

  val system = ActorSystem("client", ConfigFactory.load("client"))
  val remote = olympicsAddress.toString

  def shutdown() {system.shutdown()}

  private val listener = Await.result(system.actorSelection(remote + "/user/cacofonix").resolveOne(), 600.seconds)

  def setScore(event:String, score:String) {
    listener ! EventMessage(event, SetEventScore(score, System.currentTimeMillis()))
  }

  def incrementMedalTally(team:String, medalType:MedalType) {
    listener ! TeamMessage(team, IncrementMedals(medalType, System.currentTimeMillis()))
  }
}