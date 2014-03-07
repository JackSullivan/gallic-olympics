package so.modernized.runners

import so.modernized.TabletClient
import akka.actor.Address
import scala.util.Random

/**
 * @author John Sullivan
 */
object SpawnRandomTablet {
  def main(args:Array[String]) {
    val add = args(0)
    val port = args(1).toInt
    val freq = args(2).toLong
    val teams = args(3).split('|')
    val events = args(4).split('|')

    TabletClient.randomTabletClient(teams, events, Address("akka.tcp", "olympics", add, port), freq)(Random)
  }
}
