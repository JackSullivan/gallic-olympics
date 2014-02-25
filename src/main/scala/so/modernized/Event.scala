package so.modernized

import scala.collection.mutable

/**
 * @author John Sullivan
 */
class Event(val name:String) {
  val scores = new mutable.HashMap[String, Double].withDefaultValue(0.0)
}


