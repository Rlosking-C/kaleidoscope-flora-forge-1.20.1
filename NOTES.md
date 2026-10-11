# Notes

## A jar from the sibling project poisoned the mixin environment (2026-09-26)

A leftover build of **Kaleidoscope Sauce** was sitting in `run/mods/` and broke every
Gradle dev run in two independent ways. Both are worth remembering, because the first
one blames the wrong project:

1. **It killed mixin initialisation before any mod loaded.** The run died in
   `MixinBootstrap.doInit` with:

   `
   MixinInitialisationError: Error initialising mixin config kaleidoscope_flora.mixins.json
   Caused by: IllegalArgumentException: The specified resource
              'kaleidoscope_flora.mixins.json' was invalid or could not be read
   `

   The message points at **this** mod's config, which is misleading: the config is valid
   and structurally identical to Kaleidoscope Cookery's working one. With that jar
   removed the same run reaches `Done (15.210s)!` and applies every mixin cleanly.

2. **It fails its own mandatory dependency anyway.** Its `mods.toml` requires
   `kaleidoscope_chinesefood`, which is not installed here.

**Lesson:** when a mixin config error names your file but the file is byte-for-byte fine,
suspect another mod in the run folder before touching your own config.

## A missing dependency of ANOTHER mod masquerades as our mixin config (2026-10-02)

Symptom, straight from the dev run:

```
MixinInitialisationError: Error initialising mixin config kaleidoscope_flora.mixins.json
Caused by: IllegalArgumentException: The specified resource
           'kaleidoscope_flora.mixins.json' was invalid or could not be read
```

It names **our** file, and our file is fine (valid JSON, all 11 classes exist). The real
cause was one line earlier in the log:

```
[ERROR] [ModSorter]: Missing or unsupported mandatory dependencies:
    Mod ID: 'platform', Requested by: 'vanillabackport', Expected range: '[1.3.4,)'
```

A jar in ``run/mods`` failed its own dependency check **before** mixins initialise, so
mixin bootstrap died while reading the first config it touched — which happened to be ours.

**Rule: when a mixin config error names your file, search the log for
``Missing or unsupported mandatory dependencies`` before touching your own config.**

Same shape as the Kaleidoscope Sauce case above (that one was missing
``kaleidoscope_chinesefood``). Two for two — treat the mixin message as a symptom, not a
diagnosis.

**Also note**: VanillaBackport needs ``Platform`` (ItsBlackGear, MIT). The 1.21.1 dev run
has the pair; the 1.20.1 run needs ``VanillaBackport-forge-1.20.1-*.jar`` **plus** a Forge
1.20.1 build of ``Platform`` (>= 1.3.4). With only VB installed the game will not start.
