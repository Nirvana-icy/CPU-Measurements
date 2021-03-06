package uio.CPUThrotteling;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.ArrayList;

import static uio.CPUThrotteling.Configuration.CORES;
import static uio.CPUThrotteling.Configuration.RUN;


public class Measurer extends Thread
{
    // Denotes how long this measurer has slept
    int slept = 0;
    final long start = System.currentTimeMillis();
    ArrayList<String> log = new ArrayList<String>(4096);

    public void run()
    {
        getStateTimes();

        while (keepSampling())
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
            log( string.toString() + "\n");

            // Goes to sleep
            try
            {
                sleep(Configuration.INTERVAL);
            } catch (InterruptedException e){}
        }

        getStateTimes();
        scribble();
    }

    boolean keepSampling(){
        return (System.currentTimeMillis() - start) < RUN;
    }

    /**
     * Save the log results to file
     */
    void scribble()
    {
        String content = "";
        for(String l : log)
            content += l;

        Writer.write(content);
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
    String states = "";
    void getStateTimes()
    {
        for(int core = 0; core < CORES; core++)
        {
            states += exec("cat /sys/devices/system/cpu/cpu" + core + "/cpufreq/stats/time_in_state") + "\n";
        }

        log( "CPU states: \n" + states);
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

    void log(String content){
        //Log.i("CPU", content);
        this.log.add(content);
    }
}
