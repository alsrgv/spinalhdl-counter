package counter

import spinal.core._
import spinal.lib.Counter

class TopLevel(ledBits: BitCount, timeoutBits: BitCount) extends Component {
  val io = new Bundle {
    val btn = in Bool
    val leds = out UInt ledBits
  }
  // Set names that match expected IOs
  io.btn.setName("btn")
  io.leds.setName("leds")

  val stabilizer = new Stabilizer()
  val debouncer = new Debouncer(timeoutBits)
  val cnt = Counter(ledBits)

  stabilizer.io.signal := io.btn
  debouncer.io.signal := stabilizer.io.stable
  cnt.willIncrement := debouncer.io.justDown
  io.leds := cnt.value
}

object TopLevelVerilog {
  def main(args: Array[String]) {
    val config = new SpinalConfig {
      override val netlistFileName = "counter.v"
    }
    // It's really important to have TopLevel constructions inside the call.
    // Otherwise, in case of error all sorts of weird things happen.
    SpinalVerilog(config)(new TopLevel(4 bits, 16 bits))
  }
}
