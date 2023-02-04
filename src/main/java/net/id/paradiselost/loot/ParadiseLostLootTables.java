package net.id.paradiselost.loot;

import com.google.common.collect.Sets;
import net.id.paradiselost.ParadiseLost;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.Set;

public class ParadiseLostLootTables {
    public static final Identifier MOTHER_AUREL_STRIPPING;
    private static final Set<Identifier> LOOT_TABLES = Sets.newHashSet();
    private static final Set<Identifier> LOOT_TABLES_READ_ONLY;

    static {
        LOOT_TABLES_READ_ONLY = Collections.unmodifiableSet(LOOT_TABLES);
        MOTHER_AUREL_STRIPPING = register("gameplay/mother_aurel_log_strip");
    }

    public ParadiseLostLootTables() {
    }

    private static Identifier register(String id) {
        return registerLootTable(ParadiseLost.locate(id));
    }

    private static Identifier registerLootTable(Identifier id) {
        if (LOOT_TABLES.add(id)) {
            return id;
        } else {
            throw new IllegalArgumentException(id + " is already a registered built-in loot table");
        }
    }

    public static Set<Identifier> getAll() {
        return LOOT_TABLES_READ_ONLY;
    }
}
