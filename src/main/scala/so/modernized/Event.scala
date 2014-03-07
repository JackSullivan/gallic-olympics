package so.modernized

import akka.actor.{Props, Actor}

trait EventMessageType
case class SetEventScore(newScore:String) extends EventMessageType
case object GetEventScore extends EventMessageType

case class EventMessage(eventName:String, message:EventMessageType)

case class EventScore(eventName:String, score:String)

case class UnknownEvent(eventName: String)


/**
 * An event simply stores a score and responds to requests for it.
 * The event roster handles the logic of assigning requests to the
 * correct message. When it receives a score update it forwards the
 * update to the appropriate EventSubscription record.
 */
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

/**
 * The event roster serves as parents to all of the events at the olympics
 * and routes incoming requests to read and write to the appropriate event.
 * If no event is found for a given request a message is sent back to the
 * requester to that effect.
 */
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
      case None => sender ! UnknownEvent(eventName)
    }
    }
  }
}

