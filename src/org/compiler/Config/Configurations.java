package org.compiler.Config;

public class Configurations {
    public static final String grammarPath = "configFiles/grammar.txt";
    public static final String inputPath = "configFiles/input.txt";
    public static final String configPath = "configFiles/config.txt";

    public boolean syncErrorsFlag;
    public boolean printOutputFlag;

    public Configurations(){
        syncErrorsFlag = false;
        printOutputFlag = false;
    }

    public boolean isSyncErrorsFlag() {
        return syncErrorsFlag;
    }

    public void setSyncErrorsFlag(boolean syncErrorsFlag) {
        this.syncErrorsFlag = syncErrorsFlag;
    }

    public boolean isPrintOutputFlag() {
        return printOutputFlag;
    }

    public void setPrintOutputFlag(boolean printOutputFlag) {
        this.printOutputFlag = printOutputFlag;
    }
}