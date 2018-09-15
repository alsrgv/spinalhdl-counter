create_project -in_memory -part xc7z020clg400-1
read_verilog -library xil_defaultlib counter.v
read_xdc Zybo-Z7-Master.xdc
synth_design -top TopLevel
opt_design 
place_design 
route_design 
write_bitstream -force counter.bit 
