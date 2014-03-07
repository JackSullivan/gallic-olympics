package so.modernized

import org.scalacheck._
import akka.actor.{Inbox, Address}
import scala.concurrent.Await
import scala.concurrent.duration._

object OlympicsSpecification extends Properties("Olympics") {
  import Prop.forAll

  OlympicServer.main(null)

  property("realTimeScore") = forAll { (event: String, score: String) =>
    val cacofonix = new CacofonixClient(Address("akka.tcp","olympics", "127.0.0.1",2552))

    val client = new TabletClient(Address("akka.tcp","olympics", "127.0.0.1",2552)) {
      def inbox = Inbox.create(system)

      def checkScore(event:String): Boolean = {
        val router = Await.result(system.actorSelection(remote + "/user/router").resolveOne(), 5.seconds)
        router.tell(EventMessage("Curling", GetEventScore), inbox.getRef())

        val message = Await.result(inbox.receive(60.seconds), 60.seconds)
        message match {
          case EventScore(e, s) => {println(e + " " + s); true}
          case _ => false
        }

      }


    }

    println(s"setting $event, $score")
    cacofonix.setScore(event, score)
    Thread.sleep(100)

    val result = client.checkScore(event)
    Thread.sleep(100)

    cacofonix.shutdown()
    client.shutdown()

    result
  }

}