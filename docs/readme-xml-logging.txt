------------------------------------------------------------------------------
Battlefield 1942 dedicated server: XML event logging
------------------------------------------------------------------------------

Background
------------------------------------------------------------------------------

To better support server administrators who wish to provide their own player
statistics, DICE has incorporated a new logging system called XML event
logging into the game server.

Available both on Linux and win32, the logger will collect game events and
write them to a file in the "Logs" directory of the currently running game
mod.

The feature is intented for advanced server administrators and is currently
not available through the dedicated server launcher.

Usage
------------------------------------------------------------------------------

To enable the event logger, add the following to your ServerSettings.con file:

  game.serverEventLogging 1

By default the event logger writes uncompressed XML files directly to disk.
These files can easily grow to contain several megabytes during the course of
a game level. Therefore you can enable compression on the files to save disk
space by adding the following line to your ServerSettings.con file:

  game.serverEventLogCompression 1

With compression enabled, files will have a "zxml" extension instead of the
usual "xml" extension to indicate that they are compressed.  The compression
used is the standard zlib compression, and to uncompress the files you will
need to use a zlib-aware program or library. In the Python scripting language,
this can be accomplished by using code such as this:

	#! /usr/bin/python

	import zlib
	import sys

	input = open("input.zxml", "r")
	output = open("output.xml", "w")

	decomp_obj = zlib.decompressobj()

	while 1:
		data = input.read(256)
		if len(data) == 0: break
		output.write( decomp_obj.decompress(data) )

	input.close()
	output.close()

This script snippet will decompress the file "input.zxml" and write the
uncompressed result to the file "output.xml".

Please note that enabling compression will load the server CPU slightly more
and that compressed files are flushed to disk in blocks in a deferred manner
due to the compression algorithms.  Therefore the compression option is
unsuitable for "realtime" monitoring of the xml files.


File format description
------------------------------------------------------------------------------

The resulting XML files are in a well-formed format using XML namespaces.
There is currently no DTD or XML schema for the file format.

Rounds are encapsulated in bf:round tag sections. The total number of rounds
can be found in the bf:server section. Keep in mind that the actual number of
rounds can vary if a server administrator has issued remote commands to change
maps or to restart the level.

Each round contains bf:server section that lists all the server settings in
play for the round. These can be useful if you want to process older statistics
and don't have the ServerSettings.con file at hand or if you prefer to not
process anything but the XML file to generate stats.

Inside a bf:round block you will find bf:event tags. The bf:event tags
together with their children describe something that has happened in the game
server during the course of play. All events have a name tag and a timestamp
to make it clear to the processing program when the event happened. The events
are also sorted in order of occurance.

When a round ends a bf:roundstats block is written out, describing a snapshot
of the scoring similar to what you would see playing the game. The accumulated
statistics for all players are included together with the overall status of
the round such as the winning team and remaining number of tickets. To
calculate the length of the round, subtract the timestamp of the first event
in the round from the timestamp of the bf:roundstats tag.
