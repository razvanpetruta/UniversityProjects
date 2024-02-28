using System.Diagnostics;
using Lab4;

public class Program
{
    static void Main()
    {
        Stopwatch stopwatch = new Stopwatch();
        var hosts = new List<string>
        {
            "www.cs.ubbcluj.ro/~rlupsa/index-en.html",
            "www.cs.ubbcluj.ro/~rlupsa/edu/pdp/index.html",
            "www.cs.ubbcluj.ro/~rlupsa/edu/pdp/lab-1-noncooperative-mt.html",
            "www.cs.ubbcluj.ro/~rlupsa/edu/pdp/lab-2-producer-consumer.html",
            "www.cs.ubbcluj.ro/~rlupsa/edu/pdp/lab-3-parallel-simple.html",
            "www.cs.ubbcluj.ro/~rlupsa/edu/pdp/lab-4-futures-continuations.html"
        };
        
        Console.WriteLine("DIRECT CALLBACK");
        stopwatch.Start();
        DirectCallbacks.Run(hosts);
        stopwatch.Stop();
        TimeSpan directCallbackTime = stopwatch.Elapsed;
        
        Console.WriteLine("TASKS");
        stopwatch.Restart();
        TasksMechanism.Run(hosts);
        stopwatch.Stop();
        TimeSpan tasksMechanismTime = stopwatch.Elapsed;
        
        Console.WriteLine("ASYNC/AWAIT TASKS");
        stopwatch.Restart();
        AsyncTasksMechanism.Run(hosts);
        stopwatch.Stop();
        TimeSpan asyncTasksMechanismTime = stopwatch.Elapsed;
        
        Console.WriteLine($"Direct callback: {directCallbackTime}");
        Console.WriteLine($"Tasks mechanism: {tasksMechanismTime}");
        Console.WriteLine($"Async Tasks mechanism: {asyncTasksMechanismTime}");
    }
}