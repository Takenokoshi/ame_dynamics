package ame_dynamics;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(AMEDConstants.MODID)
public class AMEDBootstrap {
    public AMEDBootstrap() {
        DistExecutor.unsafeRunForDist(() -> AMEDClient::new, () -> AMEDynamics::new);
    }
}
