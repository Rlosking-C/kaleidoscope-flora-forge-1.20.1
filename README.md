# Kaleidoscope Flora — Forge 1.20.1 port

> **This is an unofficial port.** The upstream mod is
> [Kaleidoscope Flora](https://github.com/Rlosking-C/kaleidoscope-flora) by
> Rlosking-C, built for Minecraft 1.21.1 on NeoForge. This repository carries
> that mod over to Minecraft 1.20.1 on Forge. Both the code (MIT) and the art
> (CC BY-NC-SA 4.0) come from upstream, so the same terms apply here.

Brew drinks from flowers, and let every flower have a flavor of its own.

An addon for [Kaleidoscope Cookery](https://modrinth.com/mod/kaleidoscope-cookery)
that turns vanilla flowers into a full brewing experience.

## Gameplay

Gather flowers, craft each one into a tea bag, brew a full pot in Cookery's
teapot, then pour it out cup by cup and drink to gain the flower's blessing.

- **26 flower drinks** - each with its own recipe, effect, and a
  flower-language quote
- **22 out of the box, 4 more with Vanilla Backport** - the four 1.21.4+
  flowers do not exist on 1.20.1, so those drinks and tea bags only register
  when a mod providing them is installed
- **The teapot, not the stockpot** - one tea bag brews a whole pot in Cookery's
  teapot; pour it out with Cookery's empty teacups
- **23 custom effects** - walk on water, glow through the night, dig up
  artifacts with a sniffer's instincts, and more
- **15 advancements**

## Requirements

| Dependency | Version | Required |
|---|---|---|
| Minecraft | 1.20.1 | Yes |
| Forge | 47.3.25 | Yes |
| [Kaleidoscope Cookery](https://modrinth.com/mod/kaleidoscope-cookery) | 1.5.0 – 1.5.x (Forge 1.20.1) | Yes |
| [Just Enough Items](https://modrinth.com/mod/jei) | 15.x | No (info pages for every drink) |
| [Sodium Dynamic Lights](https://modrinth.com/mod/sodium-dynamic-lights) | - | No (full glow effect at night) |
| [RyoamicLights](https://modrinth.com/mod/ryoamiclights) | - | No (full glow effect at night) |
| [Vanilla Backport](https://modrinth.com/mod/vanillabackport) | - | No (unlocks 4 crossover drinks, **see Known issues**) |

Note: the two dynamic lights mods are incompatible with each other — install
one or the other, not both.

## Build

```
gradlew build
```

Produces `build/libs/kaleidoscope_flora-<version>.jar`. The build reads its
version from the jar manifest, so bump `mod_version` in `gradle.properties`.

MixinExtras is bundled into the jar (`jarJar`), so nothing extra needs to be
installed at runtime.

## Known issues

### Vanilla Backport recipe gating — fixed

`the_gaze`, `as_you_wish`, `springtime_stroll` and `fleeting_bloom` used to have
**no working recipe**: all nine of their tea bag and teapot recipes failed to
load on every startup with `Unknown item` errors, so neither the drinks nor
their tea bags could be crafted.

**Cause.** The recipes were gated with a top-level `"forge:conditions"` block,
which **Forge 1.20.1 does not read** — it is not a vanilla recipe field and not
a Forge hook, so GSON silently ignored it and the recipes loaded
unconditionally against items that had not been registered. The NeoForge
original uses `"neoforge:conditions"`, which is valid there; only the namespace
was changed when porting.

The Java-side gating was correct and unaffected throughout: without Vanilla
Backport the mod registers 22 tea bags, exactly as intended.

**Fix applied.** Forge 1.20.1 gates recipes with a wrapping serializer,
`forge:conditional`, so the nine files now read:

```json
{
  "type": "forge:conditional",
  "recipes": [
    {
      "conditions": [ { "type": "forge:mod_loaded", "modid": "vanillabackport" } ],
      "recipe": { "...": "the original recipe object" }
    }
  ]
}
```

Files changed:

```
data/kaleidoscope_flora/recipes/teapot/the_gaze.json
data/kaleidoscope_flora/recipes/teapot/as_you_wish.json
data/kaleidoscope_flora/recipes/teapot/springtime_stroll.json
data/kaleidoscope_flora/recipes/teapot/fleeting_bloom.json
data/kaleidoscope_flora/recipes/tea_bag/closed_eyeblossom.json
data/kaleidoscope_flora/recipes/tea_bag/open_eyeblossom.json
data/kaleidoscope_flora/recipes/tea_bag/cactus_flower_tea_bag.json
data/kaleidoscope_flora/recipes/tea_bag/wildflowers_tea_bag.json
data/kaleidoscope_flora/recipes/tea_bag/golden_dandelion_tea_bag.json
```

**Behaviour now.** With Vanilla Backport installed the four drinks and their
tea bags are craftable; without it those recipes are skipped silently, which
matches the Java side registering only 22 tea bags. Note that the recipes are
gated on the mod being present, **not** on individual flowers existing — if a
Vanilla Backport build ever ships only some of the four flowers, the recipe for
a missing flower becomes a dead recipe again rather than an error.

### "As You Wish" is built on gold, not on its flower

The golden dandelion has no 1.20.1 equivalent in this port either, so that
drink's tea bag is crafted from `minecraft:gold_nugget`. Its recipe shape is
therefore asymmetric compared to the other 25.

### Damage effects are not numerically identical to the 1.21.1 original

Three effects react to damage, and the port hooks a different stage of the
damage pipeline than upstream does:

| Effect | Port | Upstream |
|---|---|---|
| Crimson Heartbeat (lifesteal) | `LivingHurtEvent` | `LivingDamageEvent.Pre` |
| Fire Waltz (ignite on hit) | `LivingHurtEvent` | `LivingDamageEvent.Pre` |
| Tender Thorns (reflect) | `LivingHurtEvent` | `LivingDamageEvent.Pre` |

`LivingHurtEvent` fires at the top of `LivingEntity#hurt`, **before** armour and
enchantment reduction; `LivingDamageEvent` fires inside `actuallyHurt`, after
armour, enchantment and absorption. So these three effects are computed from
**pre-mitigation** damage and are therefore stronger against heavily armoured
targets than they are upstream.

The upside is intended: `LivingHurtEvent` always fires, which fixes the case
where a hit fully absorbed by absorption never triggered the reaction at all.

> The comment on `FloraEvents#onIncomingDamage` describes `LivingHurtEvent` as
> firing "after armor and enchantment reduction" — that is inaccurate and worth
> correcting.

### Saves from the 1.21.1 version are not migrated

This port deliberately starts on a clean save; the milk soup base compatibility
alias from upstream was not carried over. A stockpot saved by Flora 0.3.1 or
earlier can still hold `kaleidoscope_flora:milk` in its NBT, and Cookery's
stockpot renderer looks that id up without a null check. Cookery's Forge 1.20.1
build does expose the equivalent `SoupBaseManager` / `ISoupBase` /
`ISoupBaseRender` API, so this is portable rather than blocked — it was simply
not ported.

## Not yet verified

The only recorded run of this port is a **dedicated server**, so every
client-side path is unverified: the config screen, the JEI info pages, the
lifesteal heart flash, the two dynamic-light integrations, the water-surface
collision box, and the air-swimming local-player mixin. A single client run
would settle all of them.

## Repository layout notes

- The real project root is nested one level down, inside a same-named folder;
  the outer folder holds nothing.
- `libs/` contains decompiled third-party sources used for reference while
  porting (JEI, Forge, Kaleidoscope Cookery). It is not part of the build.
- `disabled-mods/` holds a leftover build of an unrelated sibling project that
  used to break every dev run by polluting the mixin environment. See the
  README in that folder.
- `README.txt`, `changelog.txt`, `CREDITS.txt` and `LICENSE.txt` are untouched
  Forge MDK files, not this project's documentation.
- `compile_output.txt` and `debug-sauce-color-not-showing.md` are leftovers from
  that sibling project and do not describe this mod.

## License

- Code: MIT
- Art assets: CC BY-NC-SA 4.0 — credit and non-commercial use required;
  adaptations must use the same license. Redraw the textures if you would
  rather not carry those terms.

## Credits

- [Kaleidoscope Flora](https://github.com/Rlosking-C/kaleidoscope-flora) by
  Rlosking-C — the upstream mod this port is derived from
- [Kaleidoscope Cookery](https://modrinth.com/mod/kaleidoscope-cookery) —
  upstream mod and required dependency
