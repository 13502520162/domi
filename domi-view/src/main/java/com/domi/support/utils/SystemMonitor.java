package com.domi.support.utils;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

public class SystemMonitor {

	private static final int CPUTIME = 30;
	private static final int PERCENT = 100;
	private static final int FAULTLENGTH = 10;
	
    public static double getCpuRatioForWindows() {  
        try {  
            String procCmd = System.getenv("windir") + "//system32//wbem//wmic.exe process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";  
            // 取进程信息  
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));  
            Thread.sleep(CPUTIME);  
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));  
            if (c0 != null && c1 != null) {  
                long idletime = c1[0] - c0[0];  
                long busytime = c1[1] - c0[1];  
                return Double.valueOf(PERCENT * (busytime) / (busytime + idletime)).doubleValue();  
            } else {  
                return 0.0;  
            }  
        } catch (Exception ex) {  
            ex.printStackTrace();  
            return 0.0;  
        }  
    }  
    
    public static double getPhysicalMemoryRatio(){
    	OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / 1024;  
        // 剩余的物理内存  
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / 1024;  
        
        return ((double)totalMemorySize - (double)freePhysicalMemorySize)/totalMemorySize;
    }
    
    /** 
     * 读取CPU信息. 
     * @param proc 
     * @return 
     * @author GuoHuang 
     */  
    private static long[] readCpu(final Process proc) {  
        long[] retn = new long[2];  
        try {  
            proc.getOutputStream().close();  
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());  
            LineNumberReader input = new LineNumberReader(ir);  
            String line = input.readLine();  
            if (line == null || line.length() < FAULTLENGTH) {  
                return null;  
            }  
            int capidx = line.indexOf("Caption");  
            int cmdidx = line.indexOf("CommandLine");  
            int rocidx = line.indexOf("ReadOperationCount");  
            int umtidx = line.indexOf("UserModeTime");  
            int kmtidx = line.indexOf("KernelModeTime");  
            int wocidx = line.indexOf("WriteOperationCount");  
            long idletime = 0;  
            long kneltime = 0;  
            long usertime = 0;  
            while ((line = input.readLine()) != null) {  
                if (line.length() < wocidx) {  
                    continue;  
                }  
                // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,  
                // ThreadCount,UserModeTime,WriteOperation  
                String caption = Bytes.substring(line, capidx, cmdidx - 1).trim();  
                String cmd = Bytes.substring(line, cmdidx, kmtidx - 1).trim();  
                if (cmd.indexOf("wmic.exe") >= 0) {  
                    continue;  
                }  
                String s1 = Bytes.substring(line, kmtidx, rocidx - 1).trim();  
                String s2 = Bytes.substring(line, umtidx, wocidx - 1).trim();  
                if (caption.equals("System Idle Process") || caption.equals("System")) {  
                    if (s1.length() > 0)  
                        idletime += Long.valueOf(s1).longValue();  
                    if (s2.length() > 0)  
                        idletime += Long.valueOf(s2).longValue();  
                    continue;  
                }  
                if (s1.length() > 0)  
                    kneltime += Long.valueOf(s1).longValue();  
                if (s2.length() > 0)  
                    usertime += Long.valueOf(s2).longValue();  
            }  
            retn[0] = idletime;  
            retn[1] = kneltime + usertime;  
            return retn;  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        } finally {  
            try {  
                proc.getInputStream().close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return null;  
    }  
}
