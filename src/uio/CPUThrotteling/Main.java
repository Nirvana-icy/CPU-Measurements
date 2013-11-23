package uio.CPUThrotteling;

import android.app.Activity;
import android.os.Bundle;

public class Main extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        (new Measurer()).start();
        (new Worker()).start();
        (new Worker()).start();
    }


}
