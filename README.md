PiBenchmark
===========

Benchmark CPU of android device by calculating Pi number



How we calculate Pi:

tan 45° (or π/4 radians) =1, therefore 
arctan(1) = π/4
and if we plug this into Gregory's Series: arctan(t) = t-(t^3/3)+(t^5/5)-(t^7/7)+(t^9/9)-... we get the following surprisingly simple and beautiful formula for π: 

arctan(1) = π/4 = 1-(1/3)+(1/5)-(1/7)+(1/9) - ...
