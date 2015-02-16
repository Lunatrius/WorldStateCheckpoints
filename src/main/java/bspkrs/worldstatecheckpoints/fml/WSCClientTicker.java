package bspkrs.worldstatecheckpoints.fml;

import net.minecraft.util.ChatComponentText;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.worldstatecheckpoints.WSCSettings;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WSCClientTicker
{
    private static boolean isRegistered = false;

    public WSCClientTicker()
    {
        isRegistered = true;
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event)
    {
        if (event.phase.equals(Phase.START))
            return;

        boolean keepTicking = !(WSCSettings.mc != null && WSCSettings.mc.thePlayer != null && WSCSettings.mc.theWorld != null);

        if (!keepTicking && isRegistered)
        {
            if (bspkrsCoreMod.instance.allowUpdateCheck && WorldStateCheckpointsMod.instance.versionChecker != null)
                if (!WorldStateCheckpointsMod.instance.versionChecker.isCurrentVersion())
                    for (String msg : WorldStateCheckpointsMod.instance.versionChecker.getInGameMessage())
                        WSCSettings.mc.thePlayer.addChatMessage(new ChatComponentText(msg));

            FMLCommonHandler.instance().bus().unregister(this);
            isRegistered = false;
        }
    }

    public static boolean isRegistered()
    {
        return isRegistered;
    }
}
