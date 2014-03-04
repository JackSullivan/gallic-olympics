package so.modernized

import akka.actor.{Props, Actor}
import akka.routing.{RoundRobinRoutingLogic, Router, ActorRefRoutee}

/**
 * @author John Sullivan
 */
class TabletRequestRouter extends Actor {
  var router = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props[TabletRequestWorker])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  override def receive: Actor.Receive = {
    case message:ReadOnlyMessage => router.route(message, sender())
  }
}

class TabletRequestWorker extends Actor {
  val teamPath = context.system.actorSelection(context.system./("teams"))
  val eventPath = context.system.actorSelection(context.system./("events"))

  override def receive: Actor.Receive = {
    case teamMessage:TeamMessage => teamPath ! teamMessage
    case eventMessage:EventMessage => eventPath ! eventMessage
    case tally:MedalTally => //todo DO SOMETHING
    case score:EventScore => //todo DO SOMETHING
  }
}
