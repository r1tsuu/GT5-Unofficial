package gregtech.loaders.oreprocessing;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;

public class ProcessingArrows implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingArrows() {
        OrePrefixes.arrowGtWood.add(this);
        OrePrefixes.arrowGtPlastic.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
            ItemStack aStack) {
        ItemStack tOutput = GT_Utility.copyAmount(1L, aStack);
        GT_Utility.updateItemStack(tOutput);
        GT_Utility.ItemNBT.addEnchantment(
                tOutput,
                Enchantment.smite,
                EnchantmentHelper.getEnchantmentLevel(Enchantment.smite.effectId, tOutput) + 3);
        GT_Values.RA.addChemicalBathRecipe(
                GT_Utility.copyAmount(1L, aStack),
                Materials.HolyWater.getFluid(25L),
                tOutput,
                null,
                null,
                null,
                100,
                2);

        tOutput = GT_Utility.copyAmount(1L, aStack);
        GT_Utility.updateItemStack(tOutput);
        GT_Utility.ItemNBT.addEnchantment(
                tOutput,
                Enchantment.fireAspect,
                EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, tOutput) + 3);
        GT_Values.RA.addChemicalBathRecipe(
                GT_Utility.copyAmount(1L, aStack),
                Materials.FierySteel.getFluid(25L),
                tOutput,
                null,
                null,
                null,
                100,
                2);

        tOutput = GT_Utility.copyAmount(1L, aStack);
        GT_Utility.updateItemStack(tOutput);
        GT_Utility.ItemNBT.addEnchantment(
                tOutput,
                Enchantment.fireAspect,
                EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, tOutput) + 1);
        GT_Values.RA.addChemicalBathRecipe(
                GT_Utility.copyAmount(1L, aStack),
                Materials.Blaze.getMolten(18L),
                tOutput,
                null,
                null,
                null,
                100,
                2);

        tOutput = GT_Utility.copyAmount(1L, aStack);
        GT_Utility.updateItemStack(tOutput);
        GT_Utility.ItemNBT.addEnchantment(
                tOutput,
                Enchantment.knockback,
                EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, tOutput) + 1);
        GT_Values.RA.addChemicalBathRecipe(
                GT_Utility.copyAmount(1L, aStack),
                Materials.Rubber.getMolten(18L),
                tOutput,
                null,
                null,
                null,
                100,
                2);

        tOutput = GT_Utility.copyAmount(1L, aStack);
        GT_Utility.updateItemStack(tOutput);
        GT_Utility.ItemNBT.addEnchantment(
                tOutput,
                gregtech.api.enchants.Enchantment_EnderDamage.INSTANCE,
                EnchantmentHelper
                        .getEnchantmentLevel(gregtech.api.enchants.Enchantment_EnderDamage.INSTANCE.effectId, tOutput)
                        + 1);
        GT_Values.RA.addChemicalBathRecipe(
                GT_Utility.copyAmount(1L, aStack),
                Materials.Mercury.getFluid(25L),
                tOutput,
                null,
                null,
                null,
                100,
                2);

        if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial)
                && !aMaterial.contains(SubTag.NO_WORKING)) {
            switch (aPrefix) {
                case arrowGtWood:
                    GT_ModHandler.addCraftingRecipe(
                            GT_OreDictUnificator.get(OrePrefixes.arrowGtWood, aMaterial, 1L),
                            GT_Proxy.tBits,
                            new Object[] { "  A", " S ", "F  ", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                                    OreDictNames.craftingFeather, 'A', OrePrefixes.toolHeadArrow.get(aMaterial) });
                case arrowGtPlastic:
                    GT_ModHandler.addCraftingRecipe(
                            GT_OreDictUnificator.get(OrePrefixes.arrowGtPlastic, aMaterial, 1L),
                            GT_Proxy.tBits,
                            new Object[] { "  A", " S ", "F  ", 'S', OrePrefixes.stick.get(Materials.Plastic), 'F',
                                    OreDictNames.craftingFeather, 'A', OrePrefixes.toolHeadArrow.get(aMaterial) });
                default:
                    break;
            }
        }
    }
}
