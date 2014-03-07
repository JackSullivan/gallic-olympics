package so.modernized

import akka.actor.{Props, Actor, ActorSystem}
import com.typesafe.config.ConfigFactory

/**
 * Governing Olympics class that stores the actor system.
 */
class Olympics(teams:Iterable[String], events:Iterable[String]) {

  private val system = ActorSystem("olympics", ConfigFactory.load("server"))

  system.actorOf(TeamRoster(teams), "teams")
  system.actorOf(EventRoster(events), "events")
  system.actorOf(Props[CacofonixListener], "cacofonix")
  system.actorOf(Props[TabletRequestRouter], "router")
  system.actorOf(EventSubscriptions(events), "subscriberRoster")

  def shutdown() {system.shutdown()}

}

/**
 * Listener class to receive messages from the Cacofonix process and route
 * them to the appropriate Roster
 */
class CacofonixListener extends Actor {
  val teamPath = context.system.actorSelection(context.system./("teams"))
  val eventPath = context.system.actorSelection(context.system./("events"))

  def receive: Actor.Receive = {
    case teamMessage:TeamMessage => teamPath ! teamMessage
    case eventMessage:EventMessage => eventPath ! eventMessage
  }
}