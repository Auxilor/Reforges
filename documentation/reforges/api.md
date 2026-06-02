---
title: "API"
sidebar_position: 6
---

This page is for developers who want to build against Reforges from their own plugin. Reforges is open-source, so you can read the code and depend on it directly.

## Source code

The source code is on [GitHub](https://github.com/Auxilor/Reforges).

## Adding the dependency

1. Add the Auxilor repository to your `build.gradle.kts`.
2. Add Reforges as a `compileOnly` dependency.

```kotlin
repositories {
    maven("https://repo.auxilor.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.willfp:Reforges:<version>")
}
```

The latest version available on the repo can be found [here](https://github.com/Auxilor/Reforges/tags).

<hr/>

## Where to go next

- **Shared APIs:** most cross-plugin APIs live in the [eco framework](https://github.com/Auxilor/eco).
- **Config side:** to define reforges in config instead of code, see [How to Make a Reforge](how-to-make-a-custom-reforge).