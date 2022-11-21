# Transplant SMP (Fabric)

## If Lifesteal SMP met Origins SMP

### Requirements:
- Fabric Loader (>= 0.14.9)
- Fabric API (>= 0.62.0)
- Installation on both client and server
- Friends

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
You get more/less hearts. This is normal LifeSteal.
```

Arm Transplant:
```
Hotbar slots.

Vanilla defaults: 9 Hotbar slots.
If you have MORE slots, they will be added to the hotbar.
If you have LESS slots, they will be removed in the same order they were added.
```

Skin Transplant:
```
Armor bars.

Vanilla defaults: 10 bars max.
You are able to equip 2 sets of armor at once (with this transplant).
This 2nd set of armor cannot have ANY enchantments or curses, as it will have no effect.

If you have MORE than 10 bars, the extra armor bars will come into effect.
If you have LESS than 10 bars, then you will only feel the effect of that amount of armor bars.
(i.e. If you have full diamond and no 2nd set, and you have 9 bars of armor, the 10th armor bar from full diamond will have no effect.)
```

Stomach Transplant:
```
Hunger bars.

Vanilla defaults: 10 bars max, 20 saturation max, healing above 9 bars.
If you have MORE than 10 bars, you will heal as long as you have above 9 (vanilla) bars.
If you have LESS than 10 bars, the minimum bars to heal with are scaled down accordingly.
Saturation caps are scaled accordingly.
```

### Gamerules:
- `armBalancing` (default: `false`) : If true, Arm Transplant users get an increase in attack speed (not mining speed). A decrease will occur if the player has lost slots.
- `armBalanceAmount` (default: `0.1`) : if `armBalancing` is true, this is the amount of attack speed increase/decrease PER gained/lost slot.
- `noNetheriteSkinSecondary` (default: `false`) : If true, Netherite armor will not be able to be used as a secondary set of armor for Skin Transplant users.

##### Why is it called Transplant SMP?

https://www.youtube.com/playlist?list=PL5biciXbHO_xIDkh_xhxcY0TBVdb-MJ9G

##### Mod compatibility:
Not too bad (comes with AppleSkin compat), report any incompatibilities in the Issues tab and I'll look into it.