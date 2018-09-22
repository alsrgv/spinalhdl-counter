package counter

import spinal.core._
import spinal.lib.Counter
import spinal.lib.bus.amba4.axilite._
import spinal.lib.master

class AxiCounter(ledBits: BitCount, timeoutBits: BitCount) extends Component {
  val io = new Bundle {
    val btn = in Bool
    val axi = master (AxiLite4(16, 32))
  }
  // Set names that match expected IOs
  io.btn.setName("btn")
  AxiLite4SpecRenamer(io.axi)

  val stabilizer = new Stabilizer()
  val debouncer = new Debouncer(timeoutBits)
  val cnt = Counter(ledBits)
  val axiSender = new SendToLeds(ledBits, 0x0)

  stabilizer.io.signal := io.btn
  debouncer.io.signal := stabilizer.io.stable
  cnt.willIncrement := debouncer.io.justDown
  axiSender.io.input := cnt.value
  io.axi << axiSender.io.axi
}

object AxiCounterVerilog {
  def main(args: Array[String]) {
    val config = new SpinalConfig {
      override val netlistFileName = "counter.v"
      override val defaultConfigForClockDomains: ClockDomainConfig =
        ClockDomainConfig(resetActiveLevel = LOW)
    }
    // It's really important to have TopLevel constructions inside the call.
    // Otherwise, in case of error all sorts of weird things happen.
    SpinalVerilog(config)(new AxiCounter(4 bits, 16 bits))
  }
}