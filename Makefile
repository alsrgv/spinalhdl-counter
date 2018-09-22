COUNTER_V = counter.v
TOP_LEVEL_V = top_level.v

CODE_FILES = $(shell find src/ -type f -name '*')

counter.bit: *.tcl ${COUNTER_V} ${TOP_LEVEL_V} *.xdc
	vivado -mode batch -source build.tcl -verbose -nojournal

${COUNTER_V}: ${CODE_FILES}
	sbt "runMain counter.AxiCounterVerilog"

.PHONY: sim deploy clean

sim:
	sbt "runMain counter.AxiCounterSim"

deploy: counter.bit
	vivado -mode batch -source deploy.tcl -verbose -nojournal

clean:
	rm -rf target tmp simWorkspace counter.v .Xil .srcs *.log *.jou *.bit usage_statistics_webtalk.*
