package uio.CPUThrotteling;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

import static uio.CPUThrotteling.Configuration.CORES;


public class Measurer extends Thread
{
    // Denotes how long this measurer has slept
    int slept = 0;


    public void run()
    {

        getStateTimes();

        while (Configuration.RUN > slept)
        {
            StringBuilder string = new StringBuilder();

            // Runtime
            string.append(slept  / 1000 + ",");

            // CPU-core HZ
            for(int core = 0; core < CORES; core++)
                string.append(getCPUFrequency(core) + ",");

            // SOC temperature
            string.append(getSOCTemperature());

            // Prints out the result
            Log.i("CPU", string.toString());

            // Goes to sleep
            try
            {
                sleep(Configuration.INTERVAL);
                slept += Configuration.INTERVAL;

            } catch (InterruptedException e){}
        }

        getStateTimes();
    }

    /**
     * Returns the current CPU frequency
     * @param core - the core (0-n) that you want to measure
     * @return
     */
    double getCPUFrequency(int core)
    {
        String content = exec("cat /sys/devices/system/cpu/cpu" + core + "/cpufreq/scaling_cur_freq");
        double value = (Double.parseDouble(content));
        return value / 1000;
    }


    /**
     * Retrieve the current SOC operating temperature
     * @return
     */
    double getSOCTemperature()
    {
        String content = exec("cat /sys/devices/platform/omap/omap_temp_sensor.0/temperature");
        double value = (Double.parseDouble(content));
        return value / 1000;
    }

    /**
     * Returns the current CPU state times.
     * Meaning, it returns information about - how much time the core
     * has been in a specific frequency.
     * @return
     */
    void getStateTimes(){

        for(int core = 0; core < CORES; core++)
        {
            Log.i("CPU", "CPU states: \n" + exec("cat /sys/devices/system/cpu/cpu" + core + "/cpufreq/stats/time_in_state"));
        }
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
            e.printStackTrace();
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
        BufferedReader reader = null;
        StringBuilder container = new StringBuilder();
        InputStreamReader stream = new InputStreamReader(in);
        String line;
        try
        {
            reader = new BufferedReader(stream);
            while ((line = reader.readLine()) != null)
                container.append(line + "\n");


        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return container.toString();
    }

}
