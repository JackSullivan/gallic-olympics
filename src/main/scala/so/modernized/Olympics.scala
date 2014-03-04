package so.modernized

import akka.actor.{Props, Actor, ActorSystem}

/**
 * @author John Sullivan
 */
trait ReadOnlyMessage

class Olympics(teams:Iterable[String], events:Iterable[String]) {

  val system = ActorSystem("olympics")

  system.actorOf(TeamRoster(teams), "teams")
  system.actorOf(EventRoster(events), "events")
  system.actorOf(Props[CacofonixListener], "cacofonix")
  system.actorOf(Props[TabletRequestRouter], "router")
  system.actorOf(EventSubscriptions(events), "subscriberRoster")


}

class CacofonixListener extends Actor {
  val teamPath = context.system.actorSelection(context.system./("teams"))
  val eventPath = context.system.actorSelection(context.system./("events"))

  def receive: Actor.Receive = {
    case teamMessage:TeamMessage => teamPath ! teamMessage
    case eventMessage:EventMessage => eventPath ! eventMessage
  }
}