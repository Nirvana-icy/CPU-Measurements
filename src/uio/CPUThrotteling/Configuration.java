package uio.CPUThrotteling;


public class Configuration
{
    // How long the measurement should run
    public static final int RUN = 5000 * 60; // 5 minutes
    // The measurement reporting interval
    public static final int INTERVAL = 1000; // every n seconds
    // Denotes the amount of cores available to us
    public static final int CORES = Runtime.getRuntime().availableProcessors();

}
