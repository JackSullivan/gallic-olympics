package so.modernized

import org.junit.Test
import akka.testkit.TestActorRef
import akka.pattern.ask
import scala.util.{Success, Try, Failure}
import org.scalatest.junit.JUnitSuite
import akka.actor.{ActorRef, ActorSystem}
import akka.util._

/**
 * @author John Sullivan
 */
class ActorTests extends JUnitSuite {

  @Test def medalTallyTest() {
    implicit val system = ActorSystem("test")
    implicit val timeout = Timeout(1000)

    val teamRef = TestActorRef[Team](Team.props("Rome"))

    val future = teamRef ? GetMedalTally

    val Success(MedalTally(name, gold, silver, bronze)) = future.value.get


    assert(name == "Rome")
    assert(gold == 0)
    assert(silver == 0)
    assert(bronze == 0)
  }

  @Test def incrementMedalTallyTest() {
    val olympics:Olympics = new Olympics(Seq("Rome", "Gaul", "Carthage"), Seq("Curling", "Skating", "Biathlon"))
    implicit val timeout = Timeout(6000)

    val romeRef = olympics.system.actorSelection("/user/teams/Rome")

    romeRef.tell(IncrementMedals(Gold), ActorRef.noSender)

    val future = romeRef ? GetMedalTally

    val Success(MedalTally(_, gold, _, _)) = future.value.get

    assert(gold == 1)
  }



}
