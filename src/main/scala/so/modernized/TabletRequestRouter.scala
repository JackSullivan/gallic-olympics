package so.modernized

import akka.actor.{Terminated, Props, Actor}
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

  def receive: Actor.Receive = {
    case Terminated(a) =>
      router = router.removeRoutee(a)
      val r = context.actorOf(Props[TabletRequestWorker])
      context watch r
      router = router.addRoutee(r)
    case message => router.route(message, sender())
  }
}

class TabletRequestWorker extends Actor {
  val teamPath = context.system.actorSelection(context.system./("teams"))
  val eventPath = context.system.actorSelection(context.system./("events"))

  def receive: Actor.Receive = {
    case teamMessage:TeamMessage => teamPath.tell(teamMessage, sender())
    case eventMessage:EventMessage => eventPath.tell(eventMessage, sender())
  }
}
