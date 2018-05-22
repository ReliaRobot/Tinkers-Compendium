package lance5057.tDefense.proxy;

import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import lance5057.tDefense.Reference;
import lance5057.tDefense.TD_Commands;
import lance5057.tDefense.TinkersDefense;
import lance5057.tDefense.core.items.TDOreDictItem;
import lance5057.tDefense.core.materials.TDMaterials;
import lance5057.tDefense.core.parts.TDParts;
import lance5057.tDefense.core.renderers.BaubleRenderer;
import lance5057.tDefense.core.renderers.SheatheModel;
import lance5057.tDefense.core.tools.TDTools;
import lance5057.tDefense.core.tools.armor.renderers.layers.LayerTDBipedArmor;
import lance5057.tDefense.core.tools.bases.ArmorCore;
import lance5057.tDefense.renderers.deserializers.AlphaColorTextureDeserializer;
import lance5057.tDefense.util.ArmorBuildGuiInfo;
import lance5057.tDefense.util.TDClientRegistry;
import lance5057.tDefense.util.TDModelRegistar;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.TinkerRegistryClient;
import slimeknights.tconstruct.library.client.ToolBuildGuiInfo;
import slimeknights.tconstruct.library.client.material.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ToolPart;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ClientProxy extends CommonProxy {
	// static final ToolModelLoader toolmodel = new ToolModelLoader();
	// public static final ModelSheath sheath = new ModelSheath();
	// ModifierSoulHandler SoulHandler;

	// public static ModelTinkersTabard sheath;

	ToolBuildGuiInfo roundshieldGUI;
	ToolBuildGuiInfo heatershieldGUI;
	ToolBuildGuiInfo towershieldGUI;
	
	ToolBuildGuiInfo zweihanderGUI;
	ToolBuildGuiInfo shearsGUI;
	ToolBuildGuiInfo fishingRodGUI;

	ArmorBuildGuiInfo hoodGUI;
	ArmorBuildGuiInfo shawlGUI;
	ArmorBuildGuiInfo robeGUI;
	ArmorBuildGuiInfo shoesGUI;

	ArmorBuildGuiInfo coifGUI;
	ArmorBuildGuiInfo hauberkGUI;
	ArmorBuildGuiInfo chaussesGUI;
	//ToolBuildGuiInfo bootsGUI;

	ArmorBuildGuiInfo helmGUI;
	ArmorBuildGuiInfo breastplateGUI;
	ArmorBuildGuiInfo grievesGUI;
	ArmorBuildGuiInfo sabatonsGUI;

	ToolBuildGuiInfo sheatheGUI;
	ToolBuildGuiInfo ringGUI;
	ToolBuildGuiInfo amuletGUI;

	public static SheatheModel sheathe;

	@Override
	public void preInit() {
		ClientCommandHandler.instance.registerCommand(new TD_Commands());

		MaterialRenderInfoLoader.addRenderInfo("alpha_color", AlphaColorTextureDeserializer.class);
		
		//register all armor textures into tinkers
		//CustomTextureCreator.registerTexture(new ResourceLocation(Reference.MOD_ID, "armor/hood/_hood_cloth"));

	}

	@Override
	public void init() {
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		RenderPlayer render;
		render = skinMap.get("default");
		render.addLayer(new BaubleRenderer());
		render.addLayer(new LayerTDBipedArmor(render));

		render = skinMap.get("slim");
		render.addLayer(new BaubleRenderer());
		render.addLayer(new LayerTDBipedArmor(render));

		createToolGuis();
		setToolGuis();
		registerToolGuis();
		createToolModels();
	}

	@Override
	public void postInit() {
		registerItemColorHandler(new TDOreDictItem.ColorHandler(), TDMaterials.ingot);
		registerItemColorHandler(new TDOreDictItem.ColorHandler(), TDMaterials.gem);
		registerItemColorHandler(new TDOreDictItem.ColorHandler(), TDMaterials.dust);
		registerItemColorHandler(new TDOreDictItem.ColorHandler(), TDMaterials.nugget);
		registerItemColorHandler(new TDOreDictItem.ColorHandler(), TDMaterials.grain);
		
		TinkersDefense.tab.setDisplayIcon(TDTools.heatershield.buildItemForRendering(ImmutableList.of(TinkerRegistry.getMaterial("iron"),TinkerRegistry.getMaterial("cobalt"),TinkerRegistry.getMaterial("cobalt"),TinkerRegistry.getMaterial("iron"))));
	}

	@Override
	public void registerFluidModels(Fluid fluid) {
		if (fluid == null)
			return;
		Block block = fluid.getBlock();
		if (block != null) {
			Item item = Item.getItemFromBlock(block);
			FluidStateMapper mapper = new FluidStateMapper(fluid);
			if (item != null) {
				ModelBakery.registerItemVariants(item);
				ModelLoader.setCustomMeshDefinition(item, mapper);
			}
			ModelLoader.setCustomStateMapper(block, mapper);
		}
	}

	@Override
	public void registerToolModel(ToolCore tool) {
		ModelRegisterUtil.registerToolModel(tool);
	}
	
	@Override
	public void registerArmorModel(ArmorCore tool) {
		TDModelRegistar.registerToolModel(tool);
	}

	@Override
	public void registerPartModel(ToolPart part) {
		ModelRegisterUtil.registerPartModel(part);
	}

	@Override
	public void registerMatColor(Material mat, int color) {
		mat.setRenderInfo(color);
	}

	public void createToolGuis() {
		roundshieldGUI = new ToolBuildGuiInfo(TDTools.roundshield);
		heatershieldGUI = new ToolBuildGuiInfo(TDTools.heatershield);
		towershieldGUI = new ToolBuildGuiInfo(TDTools.towershield);
		zweihanderGUI = new ToolBuildGuiInfo(TDTools.zweihander);
		shearsGUI = new ToolBuildGuiInfo(TDTools.shears);
		fishingRodGUI = new ToolBuildGuiInfo(TDTools.fishingRod);

		hoodGUI = new ArmorBuildGuiInfo(TDTools.hood);
		shawlGUI = new ArmorBuildGuiInfo(TDTools.shawl);
		robeGUI = new ArmorBuildGuiInfo(TDTools.robe);
		shoesGUI = new ArmorBuildGuiInfo(TDTools.shoes);
		
		//bootsGUI = new ToolBuildGuiInfo(TDTools.boots);
		
		helmGUI = new ArmorBuildGuiInfo(TDTools.helm);
		breastplateGUI = new ArmorBuildGuiInfo(TDTools.breastplate);
		grievesGUI = new ArmorBuildGuiInfo(TDTools.grieves);
		sabatonsGUI = new ArmorBuildGuiInfo(TDTools.sabatons);

		sheatheGUI = new ToolBuildGuiInfo(TDTools.sheathe);
		ringGUI = new ToolBuildGuiInfo(TDTools.ring);
		amuletGUI = new ToolBuildGuiInfo(TDTools.amulet);

	}

	public void setupToolGuis() {
		roundshieldGUI.addSlotPosition(34, 15 + 8);
		roundshieldGUI.addSlotPosition(34, 33 + 8);
		roundshieldGUI.addSlotPosition(34, 51 + 8);

		heatershieldGUI.addSlotPosition(34, 15 + 8);
		heatershieldGUI.addSlotPosition(25, 33 + 8);
		heatershieldGUI.addSlotPosition(43, 33 + 8);
		heatershieldGUI.addSlotPosition(34, 51 + 8);
		
		towershieldGUI.addSlotPosition(34, 15 + 8);
		towershieldGUI.addSlotPosition(25, 33 + 8);
		towershieldGUI.addSlotPosition(43, 33 + 8);
		towershieldGUI.addSlotPosition(34, 51 + 8);

		zweihanderGUI.addSlotPosition(34, 15 + 8);
		zweihanderGUI.addSlotPosition(25, 33 + 8);
		zweihanderGUI.addSlotPosition(43, 33 + 8);
		zweihanderGUI.addSlotPosition(34, 51 + 8);

		hoodGUI.addSlotPosition(34, 15 + 8);
		hoodGUI.addSlotPosition(43, 33 + 8);
		hoodGUI.addSlotPosition(34, 51 + 8);

		shawlGUI.addSlotPosition(34, 15 + 8);
		shawlGUI.addSlotPosition(43, 33 + 8);
		shawlGUI.addSlotPosition(34, 51 + 8);

		robeGUI.addSlotPosition(34, 15 + 8);
		robeGUI.addSlotPosition(43, 33 + 8);
		robeGUI.addSlotPosition(34, 51 + 8);

		shoesGUI.addSlotPosition(34, 15 + 8);
		shoesGUI.addSlotPosition(25, 33 + 8);
		shoesGUI.addSlotPosition(43, 51 + 8);
		shoesGUI.addSlotPosition(34, 51 + 8);
		
//		bootsGUI.addSlotPosition(34, 15 + 8);
//		bootsGUI.addSlotPosition(43, 33 + 8);
//		bootsGUI.addSlotPosition(34, 51 + 8);

		shearsGUI.addSlotPosition(34, 15 + 8);
		shearsGUI.addSlotPosition(43, 33 + 8);
		shearsGUI.addSlotPosition(34, 51 + 8);

		ringGUI.addSlotPosition(34, 15 + 8);
		ringGUI.addSlotPosition(43, 33 + 8);
		ringGUI.addSlotPosition(34, 51 + 8);
		
		breastplateGUI.addSlotPosition(34, 15 + 8);
		breastplateGUI.addSlotPosition(43, 33 + 8);
		breastplateGUI.addSlotPosition(34, 51 + 8);
		breastplateGUI.addSlotPosition(43, 51 + 8);
		
		helmGUI.addSlotPosition(34, 15 + 8);
		helmGUI.addSlotPosition(43, 33 + 8);
		helmGUI.addSlotPosition(34, 51 + 8);
		helmGUI.addSlotPosition(43, 51 + 8);
	}

	public void registerToolGuis() {
		TinkerRegistryClient.addToolBuilding(roundshieldGUI);
		TinkerRegistryClient.addToolBuilding(heatershieldGUI);
		TinkerRegistryClient.addToolBuilding(towershieldGUI);
		TinkerRegistryClient.addToolBuilding(zweihanderGUI);
		TinkerRegistryClient.addToolBuilding(shearsGUI);
		TinkerRegistryClient.addToolBuilding(fishingRodGUI);

		TDClientRegistry.addArmorBuilding(hoodGUI);
		TDClientRegistry.addArmorBuilding(shawlGUI);
		TDClientRegistry.addArmorBuilding(robeGUI);
		TDClientRegistry.addArmorBuilding(shoesGUI);
		
		//TinkerRegistryClient.addToolBuilding(bootsGUI);

		TinkerRegistryClient.addToolBuilding(sheatheGUI);
		TinkerRegistryClient.addToolBuilding(ringGUI);
		TinkerRegistryClient.addToolBuilding(amuletGUI);
		
		TDClientRegistry.addArmorBuilding(helmGUI);
		TDClientRegistry.addArmorBuilding(breastplateGUI);
		TDClientRegistry.addArmorBuilding(grievesGUI);
		TDClientRegistry.addArmorBuilding(sabatonsGUI);
	}

	public void registerPartModels() {
		ModelRegisterUtil.registerPartModel(TDParts.armorPlate);
		ModelRegisterUtil.registerPartModel(TDParts.chainmail);
		ModelRegisterUtil.registerPartModel(TDParts.clasp);
		ModelRegisterUtil.registerPartModel(TDParts.cloth);
		ModelRegisterUtil.registerPartModel(TDParts.filigree);
		ModelRegisterUtil.registerPartModel(TDParts.ringShank);
		ModelRegisterUtil.registerPartModel(TDParts.rivets);
		ModelRegisterUtil.registerPartModel(TDParts.setting);
		ModelRegisterUtil.registerPartModel(TDParts.wire);
	}

	@Override
	public void reloadRenderers() {
		setToolGuis();
	}

	public void setToolGuis() {
		roundshieldGUI.positions.clear();
		roundshieldGUI.addSlotPosition(34, 15);
		roundshieldGUI.addSlotPosition(34, 33);
		roundshieldGUI.addSlotPosition(34, 51);

		heatershieldGUI.positions.clear();
		heatershieldGUI.addSlotPosition(34, 15);
		heatershieldGUI.addSlotPosition(25, 33);
		heatershieldGUI.addSlotPosition(43, 33);
		heatershieldGUI.addSlotPosition(34, 51);
		
		towershieldGUI.positions.clear();
		towershieldGUI.addSlotPosition(34, 15);
		towershieldGUI.addSlotPosition(25, 33);
		towershieldGUI.addSlotPosition(43, 33);
		towershieldGUI.addSlotPosition(34, 51);

		zweihanderGUI.positions.clear();
		zweihanderGUI.addSlotPosition(34, 15);
		zweihanderGUI.addSlotPosition(25, 33);
		zweihanderGUI.addSlotPosition(43, 33);
		zweihanderGUI.addSlotPosition(34, 51);

		hoodGUI.positions.clear();
		hoodGUI.addSlotPosition(34, 15 + 8);
		hoodGUI.addSlotPosition(43, 33 + 8);
		hoodGUI.addSlotPosition(34, 51 + 8);

		shawlGUI.positions.clear();
		shawlGUI.addSlotPosition(34, 15 + 8);
		shawlGUI.addSlotPosition(43, 33 + 8);
		shawlGUI.addSlotPosition(34, 51 + 8);

		robeGUI.positions.clear();
		robeGUI.addSlotPosition(34, 15 + 8);
		robeGUI.addSlotPosition(43, 33 + 8);
		robeGUI.addSlotPosition(34, 51 + 8);

		shoesGUI.positions.clear();
		shoesGUI.addSlotPosition(34, 15 + 8);
		shoesGUI.addSlotPosition(25, 33 + 8);
		shoesGUI.addSlotPosition(43, 51 + 8);
		shoesGUI.addSlotPosition(34, 51 + 8);
		
//		bootsGUI.positions.clear();
//		bootsGUI.addSlotPosition(34, 15 + 8);
//		bootsGUI.addSlotPosition(43, 33 + 8);
//		bootsGUI.addSlotPosition(34, 51 + 8);
		
		breastplateGUI.positions.clear();
		breastplateGUI.addSlotPosition(34, 15 + 8);
		breastplateGUI.addSlotPosition(43, 33 + 8);
		breastplateGUI.addSlotPosition(34, 51 + 8);
		breastplateGUI.addSlotPosition(43, 51 + 8);
		
		helmGUI.positions.clear();
		helmGUI.addSlotPosition(34, 15 + 8);
		helmGUI.addSlotPosition(43, 33 + 8);
		helmGUI.addSlotPosition(34, 51 + 8);
		helmGUI.addSlotPosition(43, 51 + 8);

		sabatonsGUI.positions.clear();
		sabatonsGUI.addSlotPosition(34, 15 + 8);
		sabatonsGUI.addSlotPosition(43, 33 + 8);
		sabatonsGUI.addSlotPosition(34, 51 + 8);
		sabatonsGUI.addSlotPosition(43, 51 + 8);
		
		grievesGUI.positions.clear();
		grievesGUI.addSlotPosition(34, 15 + 8);
		grievesGUI.addSlotPosition(43, 33 + 8);
		grievesGUI.addSlotPosition(34, 51 + 8);
		grievesGUI.addSlotPosition(43, 51 + 8);
		
		shearsGUI.positions.clear();
		shearsGUI.addSlotPosition(34, 15 + 8);
		shearsGUI.addSlotPosition(43, 33 + 8);
		shearsGUI.addSlotPosition(34, 51 + 8);

		fishingRodGUI.positions.clear();
		fishingRodGUI.addSlotPosition(34, 15 + 8);
		fishingRodGUI.addSlotPosition(43, 33 + 8);
		fishingRodGUI.addSlotPosition(34, 51 + 8);

		sheatheGUI.positions.clear();
		sheatheGUI.addSlotPosition(34, 15);
		sheatheGUI.addSlotPosition(34, 33);
		sheatheGUI.addSlotPosition(34, 51);

		ringGUI.positions.clear();
		ringGUI.addSlotPosition(34, 15);
		ringGUI.addSlotPosition(34, 33);
		ringGUI.addSlotPosition(34, 51);

		amuletGUI.positions.clear();
		amuletGUI.addSlotPosition(34, 15);
		amuletGUI.addSlotPosition(34, 33);
		amuletGUI.addSlotPosition(34, 51);
	}

	void createToolModels() {
		sheathe = new SheatheModel();
	}

	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta,
				new ModelResourceLocation(Reference.MOD_ID + ":" + id, "inventory"));
	}

	@Override
	public void registerItemBlockRenderer(Block block, int meta, String file) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), meta,
				new ModelResourceLocation(Reference.MOD_ID + ":" + file, "inventory"));
	}

	public void registerItemColorHandler(IItemColor c, Item i) {
		ItemColors itemcolors = Minecraft.getMinecraft().getItemColors();
		itemcolors.registerItemColorHandler(c, i);
	}

	public static class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {

		public final Fluid fluid;
		public final ModelResourceLocation location;

		public FluidStateMapper(Fluid fluid) {
			this.fluid = fluid;

			// have each block hold its fluid per nbt? hm
			this.location = new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "fluid_block"),
					fluid.getName());
		}

		@Nonnull
		@Override
		protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
			return location;
		}

		@Nonnull
		@Override
		public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
			return location;
		}
	}
}
