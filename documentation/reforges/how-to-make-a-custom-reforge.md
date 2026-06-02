---
title: "How to Make a Reforge"
sidebar_position: 2
---

A **reforge** is a modifier players apply to an item from the reforge menu to boost its stats. Each reforge is one config file that pairs a set of **effects** with the **targets** they can roll on, optionally gated behind a craftable **reforge stone**. This page gets you from an example file to a working reforge, then breaks down every part.

## Quick start

1. Open the `/plugins/Reforges/reforges/` folder.
2. Copy the included `_example.yml` and rename the copy, e.g. `dynamic.yml`. The file name is the reforge ID.
3. Edit `name`, `description`, and `targets`, then set the `effects` you want.
4. Save and run `/reforges reload`.
5. Apply it with `/reforges apply dynamic` while holding a matching item, then check the item's lore shows the reforge.

:::tip
`_example.yml` is included as a reference and is **never loaded**, so copy or rename it to make a real reforge. You can also organise reforges into subfolders inside `reforges/`, and they'll still load.
:::

## Naming and IDs

The file name without `.yml` is the reforge ID. That ID is what you pass to commands and to the [Item Lookup System](https://plugins.auxilor.io/the-item-lookup-system).

:::warning ID rules
IDs may only contain lowercase letters, numbers, and underscores (a-z, 0-9, _). No spaces, capitals, or hyphens, or the reforge will not load.
:::

## The structure of a reforge

A reforge has four parts:

| Part | What it controls |
| --- | --- |
| **Display** | The reforge name and the lore it adds to an item |
| **Targets** | Which item types the reforge can roll on |
| **Reforge stone** | An optional craftable stone that applies the reforge, with its own price |
| **Effects** | The functionality: effects, conditions, and on-reforge effects |

Here is a complete reforge with every part in place:

```yaml
# === Display: the name and lore players see ===
name: "<gradient:#AAFFA9>Dynamic</gradient:#11FFBD>" # Display name of the reforge
description: # Lore lines added to a reforged item
  - "&a+5% &fDamage"
  - "&a+10% &fCrit Damage"

# === Targets: which items it can roll on ===
targets: # Item types this reforge can be applied to
  - melee

# === Reforge stone: optional craftable applicator ===
stone:
  enabled: true # If true, this reforge can only be applied with its stone
  name: "<gradient:#AAFFA9>Dynamic</gradient:#11FFBD>&f Reforge Stone" # Display name of the stone
  lore: # Lore of the stone item
    - "&7Place on the right of the"
    - "&7reforge menu to apply the"
    - "<gradient:#AAFFA9>Dynamic</gradient:#11FFBD>&7 reforge!"
  item: player_head texture:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM0YTY1YzY4OWIyZDM2NDA5MTAwYTYwYzJhYjhkM2QwYTY3Y2U5NGVlYTNjMWY3YWM5NzRmZDg5MzU2OGI1ZCJ9fX0= # The stone's item, via the Item Lookup System
  craftable: true # If the stone has a crafting recipe
  recipe-permission: ecoitems.reforge_stone_recipe # Optional; permission needed to see and use the recipe
  shapeless: false # Optional; whether the recipe is shapeless, defaults to false
  recipe: # The 3x3 crafting grid, read left to right, top to bottom
    - air
    - ecoitems:blank_reforge_stone ? air
    - air
    - iron_block
    - daylight_sensor
    - iron_block
    - air
    - phantom_membrane
    - air
  price: # Optional; overrides the default reforge price from config.yml
    value: 100000
    type: coins # See https://plugins.auxilor.io/all-plugins/prices
    display: "&6$%value%"

# === Effects: what the reforge actually does ===
effects: # Effects run while the reforge is active
  - id: damage_multiplier
    args:
      multiplier: 1.05
    triggers:
      - melee_attack
conditions: [] # Conditions a player must meet to use the reforge
on-reforge-effects: [] # Effects run once, when the reforge is applied
```

### Display

The `name` is the reforge's display name; `description` is the lore appended to any item carrying it.

```yaml
name: "<gradient:#AAFFA9>Dynamic</gradient:#11FFBD>" # Display name of the reforge
description: # Lore lines added to a reforged item
  - "&a+5% &fDamage"
  - "&a+10% &fCrit Damage"
```

### Targets

`targets` lists the item types the reforge can roll on. A reforge only appears in the menu for items that match one of its targets.

```yaml
targets: # Item types this reforge can be applied to
  - melee
```

### Reforge stone

Set `stone.enabled: true` to make the reforge obtainable only through a stone, removing it from the random pool. The stone is a normal item with an optional recipe, and `stone.price` overrides the default reforge price when applying it.

```yaml
stone:
  enabled: true # If true, this reforge can only be applied with its stone
  name: "<gradient:#AAFFA9>Dynamic</gradient:#11FFBD>&f Reforge Stone" # Display name of the stone
  lore: # Lore of the stone item
    - "&7Place on the right of the reforge menu to apply it!"
  item: player_head texture:... # The stone's item, via the Item Lookup System
  craftable: true # If the stone has a crafting recipe
  recipe-permission: ecoitems.reforge_stone_recipe # Optional; permission needed to see and use the recipe
  shapeless: false # Optional; whether the recipe is shapeless, defaults to false
  recipe: # The 3x3 crafting grid, read left to right, top to bottom
    - air
    - ecoitems:blank_reforge_stone ? air
    - air
    - iron_block
    - daylight_sensor
    - iron_block
    - air
    - phantom_membrane
    - air
  price: # Optional; overrides the default reforge price from config.yml
    value: 100000
    type: coins # See https://plugins.auxilor.io/all-plugins/prices
    display: "&6$%value%"
```

:::tip
We support both shaped and shapeless recipes. See [Recipes](https://plugins.auxilor.io/the-item-lookup-system/recipes) for how to configure them.
:::

### Effects

`effects` run while the reforge is active, `conditions` gate who can use it, and `on-reforge-effects` fire once when it is applied.

```yaml
effects: # Effects run while the reforge is active
  - id: damage_multiplier
    args:
      multiplier: 1.05
    triggers:
      - melee_attack
conditions: [] # Conditions a player must meet to use the reforge
on-reforge-effects: [] # Effects run once, when the reforge is applied
```

:::danger Effects are their own system
Effects, conditions, and triggers are a shared system documented separately, not unique to Reforges.

- To configure a single effect, see [Configuring an Effect](https://plugins.auxilor.io/effects/configuring-an-effect).
- To string effects under one trigger, see [Configuring an Effect Chain](https://plugins.auxilor.io/effects/configuring-a-chain).
:::

:::tip Troubleshooting
- **Reforge won't load after a reload?** The file name has a capital, space, or hyphen. Rename it to lowercase letters, numbers, and underscores only.
- **Reforge never shows in the menu?** Its `targets` don't match the item you placed, or `stone.enabled` is `true` so it only applies through its stone.
- **Effects do nothing?** The effect's `triggers` don't fire for that action; check the trigger against the effects docs.
:::

<hr/>

## Where to go next

- **Default reforges:** browse the [shipped configs](https://github.com/Auxilor/Reforges/tree/master/eco-core/core-plugin/src/main/resources/reforges) for working examples.
- **Community configs:** find and share more on [lrcdb](https://lrcdb.auxilor.io/).
- **Effects reference:** learn the effect system in [Configuring an Effect](https://plugins.auxilor.io/effects/configuring-an-effect).