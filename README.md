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
- No support for mission scripts
- No support for objects
- There is no error handling at all
- When compiling files are appended instead of overwritten

Yep it's pretty useless right now!

## Examples
In the examples directory there are a couple of simple example scripts.

*Stripped*

A script which only spawns a player.

*Simple*

Spawns a player, a car, a faggio and an actor. There is a missions sphere located near the actor. When the player enters the sphere, the actor will walk towards another sphere, once the actor reaches the second sphere he will start running towards the last sphere. Once this sphere has been reached, the actor will start spinning in circles.