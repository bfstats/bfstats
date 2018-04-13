package io.github.bfstats.service;

import com.google.common.collect.ImmutableMap;
import io.github.bfstats.model.KitUsage;
import lombok.Data;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.util.List;
import java.util.Map;

import static io.github.bfstats.dbstats.jooq.Tables.ROUND;
import static io.github.bfstats.dbstats.jooq.Tables.ROUND_PLAYER_PICKUP_KIT;
import static io.github.bfstats.service.PlayerService.LIMIT_PLAYER_STATS;
import static io.github.bfstats.util.DbUtils.getDslContext;
import static io.github.bfstats.util.Utils.percentage;
import static java.util.stream.Collectors.toList;

public class KitService {
  private static Map<String, String> teamNameByCodeBf1942 = ImmutableMap.<String, String>builder()
      .put("German", "Germany")
      .put("GB", "United Kingdom")
      .put("Jap", "Japan")
      .put("US", "USA")
      .put("Canadian", "Canada")
      .put("Russ", "Soviet Union")
      .put("Iraq", "Iraq") // Desert Combat mod
      .build();

  private static Map<String, String> teamNameByCodeBfVietnam = ImmutableMap.<String, String>builder()
      .put("NVA", "NVA")
      .put("Vietcong", "VC")
      .put("ARVN", "ARVN")
      .put("SpecialForces", "USA")
      .put("USArmy", "USA")
      .put("USMarine", "USA")
      .build();

  private static Map<String, Map<String, String>> teamNameByCodeByGameCode = ImmutableMap.<String, Map<String, String>>builder()
      .put("bf1942", teamNameByCodeBf1942)
      .put("bfvietnam", teamNameByCodeBfVietnam)
      .build();


  private static Map<String, String> categoryNameByCodeBf1942 = ImmutableMap.<String, String>builder()
      .put("Assault", "Assault")
      .put("Engineer", "Engineer")
      .put("AT", "Anti-Tank")
      .put("Scout", "Scout")
      .put("Medic", "Medic")
      .build();

  private static Map<String, String> categoryNameByCodeBfVietnam = ImmutableMap.<String, String>builder()
      .put("Assault", "Assault")
      .put("Engineer", "Engineer")
      .put("HeavyAssault", "Heavy Assault")
      .put("Scout", "Scout")
      .put("Recon", "Recon")
      .build();

  private static Map<String, Map<String, String>> categoryNameByCodeByGameCode = ImmutableMap.<String, Map<String, String>>builder()
      .put("bf1942", categoryNameByCodeBf1942)
      .put("bfvietnam", categoryNameByCodeBfVietnam)
      .build();


  private static Map<String, String> weaponsByKitCodeBf1942 = ImmutableMap.<String, String>builder()
      .put("German_Scout_Desert", "K98K-WALTHERP38-BINOCULARS-GRENADE")
      .put("German_Assault_Desert", "German_Assault_Desert")
      .put("German_AT_Desert", "German_AT_Desert")
      .put("German_Medic_Desert", "German_Medic_Desert")
      .put("German_Engineer_Desert", "German_Engineer_Desert")
      .put("GB_Scout", "GB_Scout")
      .put("GB_Assault", "GB_Assault")
      .put("GB_AT", "GB_AT")
      .put("GB_Medic", "GB_Medic")
      .put("GB_Engineer", "GB_Engineer")
      // TODO: fill it with real info
      .build();

  private static Map<String, String> weaponsByKitCodeBfVietnam = ImmutableMap.<String, String>builder()
      .put("NVA_Assault", "AK47-GRENADES")
      .put("NVA_Assault_Alt", "RPD-GRENADES")
      .put("NVA_Engineer", "MAT49-BOOBY TRAP-MORTAR-WRENCH")
      .put("NVA_Engineer_Alt", "MAT49-MINES-SHOVEL-WRENCH")
      .put("NVA_HeavyAssault", "TYPE56-RPG7V")
      .put("NVA_HeavyAssault_Alt", "SA7-EXPACK")
      .put("NVA_Scout", "SVD-CALTROPS-BINOCULARS")
      .put("NVA_Scout_Alt", "TYPE56-BOUNCING BETTY-BINOCULARS")

