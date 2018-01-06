package io.github.bfstats.logparser.xml.enums;

public enum EventName {
  UNKNOWN_EVENT, // easier usage, to avoid null
  changePlayerName,
  chat,
  connectPlayer, // we use playerKeyHash instead
  createPlayer,
  createVehicle, // no playerId; not useful
  destroyPlayer,
  destroyVehicle, // sometimes no playerId
  disconnectPlayer, // we use destroyPlayer instead
  endGame, // we use round end stats instead (but could use this to reset team maybe until spawnEvent?)
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
  beginRepair, endRepair,
  gamePaused, gameUnpaused, // no parameters
  deployObject, undeployObject, // params: type
  attachToHook, detachFromHook // params: cargo
}


/*
        gamePaused event (no parameters)
        gameUnpaused event (no parameters)
        sayAll event (text parameter)
        ip parameter to existing connectPlayer event
        endGame event
            reason = timelimit, scoreLimit, tickets
            winner = 1|2
            winnerScore
            loserScore
        deployObject/undeployObject event
            type
        attachToHook/detachFromHook event
            cargo
        createVehicle event
            type
            team
 */

// Maybe these exist as well: connectPlayer, pickupFlag, reSpawnEvent
