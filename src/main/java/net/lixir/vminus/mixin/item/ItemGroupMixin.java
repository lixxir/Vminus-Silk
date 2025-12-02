package net.lixir.vminus.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lixir.vminus.Vminus;
import net.lixir.vminus.duck.VisionDuck;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.property.ItemGroupVisionProperties;
import net.lixir.vminus.vision.property.ItemVisionProperties;
import net.lixir.vminus.vision.property.VisionPropertyRegistry;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.lixir.vminus.item.ItemGroupOrder;
import net.lixir.vminus.item.ItemReplacement;
import net.lixir.vminus.item.ItemStackSnapshot;
import net.lixir.vminus.vision.VisionUtils;
import net.lixir.vminus.vision.condition.VisionContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(value = ItemGroup.class, priority = 12000)
public abstract class ItemGroupMixin implements VisionDuck {
    @Unique
    private final ItemGroup vminus$self = (ItemGroup) (Object) this;
    @Unique
    private final HashMap<Item, Item> WAITING_LIST = new HashMap<>();
    @Unique
    private final HashMap<Item, Boolean> WAITING_LIST_ORDER = new HashMap<>();
    @Unique
    private Identifier vminus$visionIdentifier = null;

    @Shadow
    private Set<ItemStack> searchTabStacks;

    @Shadow
    private Collection<ItemStack> displayStacks;

    @ModifyReturnValue(method = "getIcon", at = @At(value = "RETURN"))
    private @NotNull ItemStack vminus$getIcon(ItemStack original) {
        ItemStackSnapshot visionIconItemStack = Vision.Companion.getValue(original, ItemGroupVisionProperties.INSTANCE.getICON());
        if (visionIconItemStack != null)
            original = visionIconItemStack.get();

        // Makes a tab item specifically detectable with its NBT data
        //original.getOrCreateNbt().putBoolean("tab_item", true);
        return original;
    }

    @Inject(method = "updateEntries", at = @At(value = "TAIL"))
    private void vminus$updateEntries(ItemGroup.DisplayContext displayContext, CallbackInfo ci) {
        List<ItemReplacement> removals = vminus$getVision().getValues(ItemGroupVisionProperties.INSTANCE.getREMOVE());
        List<Item> itemsToRemove = new ArrayList<>(removals.stream()
                .filter((ItemReplacement t) -> !t.itemStack().isEmpty())
                .map((ItemReplacement t) -> t.itemStack().getItem())
                .toList());

        List<TagKey<Item>> itemTagsToRemove = new ArrayList<>(removals.stream()
                .map(ItemReplacement::getTag)
                .filter(Objects::nonNull)
                .toList());

        vminus$processHiddenItems(displayStacks, itemsToRemove, itemTagsToRemove);
        vminus$processHiddenItems(searchTabStacks, itemsToRemove, itemTagsToRemove);

        List<ItemGroupOrder> orders = new ArrayList<>(Vision.Companion.getValues((VisionDuck) vminus$self, ItemGroupVisionProperties.INSTANCE.getORDER()));
        List<ItemStack> itemList = new ArrayList<>(displayStacks);

        orders.sort(Comparator.comparingInt(order -> {
            ItemStack targetStack = order.getTargetSnapshot().get();
            return !targetStack.isEmpty() ? vminus$findItemIndex(itemList, targetStack.getItem()) : Integer.MAX_VALUE;
        }));

        for (ItemGroupOrder order : orders) {
            ItemStack itemStack = order.getStackSnapshot().get();
            ItemStack targetItemStack = order.getTargetSnapshot().get();
            TagKey<Item> tagKey = order.getTag();

            if (!itemStack.isEmpty()) {
                Item item = itemStack.getItem();
                if (!targetItemStack.isEmpty()) {
                    Item targetItem = targetItemStack.getItem();
                    vminus$addItemsToTab(targetItem, item, order.getBefore());
                } else {
                    if (!displayStacks.contains(itemStack))
                        displayStacks.add(itemStack);
                    searchTabStacks.add(itemStack);
                }
            } else if (tagKey != null) {
                Registries.ITEM.stream()
                        .filter(item -> item.getRegistryEntry().isIn(tagKey))
                        .forEach(item -> {
                            if (!targetItemStack.isEmpty()) {
                                Item targetItem = targetItemStack.getItem();
                                vminus$addItemsToTab(targetItem, item, order.getBefore());
                            } else {
                                ItemStack stack = item.getDefaultStack();
                                if (!displayStacks.contains(stack))
                                    displayStacks.add(stack);
                                searchTabStacks.add(stack);
                            }
                        });
            }
        }

        boolean updated;
        do {
            updated = false;

            Iterator<Map.Entry<Item, Item>> iterator = WAITING_LIST.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Item, Item> entry = iterator.next();
                Item item = entry.getKey();
                Item targetItem = entry.getValue();
                boolean before = WAITING_LIST_ORDER.getOrDefault(item, false);

                if (displayStacks.contains(new ItemStack(targetItem))) {
                    vminus$addItemsToTab(targetItem, item, before);
                    iterator.remove();
                    WAITING_LIST_ORDER.remove(item);
                    updated = true;
                }
            }
        } while (updated);

