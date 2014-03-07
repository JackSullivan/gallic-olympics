package so.modernized.runners

import so.modernized._

/**
 * Created by akobren on 3/7/14.
 */
object Tester {
  def main(args:Array[String]) {
    val olympics = new Olympics(Seq("Gaul", "Rome", "Carthage", "Pritannia", "Lacadaemon"), Seq("Curling", "Slalom", "Skating"))
    val client = new TabletClient(olympics)
    val cacofonix = new CacofonixClient(olympics)

    println("Legal Teams: Gaul, Rome, Carthage, Pritannia, Lacadaemon")
    println("Curling, Slalom, Skating")
    Thread.sleep(2000)
    println("Client Pull Testing")
    Thread.sleep(2000)

    println("Cacofonix setting Curling score: Gaul 1, Rome 2, Carthage 0 - Client Pull")
    cacofonix.setScore("Curling", "Gaul 1, Rome 2, Carthage 0")
    Thread.sleep(2000)
    client.getScore("Curling")

    println("Cacofonix setting Slalom score: Gaul 0, Rome 2, Carthage 3 - Client Pull")
    cacofonix.setScore("Slalom", "Gaul 0, Rome 2, Carthage 3")
    Thread.sleep(2000)
    client.getScore("Slalom")

    println("Cacofonix setting Soccer (not legal): Gaul 1, Rome 1, Carthage 0 - Client Pull")
    cacofonix.setScore("Soccer", "Gaul 1, Rome 1, Carthage 0")
    Thread.sleep(2000)
    client.getScore("Soccer")

    println("Getting medal score for Carthage")
    client.getMedalTally("Carthage")

    println("Cacofonix incrementing medal tally: Garthage += 2 Gold; Rome +=1 Silver")
    cacofonix.incrementMedalTally("Carthage", Gold)
    cacofonix.incrementMedalTally("Carthage", Gold)
    cacofonix.incrementMedalTally("Rome", Silver)
    Thread.sleep(2000)
    client.getMedalTally("Carthage")

    println("Server Push Testing")
    Thread.sleep(2000)
    println("Registering Client for Slalom")
    client.registerClient("Slalom")
    Thread.sleep(2000)

    println("Cacofonix setting Slalom score: Gaul 3, Rome 2, Carthage 0 - No Client Pull")
    cacofonix.setScore("Slalom", "Gaul 3, Rome 2, Carthage 0")
    Thread.sleep(2000)

    println("Cacofonix setting Slalom score: Gaul 4, Rome 2, Carthage 0 - No Client Pull")
    cacofonix.setScore("Slalom", "Gaul 4, Rome 2, Carthage 0")
    Thread.sleep(2000)

    println("Cacofonix setting Curling score: Gaul 4, Rome 7, Carthage 5 - No Client Pull")
    cacofonix.setScore("Slalom", "Gaul 4, Rome 7, Carthage 5")
    Thread.sleep(2000)
  }
}
