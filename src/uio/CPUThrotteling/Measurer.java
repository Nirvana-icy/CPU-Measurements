package uio.CPUThrotteling;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;


public class Measurer extends Thread
{
    int run = 5 * 60 * 1000; // 5 minutes
    int sleeptime = 25 * 1000; // 25 seconds
    int slept = 0;

    public void run()
    {
        int cores = Runtime.getRuntime().availableProcessors();
        StringBuilder string = new StringBuilder();

        while (run > slept)
        {
            string.append("Runtime: " + slept  / 1000 + " seconds\n");

            for(int core = 0; core < cores; core++)
            {
                getCPUFrequency(core);
            }

            try
            {
                sleep(sleeptime);
                slept += sleeptime;

            } catch (InterruptedException e){}
        }
    }

    /**
     * Returns the current CPU frequency
     * @param core - the core (0-n) that you want to measure
     * @return
     */
    String getCPUFrequency(int core)
    {
        return exec("cat \"/sys/devices/system/cpu/cpu"+core+"/cpufreq/scaling_cur_freq");
    }


    String getCPUTemperature()
    {
       return "";
    }

    /**
     * Helper function to execute
     * internal shell commands
     * @param command
     * @return
     */
    private String exec(String command)
    {
        String result = "";
        try
        {
            Process process = Runtime.getRuntime().exec(command);
            result = read(process.getInputStream());

        } catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return result;

    }

    /**
     * Helper function used to read the input-stream
     * of a process command
     * @param in
     * @return
     */
    private String read(InputStream in)
    {
        CharBuffer container = CharBuffer.allocate(16);
        InputStreamReader reader = new InputStreamReader(in);
        try
        {
            reader.read(container);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return container.toString();
    }

}
