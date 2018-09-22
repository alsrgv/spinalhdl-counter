module top_level
   (input btn,
    input clk,
    output [3:0]leds,
    input reset);

  block_design block_design_i
       (.btn(btn),
        .clk(clk),
        .leds(leds),
        .rstn(~reset));
endmodule
