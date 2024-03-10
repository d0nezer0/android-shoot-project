package com.example.net_module.mode;

import java.util.List;

public class InitModeData {
    public List<InitMode> single_rounds;
    public int mode;//0自由训练 1是统一训练

    @Override
    public String toString() {
        return "InitModeData{" +
                "single_rounds=" + single_rounds +
                '}';
    }
}
