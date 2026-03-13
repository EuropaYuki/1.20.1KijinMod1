package com.YukiSato.KijinMod.keybind;


import net.minecraft.client.KeyMapping;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.openjdk.nashorn.internal.runtime.Context;

import java.awt.event.KeyEvent;

public class KijinKeyBind {

//    public static final String SAA = "key.category.kijinmod.ni";
//    public static final String N = "key.kijinmod.boost";
//
//    public static final KeyMapping BOOST_KEY = new KeyMapping(N, KeyConflictContext.IN_GAME,
//            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, SAA);

    public static KeyMapping[] kijinKey;

    public static void register(FMLClientSetupEvent event) {
        kijinKey = new KeyMapping[13];

        kijinKey[0] = create("boost", KeyEvent.VK_Z);
        kijinKey[1] = create("super_boost", KeyEvent.VK_X);
        kijinKey[2] = create("fly", KeyEvent.VK_I);
        kijinKey[3] = create("off_fly", KeyEvent.VK_K);
        kijinKey[4] = create("ghost", KeyEvent.VK_V);
        kijinKey[5] = create("ghost_off", KeyEvent.VK_G);
        kijinKey[6] = create("teleport", KeyEvent.VK_C);
        kijinKey[7] = create("scope", KeyEvent.VK_R);
        kijinKey[8] = create("saru", KeyEvent.VK_X);
        kijinKey[9] = create("kindan_on", KeyEvent.VK_O);
        kijinKey[10] = create("kindan_off", KeyEvent.VK_K);
        kijinKey[11] = create("wewewe", KeyEvent.VK_Y);
        kijinKey[12] = create("wwwww", KeyEvent.VK_B);
        for (int a = 0; a < kijinKey.length; a++) {

        }
    }
    private static KeyMapping create(String name, int key) {
        return new KeyMapping("key.kijinmod." + name, key, "key.category.kjinmod");
    }
}
