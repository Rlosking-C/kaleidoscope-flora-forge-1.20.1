package com.rlosking.flora.client;

import com.rlosking.flora.FloraConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;

public class FloraConfigScreen extends Screen {

    public static void register() {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (client, modsScreen) -> new FloraConfigScreen(modsScreen)));
    }

    private final Screen parent;

    private record Row(DoubleSupplier getter, DoubleConsumer setter,
                       double min, double max, boolean integer,
                       EditBox box, Component label) {
    }

    private final List<Row> rows = new ArrayList<>();

    public FloraConfigScreen(Screen parent) {
        super(Component.translatable("config.kaleidoscope_flora.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        rows.clear();
        int centre = width / 2;
        int y = height / 4 - 10;

        ForgeConfigSpec.DoubleValue duration = FloraConfig.CONFIG.effectDurationMultiplier;
        addRow(Component.translatable("config.kaleidoscope_flora.effectDurationMultiplier"),
                duration::get, duration::set, 0.25, 4.0, false, centre, y);
        y += 34;

        ForgeConfigSpec.DoubleValue aura = FloraConfig.CONFIG.auraRangeMultiplier;
        addRow(Component.translatable("config.kaleidoscope_flora.auraRangeMultiplier"),
                aura::get, aura::set, 0.25, 3.0, false, centre, y);
        y += 34;

        ForgeConfigSpec.DoubleValue vampiric = FloraConfig.CONFIG.vampiricHealPercent;
        addRow(Component.translatable("config.kaleidoscope_flora.vampiricHealPercent"),
                vampiric::get, vampiric::set, 0.0, 100.0, false, centre, y);
        y += 34;

        ForgeConfigSpec.IntValue jumps = FloraConfig.CONFIG.doubleJumpCount;
        addRow(Component.translatable("config.kaleidoscope_flora.doubleJumpCount"),
                jumps::get, v -> jumps.set((int) Math.round(v)), 0, 8, true, centre, y);

        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> save())
                .bounds(centre - 100, height - 40, 200, 20)
                .build());
    }

    private void addRow(Component label, DoubleSupplier getter, DoubleConsumer setter,
                        double min, double max, boolean integer, int centre, int y) {
        EditBox box = new EditBox(font, centre - 50, y, 100, 18, label);
        box.setValue(integer
                ? Integer.toString((int) Math.round(getter.getAsDouble()))
                : Double.toString(getter.getAsDouble()));
        addRenderableWidget(box);
        rows.add(new Row(getter, setter, min, max, integer, box, label));
    }

    private void save() {
        for (Row row : rows) {
            double parsed;
            try {
                parsed = Double.parseDouble(row.box().getValue().trim());
            } catch (NumberFormatException ignored) {
                continue;
            }
            row.setter().accept(Math.max(row.min(), Math.min(row.max(), parsed)));
        }
        FloraConfig.SPEC.save();
        onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        graphics.drawCenteredString(font, title, width / 2, height / 4 - 44, 0xFFFFFF);
        for (Row row : rows) {
            graphics.drawString(font, row.label(), width / 2 - 160, row.box().getY() + 5, 0xA0A0A0);
        }
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }
}
