package counter

import spinal.core._
import spinal.lib.bus.amba4.axilite._
import spinal.lib.fsm._
import spinal.lib.master

class SendToLeds(ledBits: BitCount, addr: Int) extends Component {
  val io = new Bundle {
    val input = in UInt ledBits
    val axi = master (AxiLite4(16, 32))
  }
  val lastSent = Reg(UInt(ledBits)) init io.input.maxValue
  val valid = Reg(Bool) init False

  val fsm = new StateMachine {
    val init : State = new State with EntryPoint {
      whenIsActive {
        when (lastSent =/= io.input) {
          goto(send)
        }
      }
    }
    val send : State = new State {
      onEntry {
        valid := True
      }
      whenIsActive {
        when(io.axi.aw.ready & io.axi.w.ready) {
          goto(init)
        }
      }
      onExit {
        valid := False
        lastSent := io.input
      }
    }
  }

  io.axi.aw.addr := addr
  io.axi.aw.valid := valid
  io.axi.aw.setUnprivileged
  io.axi.w.data := io.input.asBits.resize(32)
  io.axi.w.valid := valid
  io.axi.w.setStrb
  io.axi.b.ready := True

  // Don't read
  io.axi.ar.valid := False
  io.axi.ar.addr := 0
  io.axi.ar.setUnprivileged
  io.axi.r.ready := False
}
