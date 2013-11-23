package uio.CPUThrotteling;


import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Writer
{
    public static void write(String content)
    {
        try
        {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), Configuration.FILE);
            FileWriter filewriter = new FileWriter(file, true);
            BufferedWriter out = new BufferedWriter(filewriter);
            out.write("\n----------------------------------------------\n");
            out.write(content + "\n\n");
            out.close();
        } catch (Exception e)
        {
            Log.e("CPU", "Could not write file " + e.getMessage());
        }

        Log.i("CPU", "Wrote to file.");
    }
}
