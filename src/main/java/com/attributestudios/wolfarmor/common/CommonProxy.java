package com.attributestudios.wolfarmor.common;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.common.network.WolfArmorGuiHandler;
import com.attributestudios.wolfarmor.event.WolfArmorEntityEventHandler;
import com.attributestudios.wolfarmor.event.WolfArmorPlayerEventHandler;
import com.attributestudios.wolfarmor.item.WolfArmorItems;
import com.attributestudios.wolfarmor.item.crafting.RecipeWolfArmorDyes;
import com.attributestudios.wolfarmor.item.crafting.WolfArmorRecipes;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.oredict.RecipeSorter;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Handles mod initialization and common methods for client and server side interactions.
 */
public class CommonProxy {
    //region Fields

    private static final Map<String, ResourceLocation> CACHED_RESOURCE_LOCATIONS = Maps.newHashMap();

    //endregion Fields

    //region Public / Protected Methods

    //region Pre-Initialization

    /**
     * Handles pre-initialization tasks
     *
     * @param preInitializationEvent The pre-initialization event
     */
    public void preInit(@Nonnull FMLPreInitializationEvent preInitializationEvent) {
        registerItems(preInitializationEvent);
        registerItemRenderers(preInitializationEvent);
    }

    /**
     * Registers entity renderers.  This should not do anything in a common proxy.
     * @param preInitializationEvent The pre-initialization event
     */
    protected void registerEntityRenderingHandlers() {
        // does nothing server-side
    }

    /**
     * Registers all items for this mod
     */
    protected void registerItems(@Nonnull FMLPreInitializationEvent preInitializationEvent) {
        WolfArmorItems.init();
    }

    /**
     * Registers all item renderers for this mod
     * @param preInitializationEvent the initialization event
     */
    protected void registerItemRenderers(@Nonnull FMLPreInitializationEvent preInitializationEvent) {

    }

    //endregion Pre-Initialization

    //region Initialization

    /**
     * Handles initialization tasks
     *
     * @param initializationEvent The initialization event
     */
    public void init(@Nonnull FMLInitializationEvent initializationEvent) {
        this.registerItemColorHandlers(initializationEvent);
        this.registerRecipes(initializationEvent);
        this.registerEventListeners(initializationEvent);
        //Actually there are no need to register a new entity to storage the ammo data.
        //It will make the compatibility to another mods much more harder.
        //Instead you should using the forge capability system to override the wolf's data by attaching data to entity object.
        //And more information at: https://mcforge.readthedocs.io/en/latest/datastorage/capabilities/
        //this.registerEntities(event);
    }

    /**
     * Registers item colorization handlers
     * @param initializationEvent The initialization event
     */
    protected void registerItemColorHandlers(@Nonnull FMLInitializationEvent initializationEvent) {
    }

    /**
     * Registers all crafting recipes for this mod
     * @param initializationEvent The initialization event
     */
    @SuppressWarnings("WeakerAccess")
    protected void registerRecipes(@Nonnull FMLInitializationEvent initializationEvent) {
        WolfArmorRecipes.init();
        RecipeSorter.register(WolfArmorMod.MOD_ID + ":WolfArmorDyes", RecipeWolfArmorDyes.class, RecipeSorter.Category.SHAPELESS, "");
    }

    /**
     * Registers all event listeners for this mod.
     * @param initializationEvent The initialization event
     */
    @SuppressWarnings("WeakerAccess")
    protected void registerEventListeners(@SuppressWarnings("unused") @Nonnull FMLInitializationEvent initializationEvent) {
        MinecraftForge.EVENT_BUS.register(new WolfArmorEntityEventHandler());
        MinecraftForge.EVENT_BUS.register(new WolfArmorPlayerEventHandler());
    }

    //endregion Initialization

    //region Post-Initialization

    /**
     * Handles post-initialization tasks
     * @param postInitializationEvent The post-initialization event.
     */
    public void postInit(@Nonnull FMLPostInitializationEvent postInitializationEvent) {
        this.registerGuiHandlers(postInitializationEvent);
    }
    
    private boolean IsRenderSetted = false;

    public void serverAboutToStart(@Nonnull FMLServerAboutToStartEvent event) {
    	if (!IsRenderSetted) {
    		IsRenderSetted = true; this.registerEntityRenderingHandlers();
    	}
    }
    /**
     * Registers all GUI handlers for the mod.
     * @param postInitializationEvent The post-initialization event.
     */
    private void registerGuiHandlers(@SuppressWarnings("unused") @Nonnull FMLPostInitializationEvent postInitializationEvent) {
        NetworkRegistry.INSTANCE.registerGuiHandler(WolfArmorMod.instance, new WolfArmorGuiHandler());
    }

    //endregion Post-Initialization

    //endregion Public / Protected Methods

    //region Accessors / Mutators

    //TODO: Common lib / api implementation
    /**
     * Gets or creates and caches a resource location for the given path.
     * @param path The path
     * @return A resource location for the path
     */
    @SuppressWarnings("unused")
    public ResourceLocation getResourceLocation(String path) {
        ResourceLocation resourceLocation = CACHED_RESOURCE_LOCATIONS.get(path);

        if(resourceLocation == null) {
            int index = path.indexOf(':');
            String domain = WolfArmorMod.MOD_ID;

            if(index > -1) {
                domain = path.substring(0, index);
                path = path.substring(index);
            }

            resourceLocation = new ResourceLocation(domain, path);
            CACHED_RESOURCE_LOCATIONS.put(path, resourceLocation);
        }

        return resourceLocation;
    }

    //endregion Accessors / Mutators

}
