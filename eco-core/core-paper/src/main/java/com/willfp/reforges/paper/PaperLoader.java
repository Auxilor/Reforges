package com.willfp.reforges.paper;

import com.willfp.reforges.ReforgesPlugin;

public class PaperLoader {
    static {
        ReforgesPlugin plugin = ReforgesPlugin.getInstance();
        plugin.setPaperHandler(new EcoPaperHandler(plugin));
    }
}