      .put("Vietcong_Assault", "AKMS-GRENADES")
      .put("Vietcong_Assault_Alt", "TYPE 53-GRENADES")
      .put("Vietcong_Engineer", "MAT49-PUNGI STICKS-MORTAR-WRENCH")
      .put("Vietcong_Engineer_Alt", "MAT49-SHOVEL-LANDMINES-WRENCH")
      .put("Vietcong_HeavyAssault", "TYPE 56-RPG2")
      .put("Vietcong_HeavyAssault_Alt", "SA7-EXPACK")
      .put("Vietcong_Scout", "M91/30-CALTROPS-BINOCULARS")
      .put("Vietcong_Scout_Alt", "TYPE56-BOUNCING BETTY-TIMEBOMB")

      .put("ARVN_Assault", "CAR15-GRENADES")
      .put("ARVN_Assault_Alt", "M16-GRENADES")
      .put("ARVN_Engineer", "M14-MINES-MORTAR-WRENCH")
      .put("ARVN_Engineer_Alt", "M14-TORCH-C4-WRENCH")
      .put("ARVN_HeavyAssault", "M60-M79")
      .put("ARVN_HeavyAssault_Alt", "M14-L.A.W.")
      .put("ARVN_Recon", "M21-SMOKE-BINOCULARS")
      .put("ARVN_Recon_Alt", "M16SNIPER-SMOKE-BINOCULARS")

      .put("SpecialForces_Assault", "CAR15-GRENADES-MEDPACK")
      .put("SpecialForces_Assault_Alt", "XM148-MEDPACK")
      .put("SpecialForces_Engineer", "M14-MINES-MORTAR-WRENCH")
      .put("SpecialForces_Engineer_Alt", "M14-TORCH-C4-WRENCH")
      .put("SpecialForces_HeavyAssault", "M60-M79")
      .put("SpecialForces_HeavyAssault_Alt", "M14-L.A.W.")
      .put("SpecialForces_Recon", "M40-SMOKE-BINOCULARS")
      .put("SpecialForces_Recon_Alt", "M16SNIPER-SMOKE-BINOCULARS")

      .put("USArmy_Assault", "M16-GRENADES")
      .put("USArmy_Assault_Alt", "MOSSBERG 500-GRENADES")
      .put("USArmy_Engineer", "M14-TORCH-CLAYMORES-WRENCH")
      .put("USArmy_Engineer_Alt", "M14-MINES-MORTAR-WRENCH")
      .put("USArmy_HeavyAssault", "M60-M79")
      .put("USArmy_HeavyAssault_Alt", "M14-L.A.W.")
      .put("USArmy_Recon", "M21-SMOKE-BINOCULARS")
      .put("USArmy_Recon_Alt", "M16SNIPER-SMOKE-BINOCULARS")

      .put("USMarine_Assault", "M16-GRENADES")
      .put("USMarine_Assault_Alt", "MOSSBERG 500-GRENADES")
      .put("USMarine_Engineer", "M14-TORCH-CLAYMORES-WRENCH")
      .put("USMarine_Engineer_Alt", "M14-MINES-MORTAR-WRENCH")
      .put("USMarine_HeavyAssault", "M60-M79")
      .put("USMarine_HeavyAssault_Alt", "M14-L.A.W.")
      .put("USMarine_Recon", "M40-SMOKE-BINOCULARS")
      .put("USMarine_Recon_Alt", "M16SNIPER-SMOKE-BINOCULARS")
      .build();

  private static Map<String, Map<String, String>> weaponsByKitCodeByGameCode = ImmutableMap.<String, Map<String, String>>builder()
      .put("bf1942", weaponsByKitCodeBf1942)
      .put("bfvietnam", weaponsByKitCodeBfVietnam)
      .build();

  @Data
  public static class KitNameAndWeapons {
    String name;
    String weapons;

    @Override
    public String toString() {
      return name + " (" + weapons + ")";
    }
  }

