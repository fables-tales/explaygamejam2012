package com.samphippen.explaygamejam2012;

import java.util.ArrayList;
import java.util.List;

public class Tray {
    public static final int MAX_COGS_EVER = 40;

    private List<Cog> mCogs = new ArrayList<Cog>();

    public Tray() {
        for (int i = 0; i < MAX_COGS_EVER; i++) {
            mCogs.add(Cog.getCog());
        }
    }

    public Cog getCog() {
        if (mCogs.size() > 0) {
            return mCogs.remove(0);
        }

        return null;
    }
}
