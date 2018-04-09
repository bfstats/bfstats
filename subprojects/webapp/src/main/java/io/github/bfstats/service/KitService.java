package io.github.bfstats.service;

import com.google.common.collect.ImmutableMap;
import io.github.bfstats.model.KitUsage;

import java.util.Map;

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
      .put("German_Scout_Desert", "German_AT_Desert")
      .put("German_Assault_Desert", "German_AT_Desert")
      .put("German_AT_Desert", "German_AT_Desert")
      .put("German_Medic_Desert", "German_AT_Desert")
      .put("German_Engineer_Desert", "German_AT_Desert")
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

  public static String kitName(String gameCode, String kitCode) {
    String[] parts = kitCode.split("_");
    if (parts.length < 2) {
      return "???";
    }

    String teamCode = parts[0];
    String teamName = teamNameByCodeByGameCode.get(gameCode).getOrDefault(teamCode, teamCode);

    String categoryCode = parts[1];
    String categoryName = categoryNameByCodeByGameCode.get(gameCode).getOrDefault(categoryCode, categoryCode);

    String line = teamName + " " + categoryName;

    if (parts.length > 2) {
      String categoryAlt = parts[2];
      line += " " + categoryAlt;
    }

    String weapons = weaponsByKitCodeByGameCode.get(gameCode).get(kitCode);
    if (weapons != null) {
      line += " (" + weapons + ")";
    } else {
      line = kitCode;
    }

    return line;
  }

  public KitUsage getKit(String gameCode, String kitCode) {
    return new KitUsage()
        .setGameCode(gameCode)
        .setCode(kitCode)
        .setName(kitName(gameCode, kitCode));
  }
}
