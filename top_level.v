module top_level
   (input btn,
    input clk,
    output [3:0]leds,
    input reset);

  // Synchronize reset to clock
  reg ff_1, ff_2;
  wire reset_sync;

  always @(posedge clk)
  begin
    ff_1 <= reset;
    ff_2 <= ff_1;
  end

  assign reset_sync = ff_2;

  block_design block_design_i
       (.btn(btn),
        .clk(clk),
        .leds(leds),
        .rstn(~reset_sync));
endmodule
