package counter

import spinal.core._
import spinal.core.sim._

import scala.util.Random

object TopLevelSim {
  def main(args: Array[String]): Unit = {
    SimConfig.withWave.doSim(new TopLevel(2 bits, 2 bits)){dut =>
      //Fork a process to generate the reset and the clock on the dut
      dut.clockDomain.forkStimulus(period = 10)

      var idx = 0
      while (idx < 100) {
        //Drive the dut inputs with random values
        dut.btn #= Random.nextBoolean()
        dut.clockDomain.waitRisingEdge()
        idx += 1
      }

      // Reset and observe LEDs turn off
      dut.clockDomain.assertReset()
      while (idx < 120) {
        dut.clockDomain.waitRisingEdge()
        idx += 1
      }
    }
  }
}
