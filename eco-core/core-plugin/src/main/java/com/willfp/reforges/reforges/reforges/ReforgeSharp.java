package com.willfp.reforges.reforges.reforges;

import com.willfp.reforges.reforges.Reforge;
import com.willfp.reforges.reforges.meta.ReforgeTarget;

public class ReforgeSharp extends Reforge {
    public ReforgeSharp() {
        super("sharp");
    }

    @Override
    public ReforgeTarget getTarget() {
        return ReforgeTarget.MELEE;
    }
}
