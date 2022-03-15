# Manual Code Optimisation
While building a hash implementation for a custom character buffer, we noticed that a manual optimization of the code to allow in theory more independent CPU work suprisingly worked. As a side note, this is based on the traditional hash implementation for Strings in the JDK because we assumed that multiplication by 31 is automatically replaced by the compiler with a shift and substraction. So we started rather with a a test if this optimization is done and by accident discovered that manual code reshaping works.

## String Hashing
```java
for (int i = 0; i < s.length(); i++) 
{
    final char c = s.charAt(i);
    hash = 31 * hash + c;
}
```

## Improved by Shifting
```java
for (int i = 0; i < s.length(); i++) 
{
    final char c = s.charAt(i);
    hash = (hash << 5) + c - hash;
}
``` 

## Improved by Hinting Independent Operations
```java
for (int i = 0; i < s.length(); i++) 
{
    final char c = s.charAt(i);

    int h1 = hash << 5; // * 32
    int h2 = c - hash; // - 1 * hash aka the before was * 31
    hash = h1 + h2;
}
```

## Measurements

```
4 core Intel i7-7700K CPU @ 4.20GHz
StringHasherBenchmark.decomposed1           avgt    5   87.883 ± 3.075  ns/op
StringHasherBenchmark.decomposed2           avgt    5   86.983 ± 0.299  ns/op
StringHasherBenchmark.decomposed3           avgt    5   87.047 ± 0.234  ns/op
StringHasherBenchmark.decomposed3Reordered  avgt    5   86.724 ± 0.432  ns/op
StringHasherBenchmark.oneLine               avgt    5  112.551 ± 0.251  ns/op

StringHasherBenchmark.decomposed1                                 avgt    5    88.821 ±  1.472      ns/op
StringHasherBenchmark.decomposed1:CPI                             avgt          0.531           clks/insn
StringHasherBenchmark.decomposed1:IPC                             avgt          1.882           insns/clk
StringHasherBenchmark.decomposed1:L1-dcache-load-misses           avgt          0.417                #/op
StringHasherBenchmark.decomposed1:L1-dcache-loads                 avgt        249.084                #/op
StringHasherBenchmark.decomposed1:L1-dcache-stores                avgt         62.273                #/op
StringHasherBenchmark.decomposed1:L1-icache-load-misses           avgt          0.137                #/op
StringHasherBenchmark.decomposed1:LLC-load-misses                 avgt          0.004                #/op
StringHasherBenchmark.decomposed1:LLC-loads                       avgt          0.038                #/op
StringHasherBenchmark.decomposed1:LLC-store-misses                avgt          0.021                #/op
StringHasherBenchmark.decomposed1:LLC-stores                      avgt          0.059                #/op
StringHasherBenchmark.decomposed1:branch-misses                   avgt          0.013                #/op
StringHasherBenchmark.decomposed1:branches                        avgt         93.126                #/op
StringHasherBenchmark.decomposed1:cycles                          avgt        586.947                #/op
StringHasherBenchmark.decomposed1:dTLB-load-misses                avgt          0.008                #/op
StringHasherBenchmark.decomposed1:dTLB-loads                      avgt        246.341                #/op
StringHasherBenchmark.decomposed1:dTLB-store-misses               avgt         ≈ 10⁻⁴                #/op
StringHasherBenchmark.decomposed1:dTLB-stores                     avgt         61.757                #/op
StringHasherBenchmark.decomposed1:iTLB-load-misses                avgt          0.002                #/op
StringHasherBenchmark.decomposed1:iTLB-loads                      avgt          0.006                #/op
StringHasherBenchmark.decomposed1:instructions                    avgt       1104.612                #/op

StringHasherBenchmark.oneLine                                     avgt    5   120.365 ± 11.348      ns/op
StringHasherBenchmark.oneLine:CPI                                 avgt          0.578           clks/insn
StringHasherBenchmark.oneLine:IPC                                 avgt          1.731           insns/clk
StringHasherBenchmark.oneLine:L1-dcache-load-misses               avgt          0.462                #/op
StringHasherBenchmark.oneLine:L1-dcache-loads                     avgt        247.546                #/op
StringHasherBenchmark.oneLine:L1-dcache-stores                    avgt         61.618                #/op
StringHasherBenchmark.oneLine:L1-icache-load-misses               avgt          0.154                #/op
StringHasherBenchmark.oneLine:LLC-load-misses                     avgt          0.010                #/op
StringHasherBenchmark.oneLine:LLC-loads                           avgt          0.042                #/op
StringHasherBenchmark.oneLine:LLC-store-misses                    avgt          0.020                #/op
StringHasherBenchmark.oneLine:LLC-stores                          avgt          0.060                #/op
StringHasherBenchmark.oneLine:branch-misses                       avgt          0.108                #/op
StringHasherBenchmark.oneLine:branches                            avgt         95.661                #/op
StringHasherBenchmark.oneLine:cycles                              avgt        703.349                #/op
StringHasherBenchmark.oneLine:dTLB-load-misses                    avgt          0.010                #/op
StringHasherBenchmark.oneLine:dTLB-loads                          avgt        244.157                #/op
StringHasherBenchmark.oneLine:dTLB-store-misses                   avgt         ≈ 10⁻⁴                #/op
StringHasherBenchmark.oneLine:dTLB-stores                         avgt         61.536                #/op
StringHasherBenchmark.oneLine:iTLB-load-misses                    avgt          0.001                #/op
StringHasherBenchmark.oneLine:iTLB-loads                          avgt          0.007                #/op
StringHasherBenchmark.oneLine:instructions                        avgt       1217.463                #/op

8 core, AMD

StringHasherBenchmark.oneLine  avgt    5  148.262 ± 5.626  ns/op
StringHasherBenchmark.split    avgt    5  115.873 ± 2.777  ns/op

Benchmark                                            Mode  Cnt     Score   Error  Units
StringHasherBenchmark.oneLine                        avgt    5   153.576 ± 3.036  ns/op
StringHasherBenchmark.oneLine:L1-dcache-load-misses  avgt          0.586           #/op
StringHasherBenchmark.oneLine:L1-dcache-loads        avgt        256.442           #/op
StringHasherBenchmark.oneLine:L1-dcache-stores       avgt         61.182           #/op
StringHasherBenchmark.oneLine:L1-icache-load-misses  avgt          0.096           #/op
StringHasherBenchmark.oneLine:branch-misses          avgt          0.062           #/op
StringHasherBenchmark.oneLine:branches               avgt        101.986           #/op
StringHasherBenchmark.oneLine:dTLB-load-misses       avgt          0.003           #/op
StringHasherBenchmark.oneLine:dTLB-loads             avgt        260.380           #/op
StringHasherBenchmark.oneLine:dTLB-store-misses      avgt         ≈ 10⁻³           #/op
StringHasherBenchmark.oneLine:dTLB-stores            avgt         61.302           #/op
StringHasherBenchmark.oneLine:iTLB-load-misses       avgt          0.008           #/op
StringHasherBenchmark.oneLine:iTLB-loads             avgt          0.011           #/op
StringHasherBenchmark.oneLine:instructions           avgt       1244.522           #/op
StringHasherBenchmark.split                          avgt    5   122.046 ± 9.564  ns/op
StringHasherBenchmark.split:L1-dcache-load-misses    avgt          0.560           #/op
StringHasherBenchmark.split:L1-dcache-loads          avgt        260.938           #/op
StringHasherBenchmark.split:L1-dcache-stores         avgt         62.005           #/op
StringHasherBenchmark.split:L1-icache-load-misses    avgt          0.199           #/op
StringHasherBenchmark.split:branch-misses            avgt          0.038           #/op
StringHasherBenchmark.split:branches                 avgt        100.475           #/op
StringHasherBenchmark.split:dTLB-load-misses         avgt          0.014           #/op
StringHasherBenchmark.split:dTLB-loads               avgt        260.149           #/op
StringHasherBenchmark.split:dTLB-store-misses        avgt         ≈ 10⁻³           #/op
StringHasherBenchmark.split:dTLB-stores              avgt         61.683           #/op
StringHasherBenchmark.split:iTLB-load-misses         avgt          0.007           #/op
StringHasherBenchmark.split:iTLB-loads               avgt          0.010           #/op
StringHasherBenchmark.split:instructions             avgt       1148.588           #/op

4 core, Intel
StringHasherBenchmark.oneLine  avgt    5  173.984 ±  9.216  ns/op
StringHasherBenchmark.split    avgt    5  136.520 ± 11.486  ns/op

StringHasherBenchmark.oneLine                        avgt    5   179.171 ± 10.089  ns/op
StringHasherBenchmark.oneLine:L1-dcache-load-misses  avgt          0.779            #/op
StringHasherBenchmark.oneLine:L1-dcache-loads        avgt        263.244            #/op
StringHasherBenchmark.oneLine:L1-dcache-stores       avgt         62.294            #/op
StringHasherBenchmark.oneLine:L1-icache-load-misses  avgt          0.309            #/op
StringHasherBenchmark.oneLine:branch-misses          avgt          0.127            #/op
StringHasherBenchmark.oneLine:branches               avgt        106.561            #/op
StringHasherBenchmark.oneLine:dTLB-load-misses       avgt          0.063            #/op
StringHasherBenchmark.oneLine:dTLB-loads             avgt        262.446            #/op
StringHasherBenchmark.oneLine:dTLB-store-misses      avgt          0.001            #/op
StringHasherBenchmark.oneLine:dTLB-stores            avgt         62.527            #/op
StringHasherBenchmark.oneLine:iTLB-load-misses       avgt          0.013            #/op
StringHasherBenchmark.oneLine:iTLB-loads             avgt          0.020            #/op
StringHasherBenchmark.oneLine:instructions           avgt       1258.776            #/op
StringHasherBenchmark.split                          avgt    5   149.666 ± 39.112  ns/op
StringHasherBenchmark.split:L1-dcache-load-misses    avgt          0.811            #/op
StringHasherBenchmark.split:L1-dcache-loads          avgt        264.388            #/op
StringHasherBenchmark.split:L1-dcache-stores         avgt         63.678            #/op
StringHasherBenchmark.split:L1-icache-load-misses    avgt          0.229            #/op
StringHasherBenchmark.split:branch-misses            avgt          0.129            #/op
StringHasherBenchmark.split:branches                 avgt        103.109            #/op
StringHasherBenchmark.split:dTLB-load-misses         avgt          0.309            #/op
StringHasherBenchmark.split:dTLB-loads               avgt        268.350            #/op
StringHasherBenchmark.split:dTLB-store-misses        avgt          0.001            #/op
StringHasherBenchmark.split:dTLB-stores              avgt         63.398            #/op
StringHasherBenchmark.split:iTLB-load-misses         avgt          0.012            #/op
StringHasherBenchmark.split:iTLB-loads               avgt          0.014            #/op
StringHasherBenchmark.split:instructions             avgt       1152.701            #/op

4 core, shared intel
StringHasherBenchmark.decomposed1           avgt    5  130.639 ± 4.987  ns/op
StringHasherBenchmark.decomposed2           avgt    5  129.956 ± 0.540  ns/op
StringHasherBenchmark.decomposed3           avgt    5  130.163 ± 1.158  ns/op
StringHasherBenchmark.decomposed3Reordered  avgt    5  132.290 ± 3.685  ns/op
StringHasherBenchmark.oneLine               avgt    5  165.628 ± 3.188  ns/op



4 core, shared amd
StringHasherBenchmark.decomposed1           avgt    5  143.064 ± 20.650  ns/op
StringHasherBenchmark.decomposed2           avgt    5  141.889 ±  3.955  ns/op
StringHasherBenchmark.decomposed3           avgt    5  141.146 ±  4.967  ns/op
StringHasherBenchmark.decomposed3Reordered  avgt    5  142.026 ±  8.049  ns/op
StringHasherBenchmark.oneLine               avgt    5  178.657 ± 10.967  ns/op
``` 
