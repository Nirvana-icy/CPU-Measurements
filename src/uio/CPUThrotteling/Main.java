package uio.CPUThrotteling;

import android.app.Activity;
import android.os.Bundle;

import static uio.CPUThrotteling.Configuration.CORES;

public class Main extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Starts the measurer
        (new Measurer()).start();

        // Starts a worker node for each CPU core
        for(int core = 0; core < CORES; core++)
        {
            (new Worker()).start();
        }

    }
}
