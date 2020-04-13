# Bassebombecraft Minecraft Mod

# Introduction
BasseBombeCraft is a Minecraft mod focusing on magical books, idols and potions.

More gameplay information can be found on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/bassebombecraft).

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

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=athrane_bassebombecraft&metric=alert_status)](https://sonarcloud.io/dashboard?id=athrane_bassebombecraft)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=athrane_bassebombecraft&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=athrane_bassebombecraft)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=athrane_bassebombecraft&metric=ncloc)](https://sonarcloud.io/dashboard?id=athrane_bassebombecraft)

# Repo branches
The repository has several branches:
- A development branch for each releases:
[dev-1.31](https://github.com/athrane/bassebombecraft/tree/dev-1.31),
[dev-1.32](https://github.com/athrane/bassebombecraft/tree/dev-1.32), etc.
- Master branch: [master](https://github.com/athrane/bassebombecraft/tree/master)

# Shadowing of 3. party libraries

In order to support usage of the mod on a server, then mod uses the [Gradle Shadow plugin](https://imperceptiblethoughts.com/shadow/) 
to include these 3. party libraries within the generated jar file:
* Apache HTTP components / httpcore 
* Apache HTTP components / httpclient
* Apache Commons Logging 

See Issue #693 for additional information.
