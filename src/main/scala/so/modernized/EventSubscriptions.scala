package so.modernized

import akka.actor.{ActorRef, Actor, Props}
import scala.collection.mutable

/**
 * @author John Sullivan
 */
case class Subscribe(eventName:String)

object EventSubscription {
  def props(eventName:String):Props = Props(new EventSubscription(eventName))
}

class EventSubscription(eventName:String) extends Actor {
  val subscribers = new mutable.ArrayBuffer[ActorRef]

  def receive: Actor.Receive = {
    case Subscribe(_) => subscribers += sender()
    case score:EventScore => subscribers.foreach { subscriber =>
      subscriber ! score
    }
  }
}

object EventSubscriptions {
  def apply(events:Iterable[String]):Props = Props(new EventSubscriptions(events))
}

class EventSubscriptions(events:Iterable[String]) extends Actor {

  events.foreach { event =>
    context.actorOf(EventSubscription.props(event), event)
  }

  def receive: Actor.Receive = {
    case Subscribe(eventName) => context.child(eventName) match {
      case Some(event) => event.tell(Subscribe(eventName), sender)
      case None => sender ! UnknownEvent(eventName)
    }
  }
}