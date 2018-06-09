package util;

import java.util.LinkedList;
import java.util.List;

public class TimeStamp {

    private static long counter = 0;
    private long curBegin;
    private String curBeginS;
    private List<Period> list;

    public TimeStamp() {
        TimeStamp.counter = 0;
        this.init();
    }

    public void init() {
        this.curBegin = 0;
        this.curBeginS = null;
        this.list = new LinkedList();
    }

    public void setBegin() {
        this.setBegin(String.valueOf(TimeStamp.counter++));
    }

    public void setBegin(String timepoint) {
        this.curBegin = System.currentTimeMillis();
        this.curBeginS = timepoint;
    }

    public void setEnd() {
        this.setEnd(String.valueOf(TimeStamp.counter++));
    }

    public void setEnd(String timepoint) {
        this.list.add(new Period(this.curBegin, this.curBeginS, System.currentTimeMillis(), timepoint));
    }

    public void setEndBegin(String timepoint) {
        this.setEnd(timepoint);
        this.setBegin(timepoint);
    }

    public void seb(String timepoint) {
        this.setEndBegin(timepoint);
    }

    private class Period {

        long begin;
        String beginS;
        long end;
        String endS;

        public Period(long b, String sb, long e, String se) {
            this.setBegin(b, sb);
            this.setEnd(e, se);
        }

        private void setBegin(long b, String sb) {
            this.begin = b;
            this.beginS = sb;
        }

        private void setEnd(long e, String se) {
            this.end = e;
            this.endS = se;
        }

        @Override
        public String toString() {
            return "From '" + this.beginS + "' till '" + this.endS + "' is " + (this.end - this.begin) + " mSec.";
        }
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        for (Period p : this.list) {
            buffer.append(p.toString());
            buffer.append('\n');
        }
        return buffer.toString();
    }
}
