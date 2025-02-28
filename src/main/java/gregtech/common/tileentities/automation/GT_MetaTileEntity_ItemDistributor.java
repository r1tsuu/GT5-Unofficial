package gregtech.common.tileentities.automation;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_ITEMDISTRIBUTOR;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_ITEMDISTRIBUTOR_GLOW;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;

import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Buffer;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_ItemDistributor extends GT_MetaTileEntity_Buffer implements IAddUIWidgets {

    private byte[] itemsPerSide = new byte[6];
    private byte currentSide = 0, currentSideItemCount = 0;

    public GT_MetaTileEntity_ItemDistributor(int aID, String aName, String aNameRegional, int aTier) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                28,
                new String[] { "Distributes Items between different Machine Sides", "Default Items per Machine Side: 0",
                        "Use Screwdriver to increase/decrease Items per Side",
                        "Does not consume energy to move Item" });
    }

    public GT_MetaTileEntity_ItemDistributor(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
            String aDescription) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
    }

    public GT_MetaTileEntity_ItemDistributor(String aName, int aTier, int aInvSlotCount, String aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_ItemDistributor(String aName, int aTier, int aInvSlotCount, String[] aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ItemDistributor(
                this.mName,
                this.mTier,
                this.mInventory.length,
                this.mDescriptionArray,
                this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(
                TextureFactory.of(AUTOMATION_ITEMDISTRIBUTOR),
                TextureFactory.builder().addIcon(AUTOMATION_ITEMDISTRIBUTOR_GLOW).glow().build());
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aSide == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
            boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return mTextures[0][aColorIndex + 1];
        } else {
            return mTextures[1][aColorIndex + 1];
        }
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] returnTextures = new ITexture[2][17][];
        ITexture baseIcon = getOverlayIcon(), pipeIcon = TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_OUT);
        for (int i = 0; i < 17; i++) {
            returnTextures[0][i] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i], baseIcon };
            returnTextures[1][i] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i], pipeIcon, baseIcon };
        }
        return returnTextures;
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return getBaseMetaTileEntity().getFrontFacing() == aSide || itemsPerSide[aSide] == 0;
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return getBaseMetaTileEntity().getFrontFacing() != aSide && itemsPerSide[aSide] > 0;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < this.mInventory.length - 1;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        itemsPerSide = aNBT.getByteArray("mItemsPerSide");
        if (itemsPerSide.length != 6) {
            itemsPerSide = new byte[6];
        }
        currentSide = aNBT.getByte("mCurrentSide");
        currentSideItemCount = aNBT.getByte("mCurrentSideItemCount");
    }

    @Override
    protected void moveItems(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        fillStacksIntoFirstSlots();
        int movedItems;
        TileEntity adjacentTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(currentSide);
        int inspectedSides = 0;
        while (itemsPerSide[currentSide] == 0) {
            currentSide = (byte) ((currentSide + 1) % 6);
            currentSideItemCount = 0;
            adjacentTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(currentSide);
            inspectedSides += 1;
            if (inspectedSides == 6) {
                return;
            }
        }
        movedItems = GT_Utility.moveOneItemStack(
                aBaseMetaTileEntity,
                adjacentTileEntity,
                currentSide,
                GT_Utility.getOppositeSide(currentSide),
                null,
                false,
                (byte) 64,
                (byte) 1,
                (byte) (itemsPerSide[currentSide] - currentSideItemCount),
                (byte) 1);
        currentSideItemCount += movedItems;
        if (currentSideItemCount >= itemsPerSide[currentSide]) {
            currentSide = (byte) ((currentSide + 1) % 6);
            currentSideItemCount = 0;
        }
        if (movedItems > 0 || aBaseMetaTileEntity.hasInventoryBeenModified()) {
            mSuccess = 50;
        }
        fillStacksIntoFirstSlots();
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        // Adjust items per side by 1 or -1, constrained to the cyclic interval [0, 127]
        itemsPerSide[aSide] += aPlayer.isSneaking() ? -1 : 1;
        itemsPerSide[aSide] = (byte) ((itemsPerSide[aSide] + 128) % 128);
        GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("211", "Items per side: ") + itemsPerSide[aSide]);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByteArray("mItemsPerSide", itemsPerSide);
        aNBT.setByte("mCurrentSide", currentSide);
        aNBT.setByte("mCurrentSideItemCount", currentSideItemCount);
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        super.setItemNBT(aNBT);
        aNBT.setByteArray("mItemsPerSide", itemsPerSide);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        addEmitEnergyButton(builder);
        addEmitRedstoneButton(builder);
        addInvertRedstoneButton(builder);
        builder.widget(
                new DrawableWidget().setDrawable(GT_UITextures.PICTURE_ARROW_22_RED.apply(87, true)).setPos(62, 60)
                        .setSize(87, 22));
        addInventorySlots(builder);
    }
}
