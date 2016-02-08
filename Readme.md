LegacyPinger
============
A small utility to ping servers using pre-1.7 formats

##Usage
java -jar LegacyPinger.jar {minecraft version} {host} [port]

Supports minecraft versions 1.0.x-1.6.x
Servers of any version should support these pings.

You can also use the 'version' `all` to ping the server in all formats

##Example
`java -jar LegacyPinger all minelink.net` outputs:

````
ancient Ping::
-----------------------------------------------------------------------------------------
Successfully contacted minelink.net:25565
Message of the day: ⋮⋮⋮ MINELINK ⋮⋮⋮ Factions 6.0
Current Players: 167
Maximum Players: 1000

v15_14 Ping::
-----------------------------------------------------------------------------------------
Successfully contacted minelink.net:25565
Protocol version: 127
Minecraft Version: BungeeCord 1.8
Message of the day: §3§l⋮⋮⋮§r §b§lMINELINK §3§l⋮⋮⋮§r §a§lFactions 6.0
Current Players: 167
Maximum Players: 1000

v16 Ping::
-----------------------------------------------------------------------------------------
Successfully contacted minelink.net:25565
Protocol version: 127
Minecraft Version: BungeeCord 1.8
Message of the day: §3§l⋮⋮⋮§r §b§lMINELINK §3§l⋮⋮⋮§r §a§lFactions 6.0
Current Players: 167
Maximum Players: 1000
````
