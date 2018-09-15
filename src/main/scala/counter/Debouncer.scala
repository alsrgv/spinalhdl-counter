package counter

import spinal.core._

// Debouncer from https://www.fpga4fun.com/Debouncer.html
class Debouncer(timeoutBits: BitCount) extends Component {
  val io = new Bundle {
    val signal = in Bool
    val justDown = out Bool
  }
  val cnt = Reg(UInt(timeoutBits)) init 0
  val state = Reg(Bool()) init False

  val idle = state === io.signal
  val cntMax = cnt === cnt.getAllTrue

  when (idle) {
    cnt := 0
  } otherwise {
    cnt := cnt + 1
    when (cntMax) {
      state := !state
    }
  }
  io.justDown := !idle && cntMax && ~state
}
