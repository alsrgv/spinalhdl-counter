package counter

import spinal.core._
import spinal.lib.Counter

class TopLevel(ledBits: BitCount, timeoutBits: BitCount) extends Component {
  val btn = in Bool
  val leds = out UInt ledBits
  val stabilizer = new Stabilizer()
  val debouncer = new Debouncer(timeoutBits)
  val cnt = Counter(ledBits)

  stabilizer.io.signal := btn
  debouncer.io.signal := stabilizer.io.stable
  cnt.willIncrement := debouncer.io.justDown
  leds := cnt.value
}

object TopLevelVerilog {
  def main(args: Array[String]) {
    val config = new SpinalConfig {
      override val netlistFileName = "counter.v"
    }
    lazy val topLevel = new TopLevel(4 bits, 16 bits)
    SpinalVerilog(config)(topLevel)
  }
}