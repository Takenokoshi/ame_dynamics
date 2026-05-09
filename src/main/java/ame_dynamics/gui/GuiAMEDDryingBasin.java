package ame_dynamics.gui;

import java.util.List;

import org.cyclops.integrateddynamics.core.recipe.type.RecipeMechanicalDryingBasin;
import org.cyclops.integrateddynamicscompat.modcompat.jei.mechanicaldryingbasin.MechanicalDryingBasinRecipeCategory;
import org.jetbrains.annotations.NotNull;

import ame_dynamics.blockentity.interfaces.IAMEDDryingBasin;
import ame_dynamics.jei.AMEDJEIPlugin;
import astral_mekanism.block.blockentity.base.BlockEntityRecipeMachine;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.button.MekanismImageButton;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiAMEDDryingBasin<BE extends BlockEntityRecipeMachine<RecipeMechanicalDryingBasin> & IAMEDDryingBasin>
        extends GuiConfigurableTile<BE, MekanismTileContainer<BE>> {

    public GuiAMEDDryingBasin(MekanismTileContainer<BE> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiUpArrow(this, 68, 38));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 164, 15))
                .warning(WarningType.NOT_ENOUGH_ENERGY, tile.getWarningCheck(RecipeError.NOT_ENOUGH_ENERGY));
        addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getActive));
        addRenderableWidget(new GuiFluidGauge(tile::getInputTank, () -> tile.getFluidTanks(null), GaugeType.STANDARD,
                this, 44, 13));
        addRenderableWidget(new GuiFluidGauge(tile::getOutputTank, () -> tile.getFluidTanks(null), GaugeType.STANDARD,
                this, 137, 13));
        addRenderableWidget(new GuiProgress(tile::getScaledProgress, ProgressType.BAR, this, 82, 38))
                .warning(WarningType.INPUT_DOESNT_PRODUCE_OUTPUT,
                        tile.getWarningCheck(RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT));
        addRenderableWidget(new MekanismImageButton(this, 90, 53, 18,
                ResourceLocation.fromNamespaceAndPath("minecraft", "textures/item/knowledge_book.png"),
                () -> AMEDJEIPlugin.getRecipesGui().showTypes(List.of(MechanicalDryingBasinRecipeCategory.TYPE))));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        float widthThird = imageWidth / 3F;
        drawTextScaledBound(guiGraphics, title, widthThird - 7, titleLabelY, titleTextColor(), 2 * widthThird);
        drawString(guiGraphics, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}
