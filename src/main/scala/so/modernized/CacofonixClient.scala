package so.modernized

import akka.actor.ActorSystem

/**
 * @author John Sullivan
 */
class CacofonixClient(system:ActorSystem) {
  private val listener = system.actorSelection("user/cacofonix")

  def setScore(event:String, score:String) {
    listener ! EventMessage(event, SetEventScore(score))
  }

  def incrementMedalTally(team:String, medalType:MedalType) {
    listener ! TeamMessage(team, IncrementMedals(medalType))
  }
}