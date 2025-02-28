package gregtech.common.tileentities.machines.basic;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_Printer extends GT_MetaTileEntity_BasicMachine {

    public GT_MetaTileEntity_Printer(int aID, String aName, String aNameRegional, int aTier) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                1,
                "It can copy Books and paint Stuff",
                1,
                1,
                "Printer.png",
                GT_Recipe.GT_Recipe_Map.sPrinterRecipes.mNEIName);
    }

    public GT_MetaTileEntity_Printer(String aName, int aTier, String aDescription, ITexture[][][] aTextures,
            String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 2, 2, aGUIName, aNEIName);
    }

    @Override
    public int checkRecipe() {
        if (getOutputAt(0) != null) {
            this.mOutputBlocked += 1;
        } else if ((GT_Utility.isStackValid(getInputAt(0))) && (getInputAt(0).stackSize > 0)
                && (GT_Utility.isStackInvalid(getSpecialSlot()))
                && (OrePrefixes.block.contains(getInputAt(0)))) {
                    ArrayList<ItemStack> tList = GT_OreDictUnificator
                            .getOres(GT_OreDictUnificator.getAssociation(getInputAt(0)));
                    if (tList.size() > 1) {
                        tList.add(tList.get(0));
                        int i = 0;
                        for (int j = tList.size() - 1; i < j; i++) {
                            if (GT_Utility.areStacksEqual(getInputAt(0), (ItemStack) tList.get(i))) {
                                this.mOutputItems[0] = GT_Utility.copyAmount(1L, tList.get(i + 1));
                                calculateOverclockedNess(1, 32);
                                // In case recipe is too OP for that machine
                                if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                                    return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                                getInputAt(0).stackSize -= 1;
                                return 2;
                            }
                        }
                    }
                }
        this.mMaxProgresstime = 0;
        return 0;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return null;
    }
}
