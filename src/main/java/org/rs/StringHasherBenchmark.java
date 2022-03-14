package org.rs;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 3, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class StringHasherBenchmark
{
    private String DEMO = "http://www.com/foobjshdf hajsfjahsfdh ahsdjfh jahsdjfhjashjfhasjdhfjahsjfhueruweurweurzuwzuw urzwurzuw zuzewur zwuzr uwzeru zwurz uwzruweur ar";
    
    @Setup(Level.Invocation)
    public void setup()
    {
        // ensure a copy
        DEMO = new String(DEMO);
    }
    
    @Benchmark
    public int oneLine()
    {
        return StringHasher.hashShiftingOneLine(DEMO);
    }
    
    @Benchmark
    public int split()
    {
        return StringHasher.hashShiftingSplit(DEMO);
    }

}
