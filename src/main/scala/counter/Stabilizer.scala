package counter

import spinal.core._

class Stabilizer extends Component {
  val io = new Bundle {
    val signal = in Bool
    val stable = out Bool
  }
  val reg1 = Reg(Bool()) init False
  val reg2 = Reg(Bool()) init False

  reg1 := io.signal
  reg2 := reg1
  io.stable := reg2
}
