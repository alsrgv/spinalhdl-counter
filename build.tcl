create_project -in_memory -part xc7z020clg400-1
set_property source_mgmt_mode All [current_project]
update_ip_catalog

read_verilog -library xil_defaultlib counter.v
read_xdc Zybo-Z7-Master.xdc

create_bd_design block_design
create_bd_cell -type ip -vlnv xilinx.com:ip:axi_gpio:2.0 axi_gpio_0
create_bd_cell -type module -reference AxiCounter AxiCounter_0
create_bd_port -dir I -type clk clk
create_bd_port -dir I -type rst rstn
create_bd_port -dir I -type data btn
create_bd_port -dir O -from 3 -to 0 -type data leds

set_property -dict [list CONFIG.C_GPIO_WIDTH {4}] [get_bd_cells axi_gpio_0]
set_property -dict [list CONFIG.C_ALL_OUTPUTS {1}] [get_bd_cells axi_gpio_0]

connect_bd_intf_net [get_bd_intf_pins AxiCounter_0/axi] [get_bd_intf_pins axi_gpio_0/S_AXI]
connect_bd_net [get_bd_ports clk] [get_bd_pins AxiCounter_0/clk]
connect_bd_net [get_bd_ports clk] [get_bd_pins axi_gpio_0/s_axi_aclk]
connect_bd_net [get_bd_ports rstn] [get_bd_pins AxiCounter_0/resetn]
connect_bd_net [get_bd_ports rstn] [get_bd_pins axi_gpio_0/s_axi_aresetn]
connect_bd_net [get_bd_ports btn] [get_bd_pins AxiCounter_0/btn]
connect_bd_net [get_bd_ports leds] [get_bd_pins axi_gpio_0/gpio_io_o]

assign_bd_address
save_bd_design

generate_target all [get_files .srcs/sources_1/bd/block_design/block_design.bd]
read_verilog -library xil_defaultlib top_level.v

synth_design -top top_level
opt_design 
place_design 
route_design 
write_bitstream -force counter.bit 
