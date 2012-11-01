package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import bspkrs.util.ModVersionChecker;
import bspkrs.worldstatecheckpoints.CheckpointManager;
import bspkrs.worldstatecheckpoints.GuiCheckpointsMenu;
import bspkrs.worldstatecheckpoints.GuiLoadCheckpoint;

public class mod_WorldStateCheckpoints extends BaseMod
{
    @MLProp(info="Set to true to allow checking for mod updates, false to disable")
    public static boolean allowUpdateCheck = true;
    
    public static String menuKeyStr = "F6";
    
    public static KeyBinding menuKey = new KeyBinding("CheckpointsMenu", Keyboard.getKeyIndex(menuKeyStr));
    public static boolean justLoaded = false;
    public static String loadMessage = "";
    
    private boolean checkUpdate;
    private ModVersionChecker versionChecker;
    private String versionURL = "https://dl.dropbox.com/u/20748481/Minecraft/1.4.2/worldStateCheckpoints.version";
    private String mcfTopic = "http://www.minecraftforum.net/topic/???-";
    
    private Minecraft mc;
    
    @Override
    public String getName()
    {
        return "WorldStateCheckpoints";
    }

    @Override
    public String getVersion()
    {
        return "ML 1.4.2.r02";
    }
    
    public mod_WorldStateCheckpoints()
    {
        checkUpdate = allowUpdateCheck;
        versionChecker = new ModVersionChecker(getName(), getVersion(), versionURL, mcfTopic, ModLoader.getLogger());
        
        mc = ModLoader.getMinecraftInstance();
    }

    @Override
    public void load()
    {
        versionChecker.checkVersionWithLogging();
        		
        ModLoader.registerKey(this, menuKey, false);
        ModLoader.addLocalization(menuKey.keyDescription, "Checkpoints Menu");
        ModLoader.setInGameHook(this, true, true);
    }

    @Override
    public boolean onTickInGame(float f, Minecraft mc)
    {
        if(checkUpdate)
        {
            if(!versionChecker.isCurrentVersion())
                for(String msg : versionChecker.getInGameMessage())
                    mc.thePlayer.addChatMessage(msg);
            checkUpdate = false;
        }
        
        if(justLoaded)
        {
        	mc.thePlayer.addChatMessage(loadMessage);
        	justLoaded = false;
        	loadMessage = "";
        }
        
        return true;
    }
    
    @Override
    public void keyboardEvent(KeyBinding event)
    {
        if(event.equals(menuKey))
        {
        	if(mc.currentScreen instanceof GuiGameOver)
        		mc.displayGuiScreen(new GuiLoadCheckpoint(true));
        	else
        		mc.displayGuiScreen(new GuiCheckpointsMenu());
        }
    }
}
