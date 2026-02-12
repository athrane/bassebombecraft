# Bassebombecraft Minecraft Mod

# Introduction
BassebombeCraft is a Minecraft mod focusing on magical books, idols and potions.
More gameplay information can be found on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/bassebombecraft).

# Downloads

## Files
The mod can be downloaded from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/bassebombecraft).
and from [GitHub](https://github.com/athrane/bassebombecraft/releases).

## Docker image
A Docker image is created with a Minecraft server and the server version of the mod. 
The image is derived from the [itzg/minecraft-server](https://github.com/itzg/docker-minecraft-server) image. 
The image is published to DockerHub by the name: [bassebombecraft/minecraft](https://hub.docker.com/r/bassebombecraft/minecraft).

## How to use the image
Start by pulling the image from a registry: `docker pull bassebombecraft/minecraft`
and then start the image: `docker run bassebombecraft/minecraft`

The container will start with a random name. The Minecraft server will listen on port 25565.

# Configuration

The mod maintains its configuration in the configuration file named `bassebombecraft-common.toml`.

## Disabling the welcome message

The welcome message can be disabled by setting the `enableWelcomeMessage` property to `false` in the configuration file:

    [General.BasseBombeCraft]
	#Defines if MOD welcome message is enabled when mod is loaded.
    enableWelcomeMessage = false

# Reviews

Mod reviews from Youtube:

## Review by ItzVintriX
[![BasseBombeCraft review](https://img.youtube.com/vi/w38xET4C0wU/0.jpg)](https://www.youtube.com/watch?v=w38xET4C0wU "BasseBombeCraft review")

## Review by AznDarkproduction
[![BasseBombeCraft review](https://img.youtube.com/vi/wsRMLX1ryWY/0.jpg)](https://www.youtube.com/watch?v=wsRMLX1ryWY "BasseBombeCraft review")

## Review by yarikpika
[![BasseBombeCraft review](https://img.youtube.com/vi/jf1-MKK3-yI/0.jpg)](https://www.youtube.com/watch?v=jf1-MKK3-yI "BasseBombeCraft review")

## Review by LeKoopa
[![BasseBombeCraft review](https://img.youtube.com/vi/9F4whU9KMBs/0.jpg)](https://www.youtube.com/watch?9F4whU9KMBs "BasseBombeCraft review")

# Development badges
![](https://github.com/athrane/bassebombecraft/workflows/Build%20and%20release/badge.svg)

[![CodeFactor](https://www.codefactor.io/repository/github/athrane/bassebombecraft/badge)](https://www.codefactor.io/repository/github/athrane/bassebombecraft)

# Technical stuff

The technical aspects of the mod are documented on the project
[Wiki](https://github.com/athrane/bassebombecraft/wiki).

