package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;

public class GT_Cover_Crafting extends GT_CoverBehavior {

    /**
     * @deprecated use {@link #GT_Cover_Crafting(ITexture coverTexture)} instead
     */
    @Deprecated
    public GT_Cover_Crafting() {
        this(null);
    }

    public GT_Cover_Crafting(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
            long aTimer) {
        return false;
    }

    @Override
    public boolean onCoverRightclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
            EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if ((aPlayer instanceof EntityPlayerMP)) {
            ((EntityPlayerMP) aPlayer).getNextWindowId();
            ((EntityPlayerMP) aPlayer).playerNetServerHandler.sendPacket(
                    new S2DPacketOpenWindow(((EntityPlayerMP) aPlayer).currentWindowId, 1, "Crafting", 9, true));
            ((EntityPlayerMP) aPlayer).openContainer = new ContainerWorkbench(
                    ((EntityPlayerMP) aPlayer).inventory,
                    ((EntityPlayerMP) aPlayer).worldObj,
                    aTileEntity.getXCoord(),
                    aTileEntity.getYCoord(),
                    aTileEntity.getZCoord()) {

                @Override
                public boolean canInteractWith(EntityPlayer player) {
                    return true;
                }
            };
            ((EntityPlayerMP) aPlayer).openContainer.windowId = ((EntityPlayerMP) aPlayer).currentWindowId;
            ((EntityPlayerMP) aPlayer).openContainer.addCraftingToCrafters((EntityPlayerMP) aPlayer);
        }
        return true;
    }
}
