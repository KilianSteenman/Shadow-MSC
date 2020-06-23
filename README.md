# Shadow-MSC
![CI](https://github.com/KilianSteenman/Shadow-MSC/workflows/CI/badge.svg?branch=master)
[![](https://jitpack.io/v/KilianSteenman/Shadow-MSC.svg)](https://jitpack.io/#KilianSteenman/Shadow-MSC)

Shadow Mission Script Compiler is a super basic (and I mean basic!) mission script compiler for use with GTA: VC.

## About
As a dev that mainly uses a MacBook for development, there currently aren't that many GTA Modding tools available that run on my preferred platform.
For my side project [OASE](https://www.youtube.com/playlist?list=PLOxyV5A-M9P38WibzT8wnz0Teq9oMzqbU) I often find myself writing simple scripts to test certain opcode implementations. Switching between my desktop and MacBook just to compile a script became a hassle so I finally decided to write a super simple cross platform compiler.

## Disclaimer
This tool is purely written for my personal needs and will never be as advanced as any of the other tools/compilers available.

## Known issues
- No support for threads
- No support for objects
- There is no error handling at all

Yep it's pretty useless right now!

## Usage
**Using the compiler as a library in your project**

Add the [Jitpack.io](www.jitpack.io) repository to your project root `build.gradle` file

```groovy
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

Add the dependency [![](https://jitpack.io/v/KilianSteenman/Shadow-MSC.svg)](https://jitpack.io/#KilianSteenman/Shadow-MSC)
```groovy
dependencies {
    implementation 'com.github.KilianSteenman:Shadow-MSC:<LATEST VERSION>'
}
```

**Using the compiler as a standalone executable**

Download the provided jar and execute it, providing the required arguments.

Available Arguments:

| Argument    | Description                               | Required |
|-------------|-------------------------------------------|----------|
| --main      | Provide path to the main script.           | No*      |
| --cleo      | Provide path to the cleo script.           | No*      |
| -o / --output | Provide output path for the compiled script. When this argument is not provided the scm will be exported at the same path as the main script. | No       |
|             | List of paths to mission scripts.          | No       |
*At least the main or the cleo path should be provided.

Example usage:
```
java -jar shadow-msc.jar --main ./examples/simple.dsc -o ./examples/simple.scm
```
Including missions:
```
java -jar shadow-msc.jar --main ./examples/mission/main.dsc ./examples/mission/mission_1.dsc
```

The output parameter is optional, if the output parameter is not provided the scm will be exported to the same path as the input.


## Examples
In the examples directory there are a couple of simple example scripts.

**[Stripped](./examples/stripped.dsc)**

A script which only spawns a player.

**[Simple](./examples/simple.dsc)**

Spawns a player, a car, a faggio and an actor. There is a mission sphere located near the actor. When the player enters the sphere, the actor will walk towards another sphere, once the actor reaches the second sphere he will start running towards the last sphere. Once this sphere has been reached, the actor will start spinning in circles.

**[Mission](./examples/mission/)**

Creates a start mission sphere. When the player enters the sphere the mission starts. The mission spawns 2 spheres. Entering one sphere will pass the mission, the other will fail the mission.