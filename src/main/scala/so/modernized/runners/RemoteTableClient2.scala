package so.modernized.runners

import akka.actor.Address
import so.modernized.TabletClient

/**
 * @author John Sullivan
 */
object RemoteTableClient2 {
  def main(args: Array[String]) {
    val client = new TabletClient(Address("akka.tcp","olympics", "127.0.0.1",2552))
    client.registerClient("Biathlon")
    (0 until 10).foreach({ _ =>
      Thread.sleep(1000)
      client.getScore("Curling")
    })
  }
}