  public static KitNameAndWeapons findKitNameAndWeapons(String gameCode, String kitCode) {
    String[] parts = kitCode.split("_");
    if (parts.length < 2) {
      KitNameAndWeapons kitNameAndWeapons = new KitNameAndWeapons();
      kitNameAndWeapons.setName(kitCode);
      kitNameAndWeapons.setWeapons(kitCode);
      return kitNameAndWeapons;
    }

    String teamCode = parts[0];
    String teamName = teamNameByCodeByGameCode.get(gameCode).getOrDefault(teamCode, teamCode);

    String categoryCode = parts[1];
    String categoryName = categoryNameByCodeByGameCode.get(gameCode).getOrDefault(categoryCode, categoryCode);

    String kitName = teamName + " " + categoryName;

    if (parts.length > 2) {
      String categoryAlt = parts[2];
      kitName += " " + categoryAlt;
    }

    KitNameAndWeapons kitNameAndWeapons = new KitNameAndWeapons();
    kitNameAndWeapons.setName(kitName);

    String weapons = weaponsByKitCodeByGameCode.get(gameCode).getOrDefault(kitCode, kitCode);
    kitNameAndWeapons.setWeapons(weapons);
    return kitNameAndWeapons;
  }

  public KitUsage getKit(String gameCode, String kitCode) {
    KitNameAndWeapons kitNameAndWeapons = findKitNameAndWeapons(gameCode, kitCode);
    return new KitUsage()
        .setGameCode(gameCode)
        .setCode(kitCode)
        .setName(kitNameAndWeapons.getName())
        .setWeapons(kitNameAndWeapons.getWeapons());
  }

  public List<KitUsage> getKitUsages() {
    Result<Record3<String, String, Integer>> records = getDslContext().select(ROUND.GAME_CODE, ROUND_PLAYER_PICKUP_KIT.KIT, DSL.count().as("times_used"))
        .from(ROUND_PLAYER_PICKUP_KIT)
        .innerJoin(ROUND).on(ROUND.ID.eq(ROUND_PLAYER_PICKUP_KIT.ROUND_ID))
        .groupBy(ROUND_PLAYER_PICKUP_KIT.KIT)
        .orderBy(DSL.count().desc())
        .fetch();

    Integer totalTimesUsed = records.stream()
        .map(r -> r.get("times_used", Integer.class))
        .reduce(0, Integer::sum);

    return records.stream()
        .map(r -> toKitUsage(r, totalTimesUsed))
        .collect(toList());
  }

  public List<KitUsage> getKitUsagesForPlayer(int playerId) {
    Result<Record3<String, String, Integer>> records = getDslContext().select(ROUND.GAME_CODE, ROUND_PLAYER_PICKUP_KIT.KIT, DSL.count().as("times_used"))
        .from(ROUND_PLAYER_PICKUP_KIT)
        .innerJoin(ROUND).on(ROUND.ID.eq(ROUND_PLAYER_PICKUP_KIT.ROUND_ID))
        .where(ROUND_PLAYER_PICKUP_KIT.PLAYER_ID.eq(playerId))
        .groupBy(ROUND_PLAYER_PICKUP_KIT.KIT)
        .orderBy(DSL.count().desc())
        .limit(LIMIT_PLAYER_STATS)
        .fetch();

    Integer totalTimesUsed = records.stream()
        .map(r -> r.get("times_used", Integer.class))
        .reduce(0, Integer::sum);

    return records.stream()
        .map(r -> toKitUsage(r, totalTimesUsed))
        .collect(toList());
  }

  private static KitUsage toKitUsage(Record r, int totalTimesUsed) {
    Integer timesUsed = r.get("times_used", Integer.class);
    String gameCode = r.get(ROUND.GAME_CODE);
    String code = r.get(ROUND_PLAYER_PICKUP_KIT.KIT);
    KitService.KitNameAndWeapons kitNameAndWeapons = KitService.findKitNameAndWeapons(gameCode, code);
    return new KitUsage()
        .setGameCode(gameCode)
        .setCode(code)
        .setName(kitNameAndWeapons.getName())
        .setWeapons(kitNameAndWeapons.getWeapons())
        .setTimesUsed(timesUsed)
        .setPercentage(percentage(timesUsed, totalTimesUsed));
  }
}