        WAITING_LIST.clear();
        WAITING_LIST_ORDER.clear();
    }

    @Unique
    private void vminus$processHiddenItems(@NotNull Collection<ItemStack> itemStacks, List<Item> itemsToRemove, List<TagKey<Item>> itemTagsToRemove) {
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null)
                continue;

            Item item = itemStack.getItem();
            Boolean ban = VisionUtils.INSTANCE.getOverrideValue(item, ItemVisionProperties.INSTANCE.getBAN(), new VisionContext(itemStack));
            ItemReplacement itemReplacement = VisionUtils.INSTANCE.getOverrideValue(item, ItemVisionProperties.INSTANCE.getREPLACE(), new VisionContext(itemStack));
            boolean isTaggedForRemoval = itemTagsToRemove.stream().anyMatch(tagKey ->
                    item.getRegistryEntry().isIn(tagKey));

            if ((ban != null && ban) ||
                    (itemReplacement != null &&
                            (!itemReplacement.itemStack().isEmpty()|| itemReplacement.getTag() != null)) ||
                    itemsToRemove.contains(item) || isTaggedForRemoval) {
                itemsToRemove.add(item);
            }
        }
        itemStacks.removeIf(itemStack -> {
            Item item = itemStack.getItem();

            if (itemsToRemove.contains(item))
                return true;

            return itemTagsToRemove.stream().anyMatch(tagKey ->
                    item.getRegistryEntry().isIn(tagKey)
            );
        });

    }

    @Unique
    private void vminus$addItemsToTab(@Nullable Item targetItem, Item item, boolean before) {
        Boolean ban = Vision.Companion.getValue(item, ItemVisionProperties.INSTANCE.getBAN(), false);
        if (Boolean.TRUE.equals(ban))
            return;
        Boolean targetBan = targetItem != null
                ? Vision.Companion.getValue(targetItem, ItemVisionProperties.INSTANCE.getBAN(), false)
                : false;
        if (Boolean.TRUE.equals(targetBan)) return;

        List<ItemStack> itemList = new ArrayList<>(displayStacks);
        if (itemList.isEmpty())
            return;

        int targetIndex = vminus$findItemIndex(itemList, targetItem);

        ItemStack newItemStack = item.getDefaultStack();
        if (newItemStack.isEmpty())
            return;
        if (vminus$containsItem(itemList, item))
            itemList.remove(newItemStack);
        if (vminus$containsItem(searchTabStacks, item))
            searchTabStacks.remove(newItemStack);

        if (targetIndex != -1 && targetItem != null) {
            itemList.add(before ? targetIndex : targetIndex + 1, newItemStack);
        } else {
            WAITING_LIST.put(item, targetItem);
            WAITING_LIST_ORDER.put(item, before);
            return;
        }

        displayStacks.clear();
        displayStacks.addAll(itemList);

        LinkedHashSet<ItemStack> newSearchTabItems = new LinkedHashSet<>(searchTabStacks);
        if (!vminus$containsItem(newSearchTabItems, item)) {
            newSearchTabItems.add(newItemStack);
        }

        searchTabStacks.clear();
        searchTabStacks.addAll(newSearchTabItems);
    }

    @Unique
    private int vminus$findItemIndex(@NotNull List<ItemStack> items, Item targetItem) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItem() == targetItem) {
                return i;
            }
        }
        return -1;
    }

    @Unique
    private boolean vminus$containsItem(@NotNull Collection<ItemStack> items, Item item) {
        for (ItemStack stack : items) {
            if (stack.getItem() == item) {
                return true;
            }
        }
        return false;
    }

    public Identifier vminus$getVisionIdentifier() {
        return vminus$visionIdentifier;
    }

    public void vminus$setVisionIdentifier(Identifier id) {
        this.vminus$visionIdentifier = id;
    }

    public VisionType<?> vminus$getVisionType() {
        return VisionTypes.INSTANCE.getITEM_GROUP();
    }
}