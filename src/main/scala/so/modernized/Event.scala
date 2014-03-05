package so.modernized

import akka.actor.{Props, Actor}

/**
 * @author John Sullivan
 */
trait EventMessageType
case class SetEventScore(newScore:String) extends EventMessageType
case object GetEventScore extends EventMessageType with ReadOnlyMessage

case class EventMessage(eventName:String, message:EventMessageType)

case class EventScore(eventName:String, score:String)

object Event {
  def props(eventName:String):Props = Props(new Event(eventName))
}


class Event(val name:String) extends Actor {
  val subscriberPath = context.system./("subscriberRoster")
  var score:String = ""

  def receive: Actor.Receive = {
    case SetEventScore(newScore) => {
      score = newScore
      context.system.actorSelection(subscriberPath./(name)) ! EventScore(name, score)
    }
    case GetEventScore => sender() ! EventScore(name, score)
  }
}

object EventRoster {
  def apply(events:Iterable[String]):Props = Props(new EventRoster(events))
}

class EventRoster(events:Iterable[String]) extends Actor {

  events.foreach{ eventName =>
    context.actorOf(Event.props(eventName), eventName)
  }

  def receive: Actor.Receive = {
    case EventMessage(eventName, message) => { context.child(eventName) match {
      case Some(event) => event.tell(message, sender())
      case None => {println("I just got a non event")}//todo DO SOMETHING!!!
    }
    }
  }
}

