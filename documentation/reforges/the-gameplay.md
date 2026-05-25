---
title: "The Gameplay"
sidebar_position: 1
---

## How to reforge an item

### Step One: Open the GUI

Run `/reforge` or go to an NPC that runs `/reforge open <player>`.

This will open this GUI:

![The Reforge GUI](https://1192817931-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FXwJPPRqRpT7b0ZXxU13J%2Fuploads%2FlvfpCSUXO0yiSJxA0bAb%2Fimage.png?alt=media&token=58de1d6d-1867-4f0e-b50c-7494de004a1a)

The reforge menu is very simple to understand:

The glass on the side goes green when there is a valid item that can be reforged in the left slot

The anvil will show helpful messages:

![Example Anvil Message](https://1192817931-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FXwJPPRqRpT7b0ZXxU13J%2Fuploads%2Fh0JHN2nC2IVnkLB6zn9P%2Fimage%20(1).png?alt=media&token=4a00777d-1e48-4bb4-903e-591602282674)

The entire GUI is fully customizable and explained in config.yml

### Step Two: Place an item in teh GUI

To reforge an item, put it **on the left side** of the GUI. The right side is for reforge stones, which will be explained in the next section.

![A reforgable item in the GUI](https://1192817931-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FXwJPPRqRpT7b0ZXxU13J%2Fuploads%2Fwy8QgD9c50a9urJaGpBP%2Fimage%20(2).png?alt=media&token=aafd69fd-7997-4cdf-9dd1-5530b3874225)

The glass will then go green to indicate that a valid item has been entered.

By default, all reforgable items will say so in their lore:

![A reforgable item](https://1192817931-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FXwJPPRqRpT7b0ZXxU13J%2Fuploads%2FNh4WLTzUgau5kuhXzPgl%2Fimage%20(3).png?alt=media&token=840da5a5-7de0-4df6-9301-abe44fdacdcf)

But you can turn this off if you don't like it.

If you try to place an unreforgeable item in the GUI, you will get this:

![An unreforgeable item in the GUI](https://1192817931-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FXwJPPRqRpT7b0ZXxU13J%2Fuploads%2FH4hVtO52m9cH4HsDpEh5%2Fimage%20(4).png?alt=media&token=b7353bed-7a1b-4b50-ab76-cf741e0af2fd)

### Step Three: Click the anvil

By default, the price increases by a factor of 1.15x every time the same item is reforged. You can change this in config.yml.

![Ready to reforge!](https://1192817931-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FXwJPPRqRpT7b0ZXxU13J%2Fuploads%2FuoWrAAprorYkC4KPj4MG%2Fimage%20(5).png?alt=media&token=9839630b-6532-4a5c-8935-2c2d4faae642)

If you have enough money, the reforge will successfully happen, and you will get a message in chat and a sound specified in config.yml

![The reforged item](https://1192817931-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FXwJPPRqRpT7b0ZXxU13J%2Fuploads%2FVZbcZxSblGO9rWnOD8pL%2FScreenshot%202021-10-02%20at%2013.18.31.png?alt=media&token=1b2824e3-0ff4-468e-b978-a885a5e4eb44)

Congratulations! You've successfully reforged an item!

## Reforge Stones

Reforge Stones give reforges that are unobtainable without them. They give some of the most useful and powerful reforges.

### How to use

To use a reforge stone, place it in the right of the reforge menu:

![Placing a reforge stone](https://1192817931-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FXwJPPRqRpT7b0ZXxU13J%2Fuploads%2Fse1a7CtmPfkLOQIkGTey%2Fimage%20(6).png?alt=media&token=011043cc-4aa9-42bd-be9f-558eb7ee3396)

Then, place the item that you want to reforge on the left side and click the anvil as normal!

![Ready to apply the reforge stone](https://1192817931-files.gitbook.io/~/files/v0/b/gitbook-x-prod.appspot.com/o/spaces%2FXwJPPRqRpT7b0ZXxU13J%2Fuploads%2Fr8fC7r1ydc5cA7U9bJIy%2Fimage%20(7).png?alt=media&token=42ea54d9-2bbd-4d9a-9019-079346da7ec7)

You can specify a custom price to apply the reforge in config