package so.modernized

import akka.actor.{Props, Actor, ActorSystem}
import com.typesafe.config.ConfigFactory

/**
 * @author John Sullivan
 */
trait ReadOnlyMessage

class Olympics(teams:Iterable[String], events:Iterable[String]) {

  private val system = ActorSystem("olympics", ConfigFactory.load("server"))

  system.actorOf(TeamRoster(teams), "teams")
  system.actorOf(EventRoster(events), "events")
  system.actorOf(Props[CacofonixListener], "cacofonix")
  system.actorOf(Props[TabletRequestRouter], "router")
  system.actorOf(EventSubscriptions(events), "subscriberRoster")
  /*
  val router = system.actorSelection("user/router")
  val subscriber = system.actorSelection("user/subscriberRoster")
  val listener = system.actorSelection("user/cacofonix")
  */
  def shutdown() {system.shutdown()}

}

class CacofonixListener extends Actor {
  val teamPath = context.system.actorSelection(context.system./("teams"))
  val eventPath = context.system.actorSelection(context.system./("events"))

  def receive: Actor.Receive = {
    case teamMessage:TeamMessage => teamPath ! teamMessage
    case eventMessage:EventMessage => eventPath ! eventMessage
  }
}