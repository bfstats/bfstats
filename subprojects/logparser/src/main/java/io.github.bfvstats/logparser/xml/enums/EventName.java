package io.github.bfvstats.logparser.xml.enums;

public enum EventName {
  UNKNOWN_EVENT, // easier usage, to avoid null
  changePlayerName,
  chat,
  createPlayer,
  createVehicle, // no playerId; not useful
  destroyPlayer,
  destroyVehicle, // sometimes no playerId
  disconnectPlayer,
  endGame,
  enterVehicle,
  exitVehicle,
  pickupKit,
  playerKeyHash,
  radioMessage,
  restartMap, // no playerId
  roundInit, // no playerId
  sayAll, // no playerId; not useful
  scoreEvent,
  setTeam,
  spawnEvent,

  // not yet validated if these are in bfv:
  beginMedPack, endMedPack,
  beginRepair, endRepair
}

// Maybe these exist as well: connectPlayer, pickupFlag, reSpawnEvent