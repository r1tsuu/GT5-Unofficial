package gregtech.common.tileentities.machines.multi;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.SteamVariant;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class GT_MetaTileEntity_BrickedBlastFurnace extends GT_MetaTileEntity_PrimitiveBlastFurnace
        implements ISecondaryDescribable {

    private static final ITexture[] FACING_SIDE = { TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_DENSEBRICKS) };
    private static final ITexture[] FACING_FRONT = {
            TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_INACTIVE) };
    private static final ITexture[] FACING_ACTIVE = {
            TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE), TextureFactory.builder()
                    .addIcon(BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE_GLOW).glow().build() };
    private GT_Multiblock_Tooltip_Builder tooltipBuilder;

    public GT_MetaTileEntity_BrickedBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_BrickedBlastFurnace(String aName) {
        super(aName);
    }

    @Override
    public String[] getDescription() {
        return getCurrentDescription();
    }

    @Override
    public boolean isDisplaySecondaryDescription() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    public String[] getPrimaryDescription() {
        return getTooltip().getInformation();
    }

    public String[] getSecondaryDescription() {
        return getTooltip().getStructureInformation();
    }

    protected GT_Multiblock_Tooltip_Builder getTooltip() {
        if (tooltipBuilder == null) {
            tooltipBuilder = new GT_Multiblock_Tooltip_Builder();
            tooltipBuilder.addMachineType("Blast Furnace").addInfo("Controller Block for the Bricked Blast Furnace")
                    .addInfo("Usable for Steel and general Pyrometallurgy")
                    .addInfo("Has a useful interface, unlike other gregtech multis")
                    .addPollutionAmount(GT_Mod.gregtechproxy.mPollutionPrimitveBlastFurnacePerSecond).addSeparator()
                    .beginStructureBlock(3, 4, 3, true).addController("Front center")
                    .addOtherStructurePart("Firebricks", "Everything except the controller")
                    .addStructureInfo("The top block is also empty")
                    .addStructureInfo("You can share the walls of GT multis, so")
                    .addStructureInfo("each additional one costs less, up to 4").toolTipFinisher("Gregtech");
        }
        return tooltipBuilder;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
            boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return aActive ? FACING_ACTIVE : FACING_FRONT;
        }
        return FACING_SIDE;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_BrickedBlastFurnace(this.mName);
    }

    @Override
    protected Block getCasingBlock() {
        return GregTech_API.sBlockCasings4;
    }

    @Override
    protected int getCasingMetaID() {
        return 15;
    }

    @Override
    public String getName() {
        return "Bricked Blast Furnace";
    }

    @Override
    public SteamVariant getSteamVariant() {
        return SteamVariant.PRIMITIVE;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return getTooltip().getStructureHint();
    }
}
