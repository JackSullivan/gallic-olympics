package so.modernized.runners

import so.modernized.CacofonixClient
import akka.actor.Address

/**
 * @author John Sullivan
 */
object Cacofonix {
  def main(args: Array[String]) = {
    val cacofonix = new CacofonixClient(Address("akka.tcp","olympics", "127.0.0.1",2552))

    println("Let the games begin!")
    cacofonix.setScore("Biathlon", "Gaul 1, Rome 0, Carthage 0")
    cacofonix.setScore("Curling", "Gaul 1, Rome 0, Carthage 1")
    Thread.sleep(1000)
    cacofonix.setScore("Biathlon", "Gaul 2, Rome 0, Carthage 0")
    cacofonix.setScore("Curling", "Gaul 3, Rome 2, Carthage 1")
    Thread.sleep(1000)
  }
}