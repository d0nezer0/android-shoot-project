package com.example.net_module.mode;

public class InitMode {
    public int bout_id;
    public int bout_num;
    public int shoot_num;
    public long user_id;
    public String user_name;
    public int bullet_count;

    @Override
    public String toString() {
        return "InitMode{" +
                "bout_id=" + bout_id +
                ", shoot_num=" + shoot_num +
                ", single_round_id=" + bout_num +
                ", user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
