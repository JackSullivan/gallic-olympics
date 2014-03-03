package so.modernized

import akka.actor.{Props, Actor}

/**
 * @author John Sullivan
 */
trait TeamMessageType
case class IncrementMedals(medalType:MedalType) extends TeamMessageType
case object GetMedalTally extends TeamMessageType

case class TeamMessage(teamName:String, message:TeamMessageType)

case class MedalTally(team:String, gold:Int, silver:Int, bronze:Int)

object Team {
  def props(name:String): Props = Props(new Team(name))
}

class Team(val name:String) extends Actor {
  var gold:Int = 0
  var silver:Int = 0
  var bronze:Int = 0

  override def receive: Actor.Receive = {
    case IncrementMedals(medalType) => medalType match {
      case Gold => gold += 1
      case Silver => silver += 1
      case Bronze => bronze += 1
    }
    case GetMedalTally => sender ! MedalTally(name, gold, silver, bronze)
  }
}

class TeamRoster(teams:Iterable[String]) extends Actor {

  teams.foreach{ teamName =>
    context.actorOf(Team.props(teamName), teamName)
  }

  override def receive: Actor.Receive = {
    case TeamMessage(teamName, message) => context.child(teamName) match {
      case Some(team) => team.tell(message, sender())
      case None => //todo DO SOMETHING!!
    }
  }
}