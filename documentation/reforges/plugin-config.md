---
title: "Plugin Config"
sidebar_position: 5
---

`config.yml` controls the reforge menu, the default price, and how reforges show on items. It lives at `/plugins/Reforges/config.yml`. Edit it, then run `/reforges reload` to apply your changes.

:::warning
`no-offhand` only takes effect after a full server restart; `/reforges reload` will not apply it.
:::

## Default config.yml

```yaml
discover-recipes: true # If reforge stone recipes show in the recipe book once unlocked
no-offhand: false # If true, reforges never apply to the offhand slot; needs a restart, not a reload

# Price multipliers by permission. A player's price is multiplied by the
# highest-priority multiplier they have permission for, so a player with both
# the vip and mvp nodes below gets the mvp one, as it has the higher priority.
price-multipliers:
  - permission: reforges.mutliplier.vip
    multiplier: 0.8 # 20% off
    priority: 1
  - permission: reforges.mutliplier.mvp
    multiplier: 0.7 # 30% off
    priority: 2

gui:
  rows: 6 # Number of rows in the menu, 1 to 6

  mask:
    # The mask fills the menu background. List the materials, then draw a
    # pattern referencing them: each line is one row and must be 9 long.
    # 0 is empty, 1 is the first material, 2 the second, and so on up to 9.
    materials:
      - black_stained_glass_pane
      - magenta_stained_glass_pane
    pattern:
      - "011111110"
      - "012202210"
      - "012111210"
      - "010111010"
      - "011111110"
      - "011101110"

  show-allowed:
    allow-material: lime_stained_glass_pane # Shown in a marked slot when the item can be reforged
    deny-material: red_stained_glass_pane # Shown in a marked slot when it cannot
    # 1 marks a show-allowed slot, 0 leaves the slot to the mask above.
    pattern:
      - "100000001"
      - "100000001"
      - "100000001"
      - "100000001"
      - "100000001"
      - "100000001"

  activator-slot: # The slot players click to reforge
    row: 2
    column: 5

  stone-slot: # The slot a reforge stone goes in
    row: 4
    column: 7

  item-slot: # The slot the item to reforge goes in
    row: 4
    column: 3

  close: # The button that closes the menu
    material: barrier
    location:
      row: 6
      column: 5

  allow: # The activator item when an item is ready to reforge
    material: anvil
    name: "&aReforge Item"
    lore:
      - '&7Reforges the item on the left, giving'
      - '&7it a random item modifier that'
      - '&7boosts its stats.'
      - ''
      - '&7Price:'
      - '%price%' # Replaced with the current reforge price
      - ''
      - '&eClick to reforge!'

  allow-stone: # The activator item when a reforge stone is loaded
    material: anvil
    name: "&aReforge Item"
    lore:
      - '&7Reforges the item on the left with'
      - '&7a reforge stone, giving it'
      - '&7the %stone%&7 reforge to' # %stone% is the loaded stone's reforge name
      - '&7boost its stats.'
      - ''
      - '&7Price:'
      - '%price%'
      - ''
      - '&eClick to reforge!'

  no-item: # The activator item when the item slot is empty
    material: anvil
    name: "&eReforge Item"
    lore:
      - '&7Place an item on the left to'
      - '&7reforge it! Reforging items adds a'
      - '&7random modifier to the item that'
      - '&7grants stat boosts'

  invalid-item: # The activator item when the placed item cannot be reforged
    material: barrier
    name: "&cError!"
    lore:
      - '&7You cannot reforge this item!'

  sound: # Played on a successful reforge
    enabled: true
    sound: BLOCK_ANVIL_USE
    pitch: 1
    volume: 1
    category: PLAYERS

  open-sound: # Played when the menu opens
    enabled: true
    sound: BLOCK_ANVIL_PLACE
    pitch: 0.8
    volume: 1
    category: UI

  stone-sound: # Played when a reforge stone is applied
    enabled: true
    sound: ENTITY_ENDER_DRAGON_HURT
    pitch: 0.5
    volume: 1
    category: PLAYERS

  cannot-afford-sound: # Played when the player can't afford the reforge
    enabled: true
    sound: ENTITY_VILLAGER_NO
    pitch: 0.8
    volume: 1
    category: PLAYERS

reforge:
  price: # Default cost to reforge, unless a reforge sets its own; see https://plugins.auxilor.io/all-plugins/prices
    value: 7500
    type: coins
    display: "&6$%value%"

  cost-exponent: 1.15 # Price scales as (times this item has been reforged ^ exponent) * price

  show-reforgable: true # If reforgable items get the suffix below added to their lore
  reforgable-suffix: # Lore appended to items that can be reforged
    - ""
    - "&8This item can be reforged!"

  display-in-lore: true # If a reforge's description shows in the item's lore
  display-in-name: true # If the reforge name shows in the item's name

  reforged-prefix: # Lore shown above a reforged item's description; %reforge% is the reforge name
    - ""
    - "%reforge%"
```

<hr/>

## Where to go next

- **Make a reforge:** turn this menu into something to apply in [How to Make a Reforge](how-to-make-a-custom-reforge).
- **Prices:** see the price formats in the [eco prices docs](https://plugins.auxilor.io/all-plugins/prices).
- **Commands:** manage the plugin from [Commands and Permissions](commands-and-permissions).