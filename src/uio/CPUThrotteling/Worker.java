package uio.CPUThrotteling;

import java.util.Random;

public class Worker extends Thread
{
    public void run()
    {
        Random random = new Random();
        while(true)
        {
            Math.log10(random.nextDouble());
        }
    }
}
