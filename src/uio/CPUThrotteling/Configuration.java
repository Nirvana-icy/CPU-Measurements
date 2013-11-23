package uio.CPUThrotteling;


public class Configuration
{
    // How long the measurement should run
    public static final int RUN = 7000 * 60; // 7 minutes
    // The measurement reporting interval
    public static final int INTERVAL = 25; // every n ms
    // Denotes the amount of cores available to us
    public static final int CORES = Runtime.getRuntime().availableProcessors();
    // Output filename
    public static final String FILE = "CPU-Measurement.txt";


}
