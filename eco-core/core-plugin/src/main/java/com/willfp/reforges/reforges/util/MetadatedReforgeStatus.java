package com.willfp.reforges.reforges.util;

import org.jetbrains.annotations.NotNull;

public record MetadatedReforgeStatus(@NotNull ReforgeStatus status,
                                     double cost) {
}
