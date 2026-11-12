package com.willfp.reforges.commands

import com.willfp.reforges.ReforgesPlugin
import wiki.ecohub.core.command.command

internal fun ReforgesPlugin.reforgesCommand() = command(name = "reforges", permission = "reforges.command.reforges") {
    reloadCommand()
    giveCommand()
    openCommand()
    applyCommand()
}
