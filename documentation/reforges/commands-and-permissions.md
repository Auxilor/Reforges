---
title: "Commands and Permissions"
sidebar_position: 4
---

Every command Reforges adds, with the permission node that controls it. Grant the nodes you want players or staff to have; all of them default to operators.

| Command                                    | Description                                                                   | Permission                 |
|--------------------------------------------|-------------------------------------------------------------------------------|----------------------------|
| `/reforge`                                 | Open the reforge menu for yourself                                            | `reforges.command.reforge` |
| `/reforges apply <reforge> [player]`       | Apply a reforge to the item you're holding (optionally target another player) | `reforges.command.apply`   |
| `/reforges give <player> <stone> [amount]` | Give a player a reforge stone (e.g. `/reforges give <player> lavish 2`)       | `reforges.command.give`    |
| `/reforges open <player>`                  | Open the reforge menu for another player                                      | `reforges.command.open`    |
| `/reforges reload`                         | Reload the plugin                                                             | `reforges.command.reload`  |
| `/reforges import <id>`                    | Import a reforge from [lrcdb](https://lrcdb.auxilor.io/)                      | `reforges.command.import`  |
| `/reforges export <id>`                    | Export a reforge to [lrcdb](https://lrcdb.auxilor.io/)                        | `reforges.command.export`  |

<hr/>

## Where to go next

- **The gameplay:** see what `/reforge` opens in [The Gameplay](the-gameplay).
- **Make a reforge:** create reforges to `apply` and `give` in [How to Make a Reforge](how-to-make-a-custom-reforge).
- **Config:** customise the menu in [Plugin Config](plugin-config).