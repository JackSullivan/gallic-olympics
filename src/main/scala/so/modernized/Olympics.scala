package so.modernized

/**
 * @author John Sullivan
 */
class Olympics(teamList:Iterable[Team], eventList:Iterable[Event]) {

  private val teamMap:Map[String,Team] = teamList.map{team => team.name -> team}.toMap
  private val eventMap:Map[String,Event] = eventList.map{event => event.name -> event}.toMap

  def getMedalTally(teamName:String) = {
    val team = teamMap(teamName)
    (team.gold, team.silver, team.bronze)
  }

  def incrementMedalTally(teamName:String, medalType:MedalType) {
    val team = teamMap(teamName)
    medalType match {
      case Gold => team.gold += 1
      case Silver => team.silver += 1
      case Bronze => team.bronze += 1
    }
  }

  def getScore(eventType:String) = eventMap(eventType).scores

  //todo we probably need another argument here to actually set the score
  def setScore(eventType:String) = ???
}
