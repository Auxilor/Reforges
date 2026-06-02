---
title: "The Gameplay"
sidebar_position: 1
---

This page explains how players actually use Reforges in-game: opening the menu, reforging an item, applying reforge stones, and how the cost scales. By the end you'll understand the loop your players see, so you can decide how to price and gate it.

## Opening the reforge menu

Players run `/reforge`, or interact with an NPC that runs `/reforges open <player>`, to open the menu:

![The Reforge GUI](https://1192817931-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FXwJPPRqRpT7b0ZXxU13J%2Fuploads%2FlvfpCSUXO0yiSJxA0bAb%2Fimage.png?alt=media&token=58de1d6d-1867-4f0e-b50c-7494de004a1a)

The glass on the side turns green when a valid item sits in the left slot, and the anvil shows status messages as you go. The whole menu is customisable in [Plugin Config](plugin-config).

![Example Anvil Message](https://1192817931-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FXwJPPRqRpT7b0ZXxU13J%2Fuploads%2Fh0JHN2nC2IVnkLB6zn9P%2Fimage%20(1).png?alt=media&token=4a00777d-1e48-4bb4-903e-591602282674)

## Reforging an item

Place the item to reforge in the **left** slot. The right slot is for reforge stones.

![A reforgable item in the GUI](https://1192817931-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FXwJPPRqRpT7b0ZXxU13J%2Fuploads%2Fwy8QgD9c50a9urJaGpBP%2Fimage%20(2).png?alt=media&token=aafd69fd-7997-4cdf-9dd1-5530b3874225)

The glass turns green to confirm the item is valid. By default reforgable items say so in their lore, which you can turn off in config:

![A reforgable item](https://1192817931-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FXwJPPRqRpT7b0ZXxU13J%2Fuploads%2FNh4WLTzUgau5kuhXzPgl%2Fimage%20(3).png?alt=media&token=840da5a5-7de0-4df6-9301-abe44fdacdcf)

An item that can't be reforged shows an error instead of turning the glass green:

![An unreforgeable item in the GUI](https://1192817931-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FXwJPPRqRpT7b0ZXxU13J%2Fuploads%2FH4hVtO52m9cH4HsDpEh5%2Fimage%20(4).png?alt=media&token=b7353bed-7a1b-4b50-ab76-cf741e0af2fd)

Click the anvil to reforge. If the player can afford it, the reforge applies with a chat message and a sound:

![The reforged item](https://1192817931-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FXwJPPRqRpT7b0ZXxU13J%2Fuploads%2FVZbcZxSblGO9rWnOD8pL%2FScreenshot%202021-10-02%20at%2013.18.31.png?alt=media&token=1b2824e3-0ff4-468e-b978-a885a5e4eb44)

## Reforge stones

Reforge stones apply specific reforges that the random roll can't give, so they're how you hand out your strongest modifiers. To use one, place the stone in the **right** slot and the item to reforge on the left, then click the anvil as normal:

![Placing a reforge stone](https://1192817931-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FXwJPPRqRpT7b0ZXxU13J%2Fuploads%2Fse1a7CtmPfkLOQIkGTey%2Fimage%20(6).png?alt=media&token=011043cc-4aa9-42bd-be9f-558eb7ee3396)

You build stones, and set a per-stone price, in [How to Make a Reforge](how-to-make-a-custom-reforge).

## Reforge cost

Each reforge costs money, set by `reforge.price` in config. Reforging the same item repeatedly gets more expensive: the price is multiplied by `cost-exponent` (default `1.15`) raised to the number of times that item has been reforged. Tune both in [Plugin Config](plugin-config).

<hr/>

## Where to go next

- **Make a reforge:** define your own modifiers in [How to Make a Reforge](how-to-make-a-custom-reforge).
- **Customise the menu:** change the GUI, sounds, and prices in [Plugin Config](plugin-config).
- **Commands:** see what you can run in [Commands and Permissions](commands-and-permissions).