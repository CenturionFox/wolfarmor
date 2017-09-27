package com.attributestudios.wolfarmor.common.loot;

import com.attributestudios.wolfarmor.api.util.Definitions;
import com.attributestudios.wolfarmor.api.util.Definitions.ResourceLocations.LootTables;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LootHandler {
    private LootHandler() {
        MinecraftForge.EVENT_BUS.register(this);
        LootTableList.register(LootTables.DUNGEON_INJECT);
    }

    public static void init() {
        new LootHandler();
    }

    @SubscribeEvent
    public void dungeonLootLoad(LootTableLoadEvent event) {
        String name = event.getName().toString();
        String chests = "minecraft:chests/";
        if(name.startsWith(chests)) {
            String fileName = name.substring(chests.length());
            switch(fileName) {
                case "abandoned_mineshaft":
                case "desert_pyramid":
                case "jungle_temple":
                case "nether_bridge":
                case "simple_dungeon":
                case "stronghold_corridor":
                case "stronghold_crossing":
                case "village_blacksmith":
                    event.getTable().addPool(getInjectPool(LootTables.DUNGEON_INJECT));
                    break;
                default:
                    break;
            }
        }
    }

    private LootPool getInjectPool(ResourceLocation lootDungeonInject) {
        return new LootPool(new LootEntry[] {
                getInjectEntry(lootDungeonInject)},
                new LootCondition[0],
                new RandomValueRange(1),
                new RandomValueRange(1),
                Definitions.MOD_ID + "_injected_pool");
    }

    private LootEntry getInjectEntry(ResourceLocation lootDungeonInject) {
        return new LootEntryTable(lootDungeonInject,
                1,
                0,
                new LootCondition[0],
                Definitions.MOD_ID + "_injected_entry");
    }
}
