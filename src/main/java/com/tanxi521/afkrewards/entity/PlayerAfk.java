package com.tanxi521.afkrewards.entity;

public class PlayerAfk implements Comparable<PlayerAfk>{

    private String player_uuid;
    private String displayName;
    private String type;
    private long start;
    private long end;
    private int times;
    private long total;

    @Override
    public String toString() {
        return "PlayerAfk{" +
                "player_uuid='" + player_uuid + '\'' +
                ", displayName='" + displayName + '\'' +
                ", type='" + type + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", times=" + times +
                ", total=" + total +
                '}';
    }

    public String getPlayer_uuid() {
        return player_uuid;
    }

    public void setPlayer_uuid(String player_uuid) {
        this.player_uuid = player_uuid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public int compareTo(PlayerAfk o) {
        int i = (int)(o.getTotal()/1000 - this.getTotal()/1000);
        if (i == 0) {
            return o.getTimes() - this.getTimes();
        }
        return i;
    }
}
