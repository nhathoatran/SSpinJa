package sspinja;

public final class Config {
    public static int DFS = 0;
    public static int Accept = 1;
    public static int SchedulerCTL = 2;
    public static int SchedulerAccept = 3;
    public static int TestDFS = 4;
    public static int CTL = 5;
    public static int SchedulerDFS = 6;
    public static int Heuristic = 7;
    public static int Liveness = 8;
    public static int LivenessHeuristic = 9;
    public static int SchedulerCTLGen = 10;
    public static int SchedulerMulticoreCTL = 11;   
    public static int searchOption = 0 ;
    
    public static boolean isCompileScheduler = false ;
    public static boolean isCompileOnlyTest = false ;    
    public static boolean isSchedulerSearch = false ;
    public static boolean isCheckAcceptionCycle = false ;
    public static boolean isCheckLiveLock = false ;
    public static boolean processInit = false ;
    
    public static boolean processLimit = false ;
} 