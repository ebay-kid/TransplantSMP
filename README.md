# Transplant SMP (Fabric)

## If Lifesteal SMP met Origins SMP

### Requirements:
- Fabric Loader (>= 0.14.9)
- Fabric API (>= 0.62.0)
- Installation on both client and server
- Friends

### Mod compatibility:
Not too bad (comes with AppleSkin compat), report any incompatibilities in the Issues tab and I'll look into it.

### Usage:
The mod comes with a handful of QOL features (mainly Arm Transplant) and a crafting recipe for an Organ Item.

#### QOL Features:
A few extra keybinds have been added as a QOL for Arm Transplant users. Hotbar slots 10 through 18 are for ease of access to any gained slots via a single-key hotkey. However, one can also use the "Access Second Hotbar" key and the hotkey for a 1 through 9 slot to access the corresponding 10 through 18 slot (1 -> 10, 2 -> 11, etc.)
![image](https://user-images.githubusercontent.com/72238524/203121226-43b8d266-9f86-41fd-ab52-c104c7f0a49b.png)

#### Crafting:

```
Requires:
4 Totems of Undying
4 Diamond Blocks
2 Netherite Ingots
32 Cobwebs
```
![image](https://user-images.githubusercontent.com/72238524/203122811-35f5aa71-3ff0-4d6f-a7c1-b9e6e7a7d67a.png)


### Transplants:
- Heart Transplant
- Arm Transplant
- Skin Transplant
- Stomach Transplant

### Descriptions:
Heart Transplant:
```
Hearts.

Vanilla defaults: 10 Hearts max.
Transplant: Up to 20 Hearts.

You get more/less hearts. This is normal LifeSteal.
```

Arm Transplant:
```
Hotbar slots.

Vanilla defaults: 9 Hotbar slots.
Transplant: Up to 18 Hotbar slots.

If you have MORE slots, they will be added to the hotbar.
If you have LESS slots, they will be removed in the same order they were added.

If gamerule `armBalancing` is `true`, then you will also receive a small increase/decrease
(dep. on more/less slots respectively) in attack speed (not mining speed!) based on the number of slots you have.
```

Skin Transplant:
```
Armor bars.

Vanilla defaults: 10 bars max, one set of armor.
Transplant: Up to 20 bars.

You are able to equip 2 sets of armor at once.
(Enchantments from the second set have no effect).

If you have MORE than 10 bars, the extra armor bars will come into effect.
If you have LESS than 10 bars, then you will only feel the effect of that amount of armor bars.
(i.e. If you have full diamond and no 2nd set, and you have 9 bars of armor, the 10th armor bar from full diamond will have no effect.)
```

Stomach Transplant:
```
Hunger bars.

Vanilla defaults: 10 bars max, 20 saturation max, healing above 9 bars.
Transplant: Up to 20 bars, and up to 40 saturation max (scaling with bars), see below for healing.

If you have MORE than 10 bars, you will heal as long as you have above 9 (vanilla) bars.
If you have LESS than 10 bars, the minimum bars to heal with are scaled down accordingly.
```

### Gamerules:
- `armBalancing` (default: `false`) : If true, Arm Transplant users get an increase in attack speed (not mining speed). A decrease will occur if the player has lost slots.
- `armBalanceAmount` (default: `0.1`) : if `armBalancing` is true, this is the amount of attack speed increase/decrease PER gained/lost slot.
- `noNetheriteSkinSecondary` (default: `false`) : If true, Netherite armor will not be able to be used as a secondary set of armor for Skin Transplant users.

##### Why is it called Transplant SMP?
https://www.youtube.com/playlist?list=PL5biciXbHO_xIDkh_xhxcY0TBVdb-MJ9G
