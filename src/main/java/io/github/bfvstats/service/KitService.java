package io.github.bfvstats.service;

import com.google.common.collect.ImmutableMap;
import io.github.bfvstats.model.KitUsage;

import java.util.Map;

public class KitService {
  private static Map<String, String> kitNameByCode = ImmutableMap.<String, String>builder()
      .put("ARVN_Assault", "CAR15-GRENADES")
      .put("ARVN_Assault_Alt", "M16-GRENADES")
      .put("ARVN_Engineer", "M14-MINES-MORTAR-WRENCH")
      .put("ARVN_Engineer_Alt", "M14-TORCH-C4-WRENCH")
      .put("ARVN_HeavyAssault", "M60-M79")
      .put("ARVN_HeavyAssault_Alt", "M14-L.A.W.")
      .put("ARVN_Recon", "M21-SMOKE-BINOCULARS")
      .put("ARVN_Recon_Alt", "M16SNIPER-SMOKE-BINOCULARS")
      .put("NVA_Assault", "AK47-GRENADES")
      .put("NVA_Assault_Alt", "RPD-GRENADES")
      .put("NVA_Engineer", "MAT49-BOOBY TRAP-MORTAR-WRENCH")
      .put("NVA_Engineer_Alt", "MAT49-MINES-SHOVEL-WRENCH")
      .put("NVA_HeavyAssault", "TYPE56-RPG7V")
      .put("NVA_HeavyAssault_Alt", "SA7-EXPACK")
      .put("NVA_Scout", "SVD-CALTROPS-BINOCULARS")
      .put("NVA_Scout_Alt", "TYPE56-BOUNCING BETTY-BINOCULARS")
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
      .put("USMarine_Recon_alt", "M16SNIPER-SMOKE-BINOCULARS")
      .put("Vietcong_Assault", "AKMS-GRENADES")
      .put("Vietcong_Assault_Alt", "TYPE 53-GRENADES")
      .put("Vietcong_Engineer", "MAT49-PUNGI STICKS-MORTAR-WRENCH")
      .put("Vietcong_Engineer_Alt", "MAT49-SHOVEL-LANDMINES-WRENCH")
      .put("Vietcong_HeavyAssault", "TYPE 56-RPG2")
      .put("Vietcong_HeavyAssault_Alt", "SA7-EXPACK")
      .put("Vietcong_Scout", "M91/30-CALTROPS-BINOCULARS")
      .put("Vietcong_Scout_Alt", "TYPE56-BOUNCING BETTY-TIMEBOMB")
      .build();


  public static String kitName(String kitCode) {
    return kitNameByCode.getOrDefault(kitCode, kitCode);
  }

  public KitUsage getKit(String kitCode) {
    return new KitUsage().setName(kitCode);
  }
}
