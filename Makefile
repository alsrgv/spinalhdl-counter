COUNTER_V = counter.v

CODE_FILES = $(shell find src/ -type f -name '*')

counter.bit: *.tcl ${COUNTER_V} *.xdc
	vivado -mode batch -source build.tcl -verbose -nojournal

${COUNTER_V}: ${CODE_FILES}
	sbt "runMain counter.TopLevelVerilog"

.PHONY: sim deploy clean

sim:
	sbt "runMain counter.TopLevelSim"

deploy: counter.bit
	vivado -mode batch -source deploy.tcl -verbose -nojournal

clean:
	rm -rf target tmp simWorkspace counter.v *.log *.jou *.bit usage_statistics_webtalk.*
