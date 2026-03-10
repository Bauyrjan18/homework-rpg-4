package com.narxoz.rpg.bridge;
import com.narxoz.rpg.composite.CombatNode;

public class SingleTargetSkill extends Skill {
    public SingleTargetSkill(String skillName, int basePower, EffectImplementor effect) {
        super(skillName, basePower, effect);
    }

    @Override
    public void cast(CombatNode target) {
        int damage = resolvedDamage();
        CombatNode actualTarget = findAliveLeaf(target);
        if (actualTarget != null) {
            actualTarget.takeDamage(damage);
        }
    }
    private CombatNode findAliveLeaf(CombatNode node) {
        if (node.getChildren().isEmpty()) {
            return node.isAlive() ? node : null;
        }
        for (CombatNode child : node.getChildren()) {
            if (child.isAlive()) {
                CombatNode leaf = findAliveLeaf(child);
                if (leaf != null) return leaf;
            }
        }
        return null;
    }
}
